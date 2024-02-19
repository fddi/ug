package top.ulug.core.deploy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.deploy.domain.DeployOv;

import java.util.List;


/**
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */

public interface DeployOvRepository extends JpaRepository<DeployOv, Long> {

    DeployOv findByUnitCodeAndOptionCode(String unitCode, String optionCode);

    List<DeployOv> findByOptionCode(String optionCode);

    @Query("from DeployOv where optionCode=?1 and (unitCode='Y' or unitCode=?2)")
    List<DeployOv> findValues(String optionCode, String unitCode);
}
