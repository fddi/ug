package top.ulug.cms.media.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.util.DateTimeUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.cms.media.domain.MediaNetFile;
import top.ulug.cms.media.domain.MediaNetFileShare;
import top.ulug.cms.media.repository.MediaNetFileRepository;
import top.ulug.cms.media.repository.MediaNetFileShareRepository;
import top.ulug.cms.media.service.NetFileShareService;
import top.ulug.core.api.service.OvService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020-06-19.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class NetFileShareServiceImpl implements NetFileShareService {
    @Autowired
    MediaNetFileRepository fileRepository;
    @Autowired
    OvService ovService;
    @Autowired
    MediaNetFileShareRepository shareRepository;
    @Value("${spring.application.name}")
    String appName;

    @Override
    public WrapperDTO<MediaNetFileShare> shareControl(Long fileId, MediaNetFileShare shareInfo) {
        Optional<MediaNetFile> opl = fileRepository.findById(fileId);
        if (!opl.isPresent()) {
            return WrapperDTO.npe("file");
        }
        MediaNetFile file = opl.get();
        String status = shareInfo.getStatus();
        if ("1".equals(status)) {
            try {
                String code = StringUtils.getStringRandom(4).toUpperCase();
                String key = StringUtils.createFileKey(appName, String.valueOf(fileId));
                String url = ovService.getOv("sys_source_url").getResultData();
                String dlUrl = url + "/ug/file/a/" + key;
                String shareUrl = url + "/fs/" + key;
                shareInfo.setShareUri(dlUrl);
                shareInfo.setShareKey(key);
                if ("1".equals(shareInfo.getShareType())) {
                    shareInfo.setShareUri(shareUrl);
                    shareInfo.setShareCode(code);
                }
                shareInfo.setFile(file);
                shareRepository.save(shareInfo);
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(e.getMessage());
            }
        } else if (shareInfo.getShareId() != null) {
            //共享状态关闭，则删除共享链接
            shareRepository.delete(shareInfo);
        }
        return WrapperDTO.success(shareInfo);
    }

    @Override
    public WrapperDTO<List<MediaNetFileShare>> shareList(Long fileId) {
        List<MediaNetFileShare> list = shareRepository.findByFileId(fileId);
        return WrapperDTO.success(list);
    }

    @Override
    public ResponseEntity<byte[]> dl(String key, String code) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        MediaNetFileShare share = shareRepository.findByShareKey(key);
        if (share == null) {
            return null;
        }
        Integer shareDays = share.getShareDays();
        if (shareDays != null && shareDays > 0) {
            //有日期限制
            long c = DateTimeUtils.countDays(share.getGmtCreate(), new Date());
            if (c > shareDays) {
                return null;
            }
        }
        if ("1".equals(share.getShareType())) {
            //需要提取码
            if (code == null || !code.equals(share.getShareCode())) {
                return null;
            }
        }
        Optional<MediaNetFile> opFile = fileRepository.findById(share.getFileId());
        if (!opFile.isPresent()) {
            return null;
        }
        String path = opFile.get().getUri();
        File file = new File(path);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = fis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(opFile.get().getFileName(), "utf-8"));
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> response = new ResponseEntity<>(bos.toByteArray(), headers, statusCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
