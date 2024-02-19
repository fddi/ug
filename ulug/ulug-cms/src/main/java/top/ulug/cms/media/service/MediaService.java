package top.ulug.cms.media.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.cms.media.domain.MediaImage;

/**
 * Created by liujf on 2020/2/22.
 * 逝者如斯夫 不舍昼夜
 */
public interface MediaService {

    /**
     * 将img转换为BASE64编码保存表中
     *
     * @param image     img
     * @param imageFile file
     * @return imageId
     */
    String saveImage(MediaImage image, MultipartFile imageFile) throws Exception;

    /**
     * 获取图片
     *
     * @param imgKey imgKey
     * @return 文件流
     */
    ResponseEntity<byte[]> readImage(String imgKey);

    /**
     * 删除图片记录
     * @param imgKey 图片信息
     */
    void delImage(String imgKey);
}
