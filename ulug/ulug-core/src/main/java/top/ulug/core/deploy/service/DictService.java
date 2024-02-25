package top.ulug.core.deploy.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.deploy.domain.CodeDict;
import top.ulug.base.inf.CurdService;

import java.util.List;

/**
 * Created by liujf on 2019/4/10.
 * 逝者如斯夫 不舍昼夜
 */
public interface DictService extends CurdService<CodeDict, CodeDict> {

    /**
     * 查询目录所属字典数据，及子节点数据
     *
     * @param catalog  目录编码
     * @param dictCode 字典编码
     * @return list
     */
    WrapperDTO<List<LabelDTO>> listCatalogDict(String catalog, String dictCode);

    /**
     * 查询目录所属字典数据，及子节点数据
     *
     * @param catalog  目录编码
     * @param dictCode 字典编码
     * @param dataType 返回数据结构
     * @return dto-tree
     */
    WrapperDTO<TreeDTO> findCatalogDict(String catalog, String dictCode, String dataType);

    /**
     * 导入数据
     *
     * @param file     EXCEL
     * @param dataType 上传数据类型
     * @return RESULT
     * @throws Exception exp
     */
    WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception;
}
