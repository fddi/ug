package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.e.SQLikeEnum;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.deploy.domain.MessageRecord;
import top.ulug.core.deploy.dto.MessageRecordDTO;
import top.ulug.core.deploy.repository.MessageRecordRepository;
import top.ulug.core.deploy.service.CacheService;
import top.ulug.core.deploy.service.MessageRecordService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:21 星期五
 */
@Service
public class MessageRecordServiceImpl implements MessageRecordService {
    @Autowired
    private MessageRecordRepository messageRecordRepository;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    CacheService cacheService;

    @Override
    public WrapperDTO<String> save(MessageRecord... e) throws Exception {
        List<MessageRecord> list = Arrays.asList(e);
        messageRecordRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(MessageRecord... e) {
        List<MessageRecord> list = Arrays.asList(e);
        messageRecordRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<MessageRecord> findOne(Long id) {
        Optional<MessageRecord> optional = messageRecordRepository.findById(id);
        return optional.map(WrapperDTO::success).orElseGet(() -> WrapperDTO.npe(String.valueOf(id)));
    }

    @Override
    public WrapperDTO<PageDTO<MessageRecord>> findPage(int pageSize, int pageNo, MessageRecord messageRecord) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("gmtModified").descending());
        String userName = StringUtils.isEmpty(messageRecord.getUserName()) ? "" : messageRecord.getUserName();
        Page<MessageRecord> page = messageRecordRepository.findByMultiMessageIdAndUserNameLike(messageRecord.getMultiMessageId(),
                StringUtils.linkSQLike(userName, SQLikeEnum.ALL), pageable);
        return WrapperDTO.success(new PageDTO<MessageRecord>().convert(page));
    }

    @Override
    public WrapperDTO<Long> unreadCount() {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        return WrapperDTO.success(
                messageRecordRepository.countByUserNameAndReadStatusNotOrUserNameAndReadStatusIsNull(
                        authDTO.getAccount().getUserName(), "1", authDTO.getAccount().getUserName()));
    }

    @Override
    public WrapperDTO<PageDTO<MessageRecordDTO>> unreadList(int pageSize, int pageNo) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("gmtModified").descending());
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        Page<MessageRecordDTO> page = messageRecordRepository.readList(authDTO.getAccount().getUserName(), pageable);
        return WrapperDTO.success(new PageDTO<MessageRecordDTO>().convert(page));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> read(Long recordId) {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "token");
        }
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "token");
        }
        messageRecordRepository.updateReadStatus("1", authDTO.getAccount().getUserName(), recordId);
        return WrapperDTO.success();
    }
}
