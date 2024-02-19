package top.ulug.cms.media.service;

import org.springframework.http.ResponseEntity;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.domain.MediaNetFileShare;

import java.util.List;

/**
 * Created by liujf on 2020-06-19.
 * 逝者如斯夫 不舍昼夜
 */
public interface NetFileShareService {


    /**
     * 文件共享状态控制
     *
     * @param fileId    文件id
     * @param shareInfo 共享信息 status为0则删除共享
     * @return result
     */
    WrapperDTO<MediaNetFileShare> shareControl(Long fileId, MediaNetFileShare shareInfo);

    /**
     * 文件共享链接列表
     *
     * @param fileId 文件id
     * @return list
     */
    WrapperDTO<List<MediaNetFileShare>> shareList(Long fileId);

    /**
     * 获取文件
     *
     * @param key  共享key
     * @param code 提取码，可为空
     * @return 文件流
     */
    ResponseEntity<byte[]> dl(String key, String code);
}
