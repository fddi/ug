package top.ulug.core.deploy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.util.StringUtils;
import top.ulug.core.deploy.domain.CodeCatalog;
import top.ulug.core.deploy.repository.CodeCatalogRepository;
import top.ulug.core.deploy.service.CatalogService;
import top.ulug.base.dto.PageDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2019/10/7.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    CodeCatalogRepository catalogRepository;

    @Override
    public List<CodeCatalog> listChildrenCatalog(String catalogCode) {
        if (StringUtils.isEmpty(catalogCode)) {
            return null;
        }
        CodeCatalog catalog = catalogRepository.findByCatalogCode(catalogCode);
        if (catalog == null) {
            return null;
        }
        return catalogRepository.findByCatalogPath(catalog.getCatalogPath());
    }

    @Override
    public WrapperDTO<TreeDTO> findChildrenCatalog(String catalogCode) {
        catalogCode = StringUtils.isEmpty(catalogCode) ? "0" : catalogCode;
        TreeDTO root;
        String path = "";
        if ("0".equalsIgnoreCase(catalogCode)) {
            root = new TreeDTO(0L, 0L, "/", "/", "根目录", "根目录",
                    -1, "", "", "", false, "");
        } else {
            CodeCatalog catalog = catalogRepository.findByCatalogCode(catalogCode);
            if (catalog == null) {
                return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "CodeCatalog");
            }
            root = new TreeDTO(catalog.getCatalogId(), catalog.getParentId(), catalog.getCatalogCode(),
                    catalog.getCatalogCode(), catalog.getCatalogName(), catalog.getCatalogName(),
                    catalog.getCatalogSort(), catalog.getIcon(), "", "1", false, "");
            path = catalog.getCatalogPath();
        }
        List<CodeCatalog> list = catalogRepository.findByCatalogPath(path);
        List<TreeDTO> treeDTOList = new ArrayList<>();
        for (CodeCatalog item : list) {
            TreeDTO child = new TreeDTO(item.getCatalogId(), item.getParentId(), item.getCatalogCode(),
                    item.getCatalogCode(), item.getCatalogName(), item.getCatalogName(),
                    item.getCatalogSort(), item.getIcon(), "", "1", false, "");
            treeDTOList.add(child);
        }
        root.findChild(root, treeDTOList);
        return WrapperDTO.success(root);
    }

    @Override
    public WrapperDTO<String> save(CodeCatalog... catalogs) {
        //保存路径
        List<CodeCatalog> list = Arrays.asList(catalogs);
        List<CodeCatalog> saveList = catalogRepository.saveAll(list);
        for (CodeCatalog catalog : saveList) {
            String path = "";
            if (catalog.getParentId() != null && catalog.getParentId() > 0) {
                path = catalogRepository.findPath(catalog.getParentId()) + ">";
            }
            path += catalog.getCatalogId();
            catalog.setCatalogPath(path);
        }
        catalogRepository.saveAll(saveList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(CodeCatalog... catalogs) {
        List<CodeCatalog> list = Arrays.asList(catalogs);
        //删除下属所有数据
        List<CodeCatalog> delList = new ArrayList<>();
        for (CodeCatalog catalog : list) {
            String path = catalogRepository.findPath(catalog.getCatalogId());
            List<CodeCatalog> childList = catalogRepository.findByCatalogPath(path + ">");
            delList.addAll(childList);
        }
        delList.addAll(list);
        catalogRepository.deleteAllInBatch(delList);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<CodeCatalog> findOne(Long id) {
        Optional<CodeCatalog> optional = catalogRepository.findById(id);
        return optional.map(WrapperDTO::success).orElseGet(
                () -> WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "CodeCatalog"));
    }

    @Override
    public WrapperDTO<PageDTO<CodeCatalog>> findPage(int pageSize, int pageNo, CodeCatalog catalog) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CodeCatalog> page = catalogRepository.findAll(pageable);
        return WrapperDTO.success(new PageDTO<CodeCatalog>().convert(page));
    }

}
