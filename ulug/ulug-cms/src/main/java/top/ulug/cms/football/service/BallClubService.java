package top.ulug.cms.football.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.football.domain.BallClub;
import top.ulug.jpa.CurdService;

public interface BallClubService extends CurdService<BallClub, BallClub> {
    WrapperDTO<String> upload(BallClub club, MultipartFile file);
}
