package top.ulug.cms.football.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.jpa.CurdService;

public interface BallCountryService extends CurdService<BallCountry, BallCountry> {
    WrapperDTO<String> upload(BallCountry country, MultipartFile file);

    /**
     * 国家树
     *
     * @return tree
     */
    WrapperDTO<TreeDTO> countryExtra();
}
