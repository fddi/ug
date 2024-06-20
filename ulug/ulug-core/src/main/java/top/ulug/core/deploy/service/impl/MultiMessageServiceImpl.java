package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ulug.base.dto.MessageDTO;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.SQLikeEnum;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthRole;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.repository.AuthRoleRepository;
import top.ulug.core.deploy.domain.MessageRecord;
import top.ulug.core.deploy.domain.MultiMessage;
import top.ulug.core.deploy.repository.MultiMessageRepository;
import top.ulug.core.deploy.service.MessageRecordService;
import top.ulug.core.deploy.service.MultiMessageService;
import top.ulug.core.deploy.service.SseMessageService;

import java.util.*;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:30 星期五
 */
@Service
public class MultiMessageServiceImpl implements MultiMessageService {
    @Autowired
    private MultiMessageRepository multiMessageRepository;
    @Autowired
    private AuthRoleRepository authRoleRepository;
    @Autowired
    private SseMessageService sseMessageService;
    @Autowired
    private MessageRecordService recordService;

    @Override
    public WrapperDTO<String> save(MultiMessage... e) throws Exception {
        List<MultiMessage> list = Arrays.asList(e);
        multiMessageRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(MultiMessage... e) {
        List<MultiMessage> list = Arrays.asList(e);
        multiMessageRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<MultiMessage> findOne(Long id) {
        Optional<MultiMessage> optional = multiMessageRepository.findById(id);
        return optional.map(WrapperDTO::success).orElseGet(() -> WrapperDTO.npe(String.valueOf(id)));
    }

    @Override
    public WrapperDTO<PageDTO<MultiMessage>> findPage(int pageSize, int pageNo, MultiMessage multiMessage) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("gmtModified").descending());
        String title = StringUtils.isEmpty(multiMessage.getTitle()) ? "" : multiMessage.getTitle();
        Page<MultiMessage> page = multiMessageRepository.findByTitleLike(StringUtils.linkSQLike(title, SQLikeEnum.ALL), pageable);
        return WrapperDTO.success(new PageDTO<MultiMessage>().convert(page));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> send(MultiMessage multiMessage, String roleIds) throws Exception {
        if (multiMessage == null || multiMessage.getMessageId() == null || roleIds == null) {
            return WrapperDTO.npe("multiMessage");
        }
        String[] tmp = roleIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String s : tmp) {
            ids.add(Long.parseLong(s));
        }
        List<AuthRole> roles = authRoleRepository.findByRoleIdIn(ids);
        List<AuthUser> users = new ArrayList<>();
        for (AuthRole role : roles) {
            users.addAll(role.getUserList());
        }
        users = this.deduplication(users);
        List<MessageRecord> records = new ArrayList<>();
        for (AuthUser user : users) {
            MessageDTO<MultiMessage> dto = new MessageDTO<>();
            dto.setData(multiMessage);
            sseMessageService.send(user.getUserName(), dto);
            MessageRecord record = new MessageRecord();
            record.setMultiMessageId(multiMessage.getMessageId());
            record.setSendStatus("1");
            record.setUserName(user.getUserName());
            records.add(record);
        }
        MessageRecord[] array = new MessageRecord[records.size()];
        recordService.save(records.toArray(array));
        return WrapperDTO.success();
    }

    private List<AuthUser> deduplication(List<AuthUser> list) {
        List<AuthUser> newList = new ArrayList<>();
        Set set = new HashSet();
        for (AuthUser user : list) {
            if (set.add(user.getUserId()) && "1".equals(user.getStatus())) {
                newList.add(user);
            }
        }
        return newList;
    }
}
