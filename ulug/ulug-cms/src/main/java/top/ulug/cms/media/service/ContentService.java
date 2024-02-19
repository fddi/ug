package top.ulug.cms.media.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.domain.MediaContent;
import top.ulug.cms.media.dto.ContentDTO;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */
public interface ContentService extends CurdService<MediaContent, ContentDTO> {

    /**
     * 内容上传
     *
     * @param content 内容
     * @param file    封面图片
     * @return dto
     */
    WrapperDTO<String> upload(MediaContent content, MultipartFile file);

    /**
     * @param contentId id
     * @param status    状态
     * @return dto
     */
    WrapperDTO<String> updatePublishStatus(Long contentId, String status);

    /**
     * @param contentId id
     * @param sort      排序
     * @return
     */
    WrapperDTO<String> updateSort(Long contentId, int sort);
}
