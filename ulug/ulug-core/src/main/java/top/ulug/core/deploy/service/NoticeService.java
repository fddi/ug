package top.ulug.core.deploy.service;

import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.DeployNotice;
import top.ulug.jpa.CurdService;
import top.ulug.jpa.dto.PageDTO;

/**
 * Created by liujf on 2022/7/27.
 * 逝者如斯夫 不舍昼夜
 */
public interface NoticeService extends CurdService<DeployNotice, DeployNotice> {

    WrapperDTO<PageDTO<DeployNotice>> pagePublic(int pageSize, int pageNo);
}
