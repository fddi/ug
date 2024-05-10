package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.MessageRecord;
import top.ulug.core.deploy.repository.MessageRecordRepository;
import top.ulug.core.deploy.service.MessageRecordService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:21 星期五
 */
public class MessageRecordServiceImpl implements MessageRecordService {
    @Autowired
    private MessageRecordRepository messageRecordRepository;

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
        Pageable pageable = PageRequest.of(pageNo, pageSize,Sort.by("gmtModified").descending());
        Page<MessageRecord> page = messageRecordRepository.findByMultiMessageIdOrUserName(messageRecord.getMultiMessageId(),
                messageRecord.getUserName(), pageable);
        return WrapperDTO.success(new PageDTO<MessageRecord>().convert(page));
    }
}
