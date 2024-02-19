package top.ulug.core.deploy.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.security.Digest;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.deploy.domain.DeployClient;
import top.ulug.core.deploy.repository.DeployClientRepository;
import top.ulug.core.deploy.service.ClientService;
import top.ulug.jpa.dto.PageDTO;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by liujf on 2019/3/24.
 * 逝者如斯夫 不舍昼夜
 * <p>
 * 客户端管理
 */
@Service
public class ClientServiceImpl implements ClientService {
    @Value("${spring.application.name}")
    String projectId;
    @Autowired
    DeployClientRepository clientRepository;
    @Resource(name = "redisTemplate")
    ValueOperations<String, DeployClient> redisClient;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean checkClient(Map<String, String> params) {
        String sign = params.get("sign");
        String appId = requestUtils.getCurrentAppId();
        if (StringUtils.isEmpty(sign) || StringUtils.isEmpty(appId)) {
            //参数为空
            return false;
        }
        DeployClient client = redisClient.get(appId);
        if (client == null) {
            client = clientRepository.findByClientNameAndStatus(appId, "1");
        }
        if (client == null) {
            //此客户端未注册
            return false;
        }
        redisClient.set(appId, client);
        String queryString = StringUtils.signString(params, client.getClientKey());
        String signRight = Digest.MACSHA256Encrypt(queryString, client.getClientKey());
        if (!sign.equalsIgnoreCase(signRight)) {
            //签名不一致
            return false;
        }
        return true;
    }

    @Override
    public WrapperDTO<String> getClientKey(Long clientId) {
        DeployClient client = clientRepository.findById(clientId).get();
        if (client == null) {
            //此客户端未注册
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "clientId");
        }
        return WrapperDTO.success(client.getClientKey());
    }

    @Override
    public WrapperDTO<List<LabelDTO>> clientList() {
        List<DeployClient> list = clientRepository.findByStatus("1");
        modelMapper.typeMap(DeployClient.class, LabelDTO.class).addMappings(mapper -> {
            mapper.map(DeployClient::getClientId,
                    LabelDTO::setId);
            mapper.map(DeployClient::getClientNote,
                    LabelDTO::setLabel);
            mapper.map(DeployClient::getClientNote,
                    LabelDTO::setTitle);
            mapper.map(DeployClient::getClientName,
                    LabelDTO::setKey);
            mapper.map(DeployClient::getClientName,
                    LabelDTO::setValue);
        });
        List<LabelDTO> labelDTOList = modelMapper.map(list,
                new TypeToken<List<LabelDTO>>() {
                }.getType());
        return WrapperDTO.success(labelDTOList);
    }

    @Override
    public WrapperDTO<String> save(DeployClient... clients) {
        List<DeployClient> list = Arrays.asList(clients);
        for (DeployClient client : list) {
            Long clientId = client.getClientId();
            if (clientId != null && clientId > 0) {
                DeployClient old = clientRepository.findById(clientId).get();
                client.setClientKey(old.getClientKey());
            } else {
                client.setClientKey(StringUtils.createKey(projectId, client.getClientName()));
            }
        }
        clientRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(DeployClient... clients) {
        List<DeployClient> list = Arrays.asList(clients);
        clientRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<DeployClient> findOne(Long id) {
        Optional<DeployClient> optional = clientRepository.findById(id);
        if (!optional.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, String.valueOf(id));
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<DeployClient>> findPage(int pageSize, int pageNo, DeployClient client) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String clientName = StringUtils.isEmpty(client.getClientName()) ? ""
                : client.getClientName();
        Page<DeployClient> page = clientRepository.page(clientName, pageable);
        return WrapperDTO.success(new PageDTO<DeployClient>().convert(page));
    }

}
