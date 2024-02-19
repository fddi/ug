package top.ulug.cms.football.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.cms.football.domain.BallClub;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.repository.BallClubRepository;
import top.ulug.cms.football.repository.BallCountryRepository;
import top.ulug.cms.football.service.BallClubService;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.service.MediaService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author liu
 * @Date 2022/9/5 21:50 星期一
 */
@Service
public class BallClubServiceImpl implements BallClubService {
    @Autowired
    BallClubRepository clubRepository;
    @Autowired
    BallCountryRepository countryRepository;

    @Autowired
    MediaService mediaService;

    @Override
    public WrapperDTO<String> save(BallClub... e) throws Exception {
        List<BallClub> list = Arrays.asList(e);
        for (BallClub club : list) {
            Optional<BallCountry> op = countryRepository.findById(club.getCountryId());
            if (!op.isPresent()) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "BallCountry");
            }
            club.setCountry(op.get());
        }
        clubRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(BallClub... e) {
        List<BallClub> list = Arrays.asList(e);
        clubRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<BallClub> findOne(Long id) {
        Optional<BallClub> optional = clubRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("BallClub");
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<BallClub>> findPage(int pageSize, int pageNo, BallClub ballClub) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String name = ballClub.getClubName() == null ? "" : ballClub.getClubName();
        Page<BallClub> page = clubRepository.page(ballClub.getCountryId(), name, pageable);
        return WrapperDTO.success(new PageDTO<BallClub>().convert(page));
    }

    @Override
    public WrapperDTO<String> upload(BallClub club, MultipartFile file) {
        if (file != null) {
            MediaImage image = new MediaImage();
            image.setImgTags(club.getClubName());
            if (club.getFlag() != null) {
                mediaService.delImage(club.getFlag());
            }
            try {
                String imageKey = mediaService.saveImage(image, file);
                club.setFlag(imageKey);
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(e.getMessage());
            }
        }
        Optional<BallCountry> optional = countryRepository.findById(club.getCountryId());
        if (optional.isEmpty()) {
            return WrapperDTO.npe("BallCountry");
        }
        club.setCountry(optional.get());
        clubRepository.save(club);
        return WrapperDTO.success();
    }
}
