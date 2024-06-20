package top.ulug.core.deploy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.ulug.core.deploy.domain.MessageRecord;
import top.ulug.core.deploy.dto.MessageRecordDTO;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:32 星期五
 */
public interface MessageRecordRepository extends JpaRepository<MessageRecord, Long> {

    Page<MessageRecord> findByMultiMessageIdAndUserNameLike(Long multiMessageId, String userName, Pageable pageable);

    Long countByUserNameAndReadStatusNotOrUserNameAndReadStatusIsNull(String u1, String readStatus, String u2);

    @Query("select new top.ulug.core.deploy.dto.MessageRecordDTO(m.messageId,m.title,m.message,m.actionUrl,r.recordId,r.userName,r.readStatus,r.gmtCreate) from MessageRecord r,MultiMessage m where m.messageId=r.multiMessageId and r.userName=:userName order by r.readStatus,r.gmtModified desc")
    Page<MessageRecordDTO> readList(@Param("userName") String userName, Pageable pageable);

    @Modifying
    @Query("update MessageRecord set readStatus=:readStatus where userName=:userName and recordId=:recordId")
    void updateReadStatus(@Param("readStatus") String readStatus, @Param("userName") String userName, @Param("recordId") Long recordId);
}
