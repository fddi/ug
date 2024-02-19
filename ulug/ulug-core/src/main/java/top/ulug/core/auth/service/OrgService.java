package top.ulug.core.auth.service;

import org.springframework.web.multipart.MultipartFile;
import top.ulug.base.dto.LabelDTO;
import top.ulug.base.dto.TreeDTO;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.auth.domain.AuthOrg;
import top.ulug.jpa.CurdService;

import java.util.List;

/**
 * Created by liujf on 2019/4/2.
 * 逝者如斯夫 不舍昼夜
 */
public interface OrgService extends CurdService<AuthOrg, AuthOrg> {

    /**
     * 返回树结构的机构，parentId=0时：开发账号查询全部，否则查询本单位机构
     * parenId>0时，返回本单位父id所有子机构
     *
     * @param areaCode 区域
     * @param parentId 父级ID
     * @return dto
     */
    WrapperDTO<TreeDTO> findChildrenOrg(String areaCode, Long parentId);

    /**
     * 返回机构集合 开发账号查询全部，否则查询本单位机构
     *
     * @param areaCode 区划
     * @return list
     */
    List<AuthOrg> listOrg(String areaCode);

    /**
     * 查找机构
     *
     * @param searchKey 关键字，关联编码、名称
     * @param orgType   机构类型
     * @return list
     */
    WrapperDTO<List<LabelDTO>> search(String searchKey, String orgType);

    /**
     * 导入数据
     *
     * @param file     EXCEL
     * @param dataType 数据类型
     * @return result
     * @throws Exception exp
     */
    WrapperDTO<String> impByExcel(MultipartFile file, String dataType) throws Exception;

    /**
     * 机构层级变动
     *
     * @param dragId 机构Id
     * @param dropId 目标Id
     * @return dto
     */
    WrapperDTO<String> dragDrop(Long dragId, Long dropId);
}