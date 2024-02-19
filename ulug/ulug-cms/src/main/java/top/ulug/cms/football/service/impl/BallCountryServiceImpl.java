package top.ulug.cms.football.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.football.domain.BallCountry;
import top.ulug.cms.football.repository.BallCountryRepository;
import top.ulug.cms.football.service.BallCountryService;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.service.MediaService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author liu
 * @Date 2022/9/5 21:51 星期一
 */
@Service
public class BallCountryServiceImpl implements BallCountryService {

    @Autowired
    BallCountryRepository countryRepository;

    @Autowired
    MediaService mediaService;

    @Override
    public WrapperDTO<String> save(BallCountry... e) throws Exception {
        List<BallCountry> list = Arrays.asList(e);
        countryRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(BallCountry... e) {
        List<BallCountry> list = Arrays.asList(e);
        countryRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<BallCountry> findOne(Long id) {
        Optional<BallCountry> optional = countryRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("BallCountry");
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<BallCountry>> findPage(int pageSize, int pageNo, BallCountry ballCountry) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String name = ballCountry.getCountryName() == null ? "" : ballCountry.getCountryName();
        Page<BallCountry> page = countryRepository.page(name, pageable);
        return WrapperDTO.success(new PageDTO<BallCountry>().convert(page));
    }

    @Override
    public WrapperDTO<String> upload(BallCountry country, MultipartFile file) {
        if (file != null) {
            MediaImage image = new MediaImage();
            image.setImgTags(country.getCountryName());
            if (country.getFlag() != null) {
                mediaService.delImage(country.getFlag());
            }
            try {
                String imageKey = mediaService.saveImage(image, file);
                country.setFlag(imageKey);
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(e.getMessage());
            }
        }
        countryRepository.save(country);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<TreeDTO> countryExtra() {
        TreeDTO root = new TreeDTO(0L, 0L, "0", "0", "根目录", "根目录");
        List<TreeDTO> children = countryRepository.findTree();
        root.setChildren(children);
        return WrapperDTO.success(root);
    }
}
