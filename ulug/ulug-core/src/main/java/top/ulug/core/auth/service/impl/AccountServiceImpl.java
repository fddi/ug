package top.ulug.core.auth.service.impl;

import com.alibaba.excel.EasyExcel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.AccountDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.security.AES;
import top.ulug.base.security.Base64;
import top.ulug.base.security.Digest;
import top.ulug.base.servlet.RequestUtils;
import top.ulug.base.util.JwtUtils;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthOrg;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.dto.AuthDTO;
import top.ulug.core.auth.dto.UserDTO;
import top.ulug.core.auth.dto.UserImpDTO;
import top.ulug.core.auth.event.LoginEvent;
import top.ulug.core.auth.event.LogoutEvent;
import top.ulug.core.auth.repository.AuthOrgRepository;
import top.ulug.core.auth.repository.AuthUserRepository;
import top.ulug.core.auth.service.AccountService;
import top.ulug.core.auth.service.AuthService;
import top.ulug.jpa.dto.PageDTO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by liujf on 2019/3/25.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final String TAG_TIMES_LOGIN = "TimesLogin";
    private static final String TAG_TOKEN = "Token";
    private static final String SALT = "-a1b2";
    @Value("${spring.application.name}")
    private String appName;
    @Value("${project.auth.error.times}")
    private Long errorTimes;
    @Value("${project.auth.active.time}")
    private Long activeTime;
    @Value("${project.auth.default.password}")
    private String defaultPassword;
    @Value("${project.auth.aes.key}")
    private String key;
    @Value("${project.auth.developer.code}")
    private String developerCode;
    @Autowired
    private ApplicationContext publisher;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Long> redisVoTimes;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, AuthDTO> redisVoAuth;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, List<String>> redisVoTokens;

    @Autowired
    private AuthUserRepository userRepository;
    @Autowired
    private AuthOrgRepository orgRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    RequestUtils requestUtils;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<AccountDTO> login(String userName, String password) {
        if (!StringUtils.regexMatch(userName, StringUtils.PATTERN_LOGIN_ID)) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_LOGIN_FAIL, null);
        }
        try {
            String tagLoginTimes = StringUtils.getCacheKey(appName, userName, TAG_TIMES_LOGIN);
            long times = redisVoTimes.get(tagLoginTimes) == null ? 0L : redisVoTimes.get(tagLoginTimes);
            if (times >= errorTimes) {
                //登录错误次数过多
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_AUTH_TIMEOUT, null);
            }
            AuthUser user = userRepository.findByUserName(userName);
            if (user == null) {
                //无用户
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_LOGIN_FAIL, null);
            }
            if (!"1".equals(user.getStatus()) || !"1".equals(user.getOrg().getStatus())) {
                //无效用户或在无效机构下
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_USER_DISABLE, null);
            }
            String test = encodePassword(userName, password, true);
            if (user.getPassword().equals(test)) {
                //密码正确
                long nowDate = new Date().getTime();
                String token = JwtUtils.create(userName, appName);
                AccountDTO ad = getAccountDto(user, token, nowDate);
                AuthDTO authDTO = new AuthDTO();
                authDTO.setAccount(ad);
                redisVoAuth.set(token, authDTO, activeTime, TimeUnit.MINUTES);
                String tagTokens = StringUtils.getCacheKey(appName, userName, TAG_TOKEN);
                List<String> tokens = redisVoTokens.get(tagTokens);
                if (tokens == null) {
                    tokens = new ArrayList<>();
                }
                tokens.add(token);
                redisVoTokens.set(tagTokens, tokens);
                publisher.publishEvent(new LoginEvent(this, ad));
                return WrapperDTO.success(ad);
            }
            redisVoTimes.set(tagLoginTimes, ++times, activeTime, TimeUnit.MINUTES);
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_LOGIN_FAIL, null);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapperDTO.fail(ResultMsgEnum.RESULT_EXCEPTION, null);
        }
    }

    @Override
    public boolean logout() {
        String token = requestUtils.getCurrentToken();
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        AuthDTO authDTO = redisVoAuth.get(token);
        if (authDTO == null || authDTO.getAccount() == null) {
            return false;
        }
        redisVoAuth.getOperations().delete(token);
        publisher.publishEvent(new LogoutEvent(this, token));
        return true;
    }

    @Override
    public WrapperDTO<String> resetPassword(Long userId) {
        Optional<AuthUser> op = userRepository.findById(userId);
        if (!op.isPresent()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, ":user");
        }
        AuthUser authUser = op.get();
        String newPwd = StringUtils.getStringRandom(6);
        authUser.setPassword(encodePassword(authUser.getUserName(), newPwd, false));
        userRepository.save(authUser);
        return WrapperDTO.success(newPwd);
    }

    @Override
    public WrapperDTO<String> changePassword(String userName, String pwd, String npwd, String npwd2) throws Exception {
        AuthUser user = userRepository.findByUserName(userName);
        if (user == null) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, null);
        }
        String tagLoginTimes = StringUtils.getCacheKey(appName, userName, TAG_TIMES_LOGIN);
        Long times = redisVoTimes.get(tagLoginTimes) == null ? 0L : redisVoTimes.get(tagLoginTimes);
        if (times >= errorTimes) {
            //登录错误次数过多
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_AUTH_TIMEOUT, null);
        }
        if (!user.getPassword().equals(encodePassword(user.getUserName(), pwd, true))) {
            redisVoTimes.set(tagLoginTimes, ++times, activeTime, TimeUnit.MINUTES);
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_LOGIN_FAIL, null);
        }
        if (StringUtils.isEmpty(npwd) || !npwd.equals(npwd2)) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR, null);
        }
        user.setPassword(encodePassword(user.getUserName(), npwd, true));
        userRepository.save(user);
        return WrapperDTO.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception {
        //同步读取,自动finish
        List<Object> list = EasyExcel.read(file.getInputStream())
                .head(UserImpDTO.class).sheet().doReadSync();
        List<AuthUser> saveList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            UserImpDTO data = (UserImpDTO) list.get(i);
            if (StringUtils.isEmpty(data.getOrgCode()) || StringUtils.isEmpty(data.getUserName())
                    || StringUtils.isEmpty(data.getNickName())) {
                throw new Exception(String.format("第%d行出错：必填项存在空值，请修改！", i + 1));
            }
            AuthOrg opOrg = orgRepository.findByOrgCode(data.getOrgCode());
            if (opOrg == null) {
                throw new Exception(String.format("第%d行出错：机构代码不存在，请修改！", i + 1));
            }
            AuthUser user = modelMapper.map(data, AuthUser.class);
            user.setOrgId(opOrg.getOrgId());
            user.setStatus("1");
            saveList.add(user);
        }
        return this.save(saveList.toArray(new AuthUser[saveList.size()]));
    }

    @Override
    public WrapperDTO<String> save(AuthUser... users) {
        List<AuthUser> list = new ArrayList<>();
        for (AuthUser user : users) {
            Optional<AuthOrg> op = orgRepository.findById(user.getOrgId());
            if (!op.isPresent()) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
            }
            //禁止保存开发者类型
            String userType = developerCode.equals(user.getUserType()) ? null : user.getUserType();
            user.setUserType(userType);
            user.setAreaCode(op.get().getAreaCode());
            user.setUnitCode(op.get().getUnitCode());
            user.setOrg(op.get());
            if (user.getUserId() != null && user.getUserId() > 0) {
                AuthUser old = userRepository.findById(user.getUserId()).get();
                //账号密码禁止更新
                user.setUserName(old.getUserName());
                user.setPassword(old.getPassword());
                if (!"1".equals(user.getStatus())) {
                    cleanAuthCache(user.getUserName());
                }
            } else {
                user.setPassword(encodePassword(user.getUserName(), defaultPassword, false));
            }
            list.add(user);
        }
        userRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(AuthUser... users) {
        //禁止删除用户，只能禁用用户
        List<Long> ids = new ArrayList<>();
        for (AuthUser user : users) {
            ids.add(user.getUserId());
            cleanAuthCache(user.getUserName());
        }
        userRepository.forbidden(ids);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<UserDTO> findOne(Long id) {
        AuthUser user = userRepository.findById(id).get();
        modelMapper.typeMap(AuthUser.class, UserDTO.class).addMappings(mapper -> {
            mapper.map(src -> src.getOrg().getOrgCode(),
                    UserDTO::setOrgCode);
        });
        UserDTO data = modelMapper.map(user, UserDTO.class);
        return WrapperDTO.success(data);
    }

    @Override
    public WrapperDTO<PageDTO<UserDTO>> findPage(int pageSize, int pageNo, AuthUser user) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String status = StringUtils.isEmpty(user.getStatus()) ? "1" : user.getStatus();
        Optional<AuthOrg> op = Optional.empty();
        if (user.getOrgId() != null) {
            op = orgRepository.findById(user.getOrgId());
        }
        String userName = user.getUserName() == null ? "" : user.getUserName();
        Page<UserDTO> page = userRepository.page(
                userName, op.get().getOrgPath(), status, pageable);
        PageDTO<UserDTO> pageDTO = new PageDTO<>();
        return WrapperDTO.success(pageDTO.convert(page));
    }

    /**
     * 组合登录对象用于返回
     *
     * @param user  user
     * @param token 令牌
     * @param time  登录时间戳
     * @return dto
     */
    private AccountDTO getAccountDto(AuthUser user, String token, Long time) {
        AccountDTO ad = new AccountDTO();
        ad.setUserName(user.getUserName());
        ad.setUserType(user.getUserType());
        ad.setNickName(user.getNickName());
        ad.setAreaCode(user.getAreaCode());
        ad.setUnitCode(user.getUnitCode());
        ad.setStatus(user.getStatus());
        ad.setOrgId(user.getOrgId());
        ad.setOrgName(user.getOrg().getOrgName());
        ad.setToken(token);
        ad.setAuthTime(time);
        return ad;
    }

    /**
     * 加盐SHA256不可逆加密，BASE64编码保存
     *
     * @param userName  用户名
     * @param password  密码
     * @param isEncrypt 密码是否AES加密传输
     * @return 加密密码
     */
    private String encodePassword(String userName, String password, Boolean isEncrypt) {
        try {
            if (isEncrypt) {
                password = AES.decrypt(password, key, AES.IV);
            }
            password = password.replaceAll("\\s", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String pwdHash = userName + password + SALT;
        pwdHash = Base64.encodeBytes(
                Digest.SHA256Encrypt(pwdHash).getBytes()).toUpperCase();
        return pwdHash;
    }

    public static void main(String[] args){
       String pwdHash = Base64.encodeBytes(
                Digest.SHA256Encrypt("developer111111"+ SALT).getBytes()).toUpperCase();
       System.out.println(pwdHash);
    }

    /**
     * 清除认证缓存
     *
     * @param userName 用户名
     */
    private void cleanAuthCache(String userName) {
        String tagTokens = StringUtils.getCacheKey(appName, userName, TAG_TOKEN);
        List<String> tokens = redisVoTokens.get(tagTokens);
        if (tokens != null) {
            for (String token : tokens) {
                redisVoAuth.getOperations().delete(token);
            }
            redisVoAuth.getOperations().delete(tagTokens);
        }
    }
}
