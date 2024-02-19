package top.ulug.cms.media.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.security.Base64;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.repository.MediaImageRepository;
import top.ulug.cms.media.service.MediaService;
import top.ulug.jpa.tool.HbNative;
import ug.template.engine.core.launcher.CombExecutor;

import java.util.Optional;

/**
 * Created by liujf on 2020/2/22.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class MediaServiceImpl implements MediaService {
    @Value("${spring.application.name}")
    String projectId;
    @Autowired
    MediaImageRepository imageRepository;
    @Autowired
    CombExecutor combExecutor;
    @Autowired
    HbNative hbNative;

    @Override
    public String saveImage(MediaImage image, MultipartFile imageFile) throws Exception {
        assert image != null;
        if (image.getImgKey() == null) {
            image.setImgKey(StringUtils.createFileKey(projectId, image.getImgTags()));
        }
        String filename = imageFile.getOriginalFilename();
        assert filename != null;
        if (!StringUtils.regexMatch(filename.toLowerCase(), StringUtils.PATTERN_IMG)) {
            throw new Exception("不是系统支持的图片格式");
        }
        String extension = filename.substring(filename.lastIndexOf("."));
        image.setImgType(extension);
        String encodeData = Base64.encodeBytes(imageFile.getBytes());
        image.setImgData(encodeData);
        imageRepository.save(image);
        return image.getImgKey();
    }

    @Override
    public ResponseEntity<byte[]> readImage(String imgKey) {
        if (StringUtils.isEmpty(imgKey)) {
            return null;
        }
        Optional<MediaImage> opl = imageRepository.findById(imgKey);
        if (opl.isEmpty()) {
            return null;
        }
        MediaImage image = opl.get();
        String fileName = image.getImgKey() + image.getImgType();
        try {
            byte[] bytes = Base64.decode(image.getImgData());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> response = new ResponseEntity<>(bytes, headers, statusCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public void delImage(String imgKey) {
        if (!StringUtils.isEmpty(imgKey)) {
            Optional<MediaImage> optional = imageRepository.findById(imgKey);
            optional.ifPresent(mediaImage -> imageRepository.delete(mediaImage));
        }
    }

}
