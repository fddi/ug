package top.ulug.core.deploy.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import top.ulug.core.deploy.domain.MultiMessage;

/**
 * @Author liu
 * @Date 2024/5/10 下午5:32 星期五
 */
public interface MultiMessageRepository extends JpaRepository<MultiMessage, Long> {

    Page<MultiMessage> findByTitleLike(String title, Pageable pageable);
}
