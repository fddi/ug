package top.ulug.base.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujf on 2019/3/25.
 * 逝者如斯夫 不舍昼夜
 */
public class TreeDTO implements Serializable {
    private Long id;
    private Long parentId;
    private String key;
    private String value;
    private String title;
    private String label;
    private Integer sort;
    private String icon;
    private String note;
    private List<TreeDTO> children;
    private String status;
    private boolean checked;
    private String type;

    public TreeDTO() {
    }

    public TreeDTO(Long id, Long parentId, String key, String value, String title, String label) {
        this.id = id;
        this.parentId = parentId;
        this.key = key;
        this.value = value;
        this.title = title;
        this.label = label;
    }

    public TreeDTO(Long id, Long parentId, String key, String value, String title, String label, Integer sort, String icon, String note, String status, boolean checked, String type) {
        this.id = id;
        this.parentId = parentId;
        this.key = key;
        this.value = value;
        this.title = title;
        this.label = label;
        this.sort = sort;
        this.icon = icon;
        this.note = note;
        this.status = status;
        this.checked = checked;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<TreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreeDTO> children) {
        this.children = children;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    /**
     * 添加子节点
     *
     * @param child 子节点
     */
    public void addChild(TreeDTO child) {
        if (child == null) return;
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    /**
     * 递归查找所有子节点
     *
     * @param node 起始节点
     * @param list 数据集
     */
    public void findChild(TreeDTO node, List<TreeDTO> list) {
        long parentId = node.getId();
        for (TreeDTO label : list) {
            if (parentId == label.getParentId()) {
                node.addChild(label);
                findChild(label, list);
            }
        }
    }

}
