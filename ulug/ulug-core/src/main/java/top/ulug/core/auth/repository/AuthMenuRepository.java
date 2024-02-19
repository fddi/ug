package top.ulug.core.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.auth.domain.AuthMenu;
import top.ulug.core.auth.dto.MenuDTO;

import java.util.List;

/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface AuthMenuRepository extends JpaRepository<AuthMenu, Long>,
        JpaSpecificationExecutor<AuthMenu> {

    @Query("select menuPath from AuthMenu where menuId=?1")
    String findPath(long menuId);

    @Query(" from AuthMenu t where t.menuPath like ?1% ")
    List<AuthMenu> findByMenuPath(String menuPath);

    List<AuthMenu> findByClientNameOrderByParentId(String clientName);

    Page<MenuDTO> findByMenuNameLikeOrMenuUriLike(String menuName, String menuUri, Pageable pageable);

    List<AuthMenu> findByMenuIdIn(List<Long> ids);

}
