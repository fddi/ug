package top.ulug.cms.football.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.ulug.cms.football.domain.BallEvent;

/**
 * Created by liujf on 2020/10/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallEventRepository extends JpaRepository<BallEvent, Long> {
}
