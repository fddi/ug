package top.ulug.core.auth.dto;

import top.ulug.base.dto.TreeDTO;

import java.util.List;

/**
 * Created by liujf on 2021/7/21.
 * 逝者如斯夫 不舍昼夜
 */
public class RoleOrgDTO {
    private List<TreeDTO> unitList;
    private TreeDTO org;

    public List<TreeDTO> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<TreeDTO> unitList) {
        this.unitList = unitList;
    }

    public TreeDTO getOrg() {
        return org;
    }

    public void setOrg(TreeDTO org) {
        this.org = org;
    }
}
