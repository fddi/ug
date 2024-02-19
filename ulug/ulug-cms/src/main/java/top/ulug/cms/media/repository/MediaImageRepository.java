package top.ulug.cms.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.ulug.cms.media.domain.MediaImage;

/**
 * Created by liujf on 2020/2/21.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaImageRepository extends JpaRepository<MediaImage, String>,
        JpaSpecificationExecutor<MediaImage> {

}
