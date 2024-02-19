package top.ulug.base.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujf on 2020-03-02.
 * 逝者如斯夫 不舍昼夜
 */
public class NormTreeDTO extends TreeDTO {
    private Long id;
    private String value;
    private String title;
    private Long parentId;
    private List<TreeDTO> children;

    public NormTreeDTO(Long id, String value, String title, Long parentId) {
        this.id = id;
        this.value = value;
        this.title = title;
        this.parentId = parentId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public List<TreeDTO> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<TreeDTO> children) {
        this.children = children;
    }

    /**
     * 添加子节点
     *
     * @param child 子节点
     */
    public void addChild(NormTreeDTO child) {
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
    public void findChild(NormTreeDTO node, List<NormTreeDTO> list) {
        long parentId = node.getId();
        for (NormTreeDTO label : list) {
            if (parentId == label.getParentId()) {
                node.addChild(label);
                findChild(label, list);
            }
        }
    }

}
