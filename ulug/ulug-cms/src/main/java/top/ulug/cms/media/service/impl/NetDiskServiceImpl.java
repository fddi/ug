package top.ulug.cms.media.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.media.domain.MediaNetDisk;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.NetDiskDTO;
import top.ulug.cms.media.repository.MediaNetDiskRepository;
import top.ulug.cms.media.repository.MediaSubjectRepository;
import top.ulug.cms.media.service.NetDiskService;

import java.util.Optional;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class NetDiskServiceImpl implements NetDiskService {
    @Autowired
    private MediaSubjectRepository subjectRepository;
    @Autowired
    private MediaNetDiskRepository diskRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<NetDiskDTO> info(Long subjectId) {
        Optional<MediaSubject> ol = subjectRepository.findById(subjectId);
        if (!ol.isPresent()) {
            return WrapperDTO.npe("subject");
        }
        MediaNetDisk disk = diskRepository.findBySubjectIdAndDefaultFlag(subjectId, "1");
        return WrapperDTO.success(modelMapper.map(disk, NetDiskDTO.class));
    }
}
