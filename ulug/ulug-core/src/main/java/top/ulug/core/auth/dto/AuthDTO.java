package top.ulug.core.auth.dto;

import com.alibaba.fastjson.JSON;
import top.ulug.base.dto.AccountDTO;
import top.ulug.core.deploy.domain.DeployAbility;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liujf on 2020/5/16.
 * 逝者如斯夫 不舍昼夜
 */
public class AuthDTO implements Serializable {
    private AccountDTO account;
    private List<OrgDTO> orgList;
    private List<DeployAbility> abilityList;

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public List<OrgDTO> getOrgList() {
        return orgList;
    }

    public void setOrgList(List<OrgDTO> orgList) {
        this.orgList = orgList;
    }

    public List<DeployAbility> getAbilityList() {
        return abilityList;
    }

    public void setAbilityList(List<DeployAbility> abilityList) {
        this.abilityList = abilityList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
