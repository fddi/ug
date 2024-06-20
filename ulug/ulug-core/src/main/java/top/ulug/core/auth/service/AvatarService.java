package top.ulug.core.auth.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;

import java.io.IOException;

/**
 * @Author liu
 * @Date 2024/6/18 下午10:26 星期二
 */
public interface AvatarService {
    /**
     * 图标上传
     *
     * @param file file
     * @return res
     */
    WrapperDTO<String> upload(MultipartFile file) throws Exception;

    /**
     * 获取图片
     *
     * @param userName userName
     * @return 文件流
     */
    ResponseEntity<byte[]> read(String userName);
}
