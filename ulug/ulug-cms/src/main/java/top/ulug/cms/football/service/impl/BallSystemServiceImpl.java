package top.ulug.cms.football.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.football.domain.BallSystem;
import top.ulug.cms.football.repository.BallSystemRepository;
import top.ulug.cms.football.service.BallSystemService;
import top.ulug.cms.media.domain.MediaImage;
import top.ulug.cms.media.service.MediaService;
import top.ulug.jpa.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2020/11/1.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class BallSystemServiceImpl implements BallSystemService {
    @Autowired
    BallSystemRepository systemRepository;
    @Autowired
    MediaService mediaService;

    @Override
    public WrapperDTO<String> save(BallSystem... ballSystems) {
        List<BallSystem> list = Arrays.asList(ballSystems);
        systemRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(BallSystem... ballSystems) {
        List<BallSystem> list = Arrays.asList(ballSystems);
        for (BallSystem app : list) {
            Optional<BallSystem> opl = systemRepository.findById(app.getSystemId());
            opl.ifPresent(ballSystem -> mediaService.delImage(ballSystem.getLogo()));
        }
        systemRepository.deleteAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<BallSystem> findOne(Long id) {
        Optional<BallSystem> optional = systemRepository.findById(id);
        if (optional.isEmpty()) {
            return WrapperDTO.npe("BallSystem");
        }
        return WrapperDTO.success(optional.get());
    }

    @Override
    public WrapperDTO<PageDTO<BallSystem>> findPage(int pageSize, int pageNo, BallSystem ballSystem) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String name = ballSystem.getSystemName() == null ? "" : ballSystem.getSystemName();
        Page<BallSystem> page = systemRepository.page(
                name, pageable);
        return WrapperDTO.success(new PageDTO<BallSystem>().convert(page));
    }

    @Override
    public WrapperDTO<String> upload(BallSystem system, MultipartFile file) {
        if (file != null) {
            MediaImage image = new MediaImage();
            image.setImgTags(system.getSystemName());
            if (system.getLogo() != null) {
                mediaService.delImage(system.getLogo());
            }
            try {
                String imageKey = mediaService.saveImage(image, file);
                system.setLogo(imageKey);
            } catch (Exception e) {
                e.printStackTrace();
                return WrapperDTO.fail(e.getMessage());
            }
        }
        systemRepository.save(system);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<TreeDTO> systemExtra() {
        TreeDTO root=new TreeDTO(0L, 0L, "0","0","根目录", "根目录");
        List<TreeDTO> children = systemRepository.findTree();
        root.setChildren(children);
        return  WrapperDTO.success(root);
    }
}
