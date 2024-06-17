package top.ulug.core.deploy.service;

import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.inf.CurdService;
import top.ulug.core.deploy.domain.MessageRecord;

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
}
