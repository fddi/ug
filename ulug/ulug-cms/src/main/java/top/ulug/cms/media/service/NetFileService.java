package top.ulug.cms.media.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.domain.MediaNetFile;
import top.ulug.cms.media.dto.NetFileDTO;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
public interface NetFileService extends CurdService<MediaNetFile, NetFileDTO> {

    /**
     * 上传文件
     *
     * @param diskId 云盘id
     * @param file   文件
     * @return result
     */
    WrapperDTO<String> upload(Long diskId, MultipartFile file);

    /**
     * 文件下载
     *
     * @param fileId 文件ID
     * @return FILE
     */
    ResponseEntity<byte[]> download(Long fileId);
}
