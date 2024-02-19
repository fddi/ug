package top.ulug.cms.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.media.domain.MediaNetFile;
import top.ulug.cms.media.dto.NetFileDTO;

/**
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaNetFileRepository extends JpaRepository<MediaNetFile, Long>,
        JpaSpecificationExecutor<MediaNetFile> {

    MediaNetFile findFirstByCheckCodeOrderByGmtModifiedDesc(String checkCode);

    @Query("select count(*) as num from MediaNetFile t where t.checkCode=?1")
    Long findCountByCheckCode(String checkCode);

    @Query("select new top.ulug.cms.media.dto.NetFileDTO(t.fileId,t.diskId,t.fileName,t.fileExt,t.checkCode,t.uri,t.fileType,t.fileSize,t.gmtCreate) from MediaNetFile  t where t.diskId=?1 and t.fileName like  %?2% and t.fileType in (?3)")
    Page<NetFileDTO> page(Long diskId, String name, String[] type, Pageable page);
}
