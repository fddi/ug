package top.ulug.cms.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.ulug.cms.media.domain.MediaNetFileShare;

import java.util.List;

/**
 * Created by liujf on 2020-06-10.
 * 逝者如斯夫 不舍昼夜
 */
public interface MediaNetFileShareRepository extends JpaRepository<MediaNetFileShare, Long>,
        JpaSpecificationExecutor<MediaNetFileShare> {

    void deleteByFileId(Long fileId);

    List<MediaNetFileShare> findByFileId(Long fileId);

    MediaNetFileShare findByShareKey(String key);
}
