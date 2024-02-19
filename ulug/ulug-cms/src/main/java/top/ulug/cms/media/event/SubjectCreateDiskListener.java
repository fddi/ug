package top.ulug.cms.media.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.ulug.base.util.FileUtils;
import top.ulug.cms.media.domain.MediaNetDisk;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.repository.MediaNetDiskRepository;

import java.io.File;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
@Component
public class SubjectCreateDiskListener {
    @Value("${project.media.disk.path}")
    private String diskDefaultPath;
    @Value("${project.media.disk.default-size}")
    private Long diskDefaultSize;

    @Autowired
    MediaNetDiskRepository diskRepository;
    //网盘类别
    public static final String SUBJECT_DISK = "V3,";
    private final Logger LOG = LoggerFactory.getLogger(SubjectCreateDiskListener.class);

    @EventListener
    public void createDisk(SubjectCreateEvent event) {
        MediaSubject subject = event.getSubject();
        if (SUBJECT_DISK.contains(subject.getSubjectType())) {
            return;
        }
        MediaNetDisk old = diskRepository
                .findBySubjectIdAndDefaultFlag(subject.getSubjectId(), "1");
        if (old != null) {
            return;
        }
        LOG.info("subject create Disk");
        MediaNetDisk netDisk = new MediaNetDisk();
        netDisk.setSubject(subject);
        netDisk.setDiskName(subject.getSubjectCode());
        netDisk.setAreaCode(subject.getAreaCode());
        netDisk.setUnitCode(subject.getUnitCode());
        netDisk.setDefaultFlag("1");
        netDisk.setDiskSize(diskDefaultSize * 1024 * 1024);
        netDisk.setUsedSize(0);
        String location = diskDefaultPath + File.separator + subject.getUnitCode();
        FileUtils.createDirIfNotExist(location);
        netDisk.setLocation(location);
        netDisk.setStatus("1");
        diskRepository.save(netDisk);
    }
}
