package top.ulug.cms.media.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaContent;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.ContentDTO;
import top.ulug.cms.media.repository.MediaContentRepository;
import top.ulug.cms.media.repository.MediaSubjectRepository;
import top.ulug.cms.media.service.ContentService;
import top.ulug.cms.media.service.MediaService;
import top.ulug.core.api.service.AuthService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2019/5/4.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    MediaContentRepository contentRepository;
    @Autowired
    MediaSubjectRepository subjectRepository;
    @Autowired
    AuthService authService;
    @Autowired
    MediaService mediaService;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<String> upload(MediaContent content, MultipartFile file) {
        Optional<MediaSubject> op = subjectRepository.findById(content.getSubjectId());
        if (op.isEmpty()) {
            return WrapperDTO.npe("MediaSubject");
        }
        if (!authService.checkDevOps()
                && !authService.checkUnit(op.get().getUnitCode())) {
            return WrapperDTO.noPermission();
        }
        if (file != null) {
            MediaImage image = new MediaImage();
            image.setImgTags(content.getContentTitle());
            if (content.getCover() != null) {
                mediaService.delImage(content.getCover());
            }
            try {
                String imageKey = mediaService.saveImage(image, file);
                content.setCover(imageKey);
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(e.getMessage());
            }
        }
        content.setAreaCode(op.get().getAreaCode());
        content.setUnitCode(op.get().getUnitCode());
        content.setSubject(op.get());
        content.setSort(1);
        content.setPublishStatus("0");
        if (content.getContentId() != null) {
            MediaContent old = contentRepository.findById(content.getContentId()).get();
            content.setSort(old.getSort());
            content.setPublishStatus(old.getPublishStatus());
        }
        String summary = content.getSummary();
        if (StringUtils.isEmpty(summary)) {
            summary = StringUtils.matherFind(content.getArticle(), 100, StringUtils.PATTERN_SUMMARY);
        }
        content.setSummary(summary);
        content = contentRepository.save(content);
        return WrapperDTO.success(String.valueOf(content.getContentId()));
    }

    @Override
    public WrapperDTO<String> updatePublishStatus(Long contentId, String status) {
        Optional<MediaContent> opl = contentRepository.findById(contentId);
        if (!opl.isPresent()) {
            return WrapperDTO.npe("MediaContent");
        }
        if (!authService.checkDevOps()
                && !authService.checkUnit(opl.get().getUnitCode())) {
            return WrapperDTO.noPermission();
        }
        MediaContent content = opl.get();
        status = status == null ? "1" : status;
        content.setPublishStatus(status);
        contentRepository.save(content);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> updateSort(Long contentId, int sort) {
        Optional<MediaContent> opl = contentRepository.findById(contentId);
        if (!opl.isPresent()) {
            return WrapperDTO.npe("MediaContent");
        }
        if (!authService.checkDevOps()
                && !authService.checkUnit(opl.get().getUnitCode())) {
            return WrapperDTO.noPermission();
        }
        MediaContent content = opl.get();
        content.setSort(sort);
        contentRepository.save(content);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> save(MediaContent... contents) {
        return null;
    }

    @Override
    public WrapperDTO<String> del(MediaContent... contents) {
        List<MediaContent> list = Arrays.asList(contents);
        for (MediaContent content : list) {
            Optional<MediaContent> opl = contentRepository.findById(content.getContentId());
            if (!authService.checkDevOps()
                    && !authService.checkUnit(opl.get().getUnitCode())) {
                return WrapperDTO.noPermission();
            }
            opl.ifPresent(mediaContent -> mediaService.delImage(mediaContent.getCover()));
        }
        contentRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<ContentDTO> findOne(Long id) {
        Optional<MediaContent> optional = contentRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("MediaContent");
        }
        return WrapperDTO.success(
                modelMapper.map(optional.get(), ContentDTO.class));
    }

    @Override
    public WrapperDTO<PageDTO<ContentDTO>> findPage(int pageSize, int pageNo, MediaContent content) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String contentTitle = content.getContentTitle() == null ? "" : content.getContentTitle();
        Date startDate = content.getGmtModified() == null ?
                DateTimeUtils.getDate("1900", "yyyy") : content.getGmtModified();
        Date endDate = content.getGmtModified() == null ?
                new Date() : DateTimeUtils.getLastDayOfMonth(content.getGmtModified());
        Page<ContentDTO> page = contentRepository.page(content.getSubjectId(), contentTitle,
                startDate, endDate, pageable);
        return WrapperDTO.success(new PageDTO<ContentDTO>().convert(page));
    }

}
