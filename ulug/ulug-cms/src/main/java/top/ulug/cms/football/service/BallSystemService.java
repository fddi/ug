package top.ulug.cms.football.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.cms.football.domain.BallSystem;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2020/11/1.
 * 逝者如斯夫 不舍昼夜
 */
public interface BallSystemService extends CurdService<BallSystem,BallSystem> {

    /**
     * 信息上传
     *
     * @param system 赛事信息
     * @param file   logo
     * @return result
     */
    WrapperDTO<String> upload(BallSystem system, MultipartFile file);

    WrapperDTO<TreeDTO> systemExtra();
}
