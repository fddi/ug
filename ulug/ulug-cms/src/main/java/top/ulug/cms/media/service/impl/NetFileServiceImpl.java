package top.ulug.cms.media.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.security.Digest;
import top.ulug.base.util.FileUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaNetDisk;
import top.ulug.cms.media.domain.MediaNetFile;
import top.ulug.cms.media.dto.NetFileDTO;
import top.ulug.cms.media.repository.MediaNetDiskRepository;
import top.ulug.cms.media.repository.MediaNetFileRepository;
import top.ulug.cms.media.repository.MediaNetFileShareRepository;
import top.ulug.cms.media.service.NetFileService;
import top.ulug.core.api.service.AuthService;
import top.ulug.jpa.dto.PageDTO;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class NetFileServiceImpl implements NetFileService {
    @Autowired
    MediaNetDiskRepository diskRepository;
    @Autowired
    MediaNetFileRepository fileRepository;
    @Autowired
    MediaNetFileShareRepository shareRepository;
    @Value("${spring.application.name}")
    private String projectId;
    @Autowired
    AuthService authService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> upload(Long diskId, MultipartFile file) {
        Optional<MediaNetDisk> opl = diskRepository.findById(diskId);
        if (!opl.isPresent()) {
            return WrapperDTO.npe("disk");
        }
        MediaNetDisk disk = opl.get();
        long size = file.getSize() + disk.getUsedSize();
        if (size >= disk.getDiskSize()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_DISK_OUT_SIZE, null);
        }
        MediaNetFile netFile = new MediaNetFile();
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        String extension = fileName.substring(fileName.lastIndexOf("."));
        String tag = StringUtils.linkTags(projectId, fileName, String.valueOf(file.getSize()));
        String checkCode = Digest.MD5Encrypt(tag);
        netFile.setNetDisk(disk);
        netFile.setCheckCode(checkCode);
        netFile.setFileName(fileName);
        netFile.setFileExt(extension);
        netFile.setFileType(findFileType(extension));
        MediaNetFile yunFile = fileRepository.findFirstByCheckCodeOrderByGmtModifiedDesc(checkCode);
        if (yunFile != null) {
            netFile.setUri(yunFile.getUri());
            if (disk.getDiskId().equals(yunFile.getDiskId())) {
                //同网盘已存在相同文件直接返回成功
                return WrapperDTO.success();
            }
        } else {
            try {
                FileUtils.saveFile(file, disk.getLocation(), fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(e.getMessage());
            }
            String path = disk.getLocation() + File.separator + fileName;
            netFile.setUri(path);
        }
        netFile.setFileSize(file.getSize());
        fileRepository.save(netFile);
        Long useSize = disk.getUsedSize() + file.getSize();
        disk.setUsedSize(useSize);
        diskRepository.save(disk);
        return WrapperDTO.success();
    }

    @Override
    public ResponseEntity<byte[]> download(Long fileId) {
        if (fileId == null || fileId == 0) {
            return null;
        }
        Optional<MediaNetFile> opl = fileRepository.findById(fileId);
        if (!opl.isPresent()) {
            return null;
        }
        MediaNetFile file = opl.get();
        try {
            File localFile = new File(file.getUri());
            if (!localFile.isFile()) {
                throw new Exception("can not find file");
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) localFile.length());
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(localFile));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            byte[] bytes = bos.toByteArray();
            in.close();
            bos.close();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(file.getFileName(), "utf-8"));
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> response = new ResponseEntity<>(bytes, headers, statusCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public WrapperDTO<String> save(MediaNetFile... netFiles) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> del(MediaNetFile... netFiles) {
        List<MediaNetFile> list = Arrays.asList(netFiles);
        if (list == null || list.size() == 0) {
            return WrapperDTO.npe("file");
        }
        Long total = 0L;
        MediaNetDisk disk = null;
        for (MediaNetFile file : list) {
            file = fileRepository.findById(file.getFileId()).get();
            if ((disk != null && !disk.getDiskId().equals(file.getDiskId()))
                    || (!authService.checkDevOps() && !authService.checkUnit(file.getNetDisk().getUnitCode()))) {
                return WrapperDTO.noPermission();
            }
            disk = file.getNetDisk();
            total += file.getFileSize();
            long count = fileRepository.findCountByCheckCode(file.getCheckCode());
            if (count == 1) {
                //删除物理文件
                File temp = new File(file.getUri());
                if (temp.isFile()) {
                    temp.delete();
                }
            }
            shareRepository.deleteByFileId(file.getFileId());
        }
        disk.setUsedSize(Math.abs(disk.getUsedSize() - total));
        diskRepository.save(disk);
        fileRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<NetFileDTO> findOne(Long id) {
        return null;
    }

    @Override
    public WrapperDTO<PageDTO<NetFileDTO>> findPage(int pageSize, int pageNo, MediaNetFile netFile) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String name = netFile.getFileName() == null ? "" : netFile.getFileName();
        String fileType = netFile.getFileType() == null ? "1,2,3,4,5,9" : netFile.getFileType();
        Page<NetFileDTO> page = fileRepository.page(netFile.getDiskId(), name,
                fileType.split(","), pageable);
        return WrapperDTO.success(new PageDTO<NetFileDTO>().convert(page));
    }

    private String findFileType(String ext) {
        if (ext == null) {
            return null;
        }
        if (StringUtils.regexMatch(ext.toLowerCase(), StringUtils.PATTERN_IMG)) {
            return "1";
        }
        if (StringUtils.regexMatch(ext.toLowerCase(), StringUtils.PATTERN_VIDEO)) {
            return "2";
        }
        if (StringUtils.regexMatch(ext.toLowerCase(), StringUtils.PATTERN_AUDIO)) {
            return "3";
        }
        if (StringUtils.regexMatch(ext.toLowerCase(), StringUtils.PATTERN_DOC)) {
            return "4";
        }
        if (StringUtils.regexMatch(ext.toLowerCase(), StringUtils.PATTERN_ARCHIVE)) {
            return "5";
        }
        return "9";
    }
}
