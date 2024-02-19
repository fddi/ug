package top.ulug.cms.media.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.SubjectDTO;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */
public interface SubjectService extends CurdService<MediaSubject, SubjectDTO> {


    /**
     * 图标上传
     *
     * @param subjectId id
     * @param file      file
     * @return res
     */
    WrapperDTO<String> uploadLogo(Long subjectId, MultipartFile file);
}
