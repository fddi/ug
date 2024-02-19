package top.ulug.core.auth.domain;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import top.ulug.core.deploy.domain.DeployAbility;
import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单
 */
@Entity
@Table
public class AuthMenu extends BaseEntity implements Comparable<AuthMenu> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long menuId;

    @Column(columnDefinition = "VARCHAR(2) COMMENT '状态 1--正常 0--异常'")
    private String status;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) COMMENT '客户端名称'")
    private String clientName;

    @Column(columnDefinition = "VARCHAR(128) COMMENT '菜单路径'")
    private String menuUri;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '菜单名'")
    private String menuName;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '菜单类型'")
    private String menuType;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '菜单说明'")
    private String menuNote;

    @Column(columnDefinition = "INT(5) COMMENT '排序'")
    private Integer menuSort;

    @Column(nullable = false, columnDefinition = "INT(10) COMMENT '父级id'")
    private Long parentId;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '节点路径'")
    private String menuPath;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '图标'")
    private String icon;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "auth_menu_ability", joinColumns = @JoinColumn(name = "menuId"),
            inverseJoinColumns = @JoinColumn(name = "abilityId"))
    private List<DeployAbility> abilityList = new ArrayList<>();

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getMenuUri() {
        return menuUri;
    }

    public void setMenuUri(String menuUri) {
        this.menuUri = menuUri;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenuNote() {
        return menuNote;
    }

    public void setMenuNote(String menuNote) {
        this.menuNote = menuNote;
    }

    public Integer getMenuSort() {
        return menuSort;
    }

    public void setMenuSort(Integer menuSort) {
        this.menuSort = menuSort;
    }

    public List<DeployAbility> getAbilityList() {
        return abilityList;
    }

    public void setAbilityList(List<DeployAbility> abilityList) {
        this.abilityList = abilityList;
    }

    @Override
    public int compareTo(AuthMenu menu) {
        return this.getMenuSort().compareTo(menu.getMenuSort());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AuthMenu) {
            Long id = this.menuId;
            if (id == null || id == 0L) {
                return false;
            }
            return id.equals(((AuthMenu) obj).getMenuId());
        }
        return false;
    }
}