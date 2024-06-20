package top.ulug.core.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.security.Base64;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthUserAvatar;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.repository.AuthUserAvatarRepository;
import top.ulug.core.auth.service.AvatarService;
import top.ulug.core.deploy.service.CacheService;

/**
 * @Author liu
 * @Date 2024/6/18 下午10:32 星期二
 */
@Service
public class AvatarServiceImpl implements AvatarService {
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private AuthUserAvatarRepository avatarRepository;

    @Override
    public WrapperDTO<String> upload(MultipartFile file) throws Exception {
        String appId = requestUtils.getCurrentAppId();
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "token");
        }
        AuthDTO authDTO = cacheService.getAuth(appId, token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "token");
        }
        AuthUserAvatar avatar = avatarRepository.findByUserName(authDTO.getAccount().getUserName());
        if (avatar == null) {
            avatar = new AuthUserAvatar();
            avatar.setUserName(authDTO.getAccount().getUserName());
        }
        String filename = file.getOriginalFilename();
        assert filename != null;
        if (!StringUtils.regexMatch(filename.toLowerCase(), StringUtils.PATTERN_IMG)) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, "不是系统支持的图片格式");
        }
        String extension = filename.substring(filename.lastIndexOf("."));
        avatar.setFileType(extension);
        String encodeData = Base64.encodeBytes(file.getBytes());
        avatar.setAvatarData(encodeData);
        avatarRepository.save(avatar);
        return WrapperDTO.success();
    }

    @Override
    public ResponseEntity<byte[]> read(String userName) {
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        AuthUserAvatar avatar = avatarRepository.findByUserName(userName);
        String fileName = userName + avatar.getFileType();
        try {
            byte[] bytes = Base64.decode(avatar.getAvatarData());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            HttpStatus statusCode = HttpStatus.OK;
            ResponseEntity<byte[]> response = new ResponseEntity<>(bytes, headers, statusCode);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}
