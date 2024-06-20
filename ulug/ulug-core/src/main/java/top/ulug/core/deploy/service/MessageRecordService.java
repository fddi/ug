package top.ulug.core.deploy.service;

import top.ulug.base.dto.PageDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.CurdService;
import top.ulug.core.deploy.domain.MessageRecord;
import top.ulug.core.deploy.domain.MultiMessage;
import top.ulug.core.deploy.dto.MessageRecordDTO;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:14 星期五
 */
public interface MessageRecordService extends CurdService<MessageRecord, MessageRecord> {
    /**
     * 未读数量
     *
     * @return count
     */
    WrapperDTO<Long> unreadCount();

    /**
     * 未读列表
     *
     * @return list
     */
    WrapperDTO<PageDTO<MessageRecordDTO>> unreadList(int pageSize, int pageNo);

    /**
     * 读
     *
     * @param messageId 消息ID
     * @return res
     */
    WrapperDTO<String> read(Long messageId);
}
