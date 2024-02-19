package top.ulug.cms.media.service;


import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.dto.NetDiskDTO;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
public interface NetDiskService {

    /**
     * 云盘信息
     *
     * @param subjectId 业务id
     * @return disk info
     */
    WrapperDTO<NetDiskDTO> info(Long subjectId);
}
