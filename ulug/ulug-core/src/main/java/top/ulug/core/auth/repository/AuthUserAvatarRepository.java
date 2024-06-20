package top.ulug.core.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.ulug.core.auth.domain.AuthUser;
import top.ulug.core.auth.domain.AuthUserAvatar;

/**
 * @Author liu
 * @Date 2024/6/18 下午10:21 星期二
 */
public interface AuthUserAvatarRepository extends JpaRepository<AuthUserAvatar, Long>,
        JpaSpecificationExecutor<AuthUserAvatar> {

    AuthUserAvatar findByUserName(String userName);
}
