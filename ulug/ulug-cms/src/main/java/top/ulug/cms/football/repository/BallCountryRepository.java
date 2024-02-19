package top.ulug.cms.football.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.base.dto.TreeDTO;
import top.ulug.cms.football.domain.BallCountry;

import java.util.List;

/**
 * Created by liujf on 2020/10/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallCountryRepository extends JpaRepository<BallCountry, Long> {

    BallCountry findByCountryName(String countryName);

    BallCountry findByEnName(String enName);

    @Query("from BallCountry t where t.countryName like %?1% order by enName ")
    Page<BallCountry> page(String name, Pageable pageable);

    @Query("select new top.ulug.base.dto.TreeDTO(countryId,0L,concat(countryId,''),concat(countryId,''),countryName,countryName) from BallCountry t where t.status='1' ")
    List<TreeDTO> findTree();
}
