package top.ulug.core.deploy.service;

import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.CodeCatalog;
import top.ulug.base.inf.CurdService;

import java.util.List;

/**
 * Created by liujf on 2019/4/10.
 * 逝者如斯夫 不舍昼夜
 */
public interface CatalogService extends CurdService<CodeCatalog, CodeCatalog> {

    /**
     * 查询目录及下属目录数据
     *
     * @param catalogCode 目录编码
     * @return list
     */
    List<CodeCatalog> listChildrenCatalog(String catalogCode);

    /**
     * 查询目录及下属目录树结构数据
     *
     * @param catalogCode 目录编码
     * @return dto-tree
     */
    WrapperDTO<TreeDTO> findChildrenCatalog(String catalogCode);
}
