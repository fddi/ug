package top.ulug.cms.media.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.SubjectDTO;
import top.ulug.cms.media.event.SubjectCreateEvent;
import top.ulug.cms.media.repository.MediaSubjectRepository;
import top.ulug.cms.media.service.MediaService;
import top.ulug.cms.media.service.SubjectService;
import top.ulug.core.api.service.AuthService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2019/5/4.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class SubjectServiceImpl implements SubjectService {
    @Value("${spring.application.name}")
    String projectId;
    @Autowired
    MediaSubjectRepository subjectRepository;
    @Autowired
    MediaService mediaService;
    @Autowired
    AuthService authService;
    @Autowired
    ApplicationContext publisher;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<String> save(MediaSubject... subjects) {
        if (subjects.length > 1) {
            return WrapperDTO.fail("禁止批量提交保存");
        }
        MediaSubject subject = subjects[0];
        WrapperDTO<AccountDTO> wrapperDTO = authService.checkAccount();
        if (wrapperDTO.getResultData() == null) {
            return WrapperDTO.fail(wrapperDTO.getResultMsg());
        }
        AccountDTO account = wrapperDTO.getResultData();
        if (!authService.checkDevOps() &&
                (!authService.checkUnit(account.getUnitCode()))) {
            //非开发者禁止保存非本单位数据
            return WrapperDTO.noPermission();
        }
        subject.setAreaCode(account.getAreaCode());
        Long subjectId = subject.getSubjectId();
        if (subjectId != null && subjectId > 0) {
            MediaSubject old = subjectRepository.findById(subjectId).get();
            subject.setSubjectKey(old.getSubjectKey());
            if (subject.getLogo() == null) {
                subject.setLogo(old.getLogo());
            }
        } else {
            subject.setSubjectKey(StringUtils.createKey(projectId, subject.getSubjectName()));
        }
        subject = subjectRepository.save(subject);
        publisher.publishEvent(new SubjectCreateEvent(this, subject));
        return WrapperDTO.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> del(MediaSubject... subjects) {
        List<MediaSubject> list = Arrays.asList(subjects);
        for (MediaSubject subject : list) {
            Optional<MediaSubject> opl = subjectRepository.findById(subject.getSubjectId());
            if (opl.isPresent()) {
                //禁止保存非本单位数据
                if (!authService.checkDevOps() &&
                        (!authService.checkUnit(opl.get().getUnitCode()))) {
                    return WrapperDTO.noPermission();
                }
                mediaService.delImage(opl.get().getLogo());
            }
        }
        subjectRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<SubjectDTO> findOne(Long id) {
        Optional<MediaSubject> optional = subjectRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("MediaSubject");
        }
        return WrapperDTO.success(
                modelMapper.map(optional.get(), SubjectDTO.class));
    }

    @Override
    public WrapperDTO<PageDTO<SubjectDTO>> findPage(int pageSize, int pageNo, MediaSubject subject) {
        String name = subject.getSubjectName() == null ? "" : subject.getSubjectName();
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SubjectDTO> page = subjectRepository.page(subject.getUnitCode(),
                name, pageable);
        return WrapperDTO.success(new PageDTO<SubjectDTO>().convert(page));
    }


    @Override
    public WrapperDTO<String> uploadLogo(Long subjectId, MultipartFile file) {
        Optional<MediaSubject> opl = subjectRepository.findById(subjectId);
        if (!opl.isPresent()) {
            return WrapperDTO.npe("MediaSubject");
        }
        MediaSubject subject = opl.get();
        MediaImage image = new MediaImage();
        image.setImgTags(subject.getSubjectName());
        if (subject.getLogo() != null) {
            mediaService.delImage(subject.getLogo());
        }
        try {
            String imageKey = mediaService.saveImage(image, file);
            subject.setLogo(imageKey);
            subjectRepository.save(subject);
            return WrapperDTO.success(imageKey);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapperDTO.fail(e.getMessage());
        }
    }

}
