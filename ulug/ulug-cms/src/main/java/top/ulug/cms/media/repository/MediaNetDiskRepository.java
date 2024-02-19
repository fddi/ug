package top.ulug.cms.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.ulug.cms.media.domain.MediaNetDisk;

/**
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaNetDiskRepository extends JpaRepository<MediaNetDisk, Long>,
        JpaSpecificationExecutor<MediaNetDisk> {

    MediaNetDisk findBySubjectIdAndDefaultFlag(long subjectId, String defaultFlag);
}
