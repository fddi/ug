package top.ulug.core.deploy.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2019/9/29.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table
public class CodeCatalog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long catalogId;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(50) COMMENT '目录编码'")
    private String catalogCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(150) COMMENT '目录名称'")
    private String catalogName;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '拼音'")
    private String pinyin;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '目录说明'")
    private String catalogNote;

    @Column(columnDefinition = "INT(5) COMMENT '排序'")
    private Integer catalogSort;

    @Column(nullable = false,columnDefinition = "INT(10) COMMENT '父级id'")
    private Long parentId;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '路径'")
    private String catalogPath;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '图标'")
    private String icon;

    @Column(columnDefinition = "VARCHAR(512) COMMENT '附加值'")
    private String extraData;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '附加值1'")
    private String ED001;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '附加值2'")
    private String ED002;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '附加值3'")
    private String ED003;

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getCatalogNote() {
        return catalogNote;
    }

    public void setCatalogNote(String catalogNote) {
        this.catalogNote = catalogNote;
    }

    public Integer getCatalogSort() {
        return catalogSort;
    }

    public void setCatalogSort(Integer catalogSort) {
        this.catalogSort = catalogSort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCatalogPath() {
        return catalogPath;
    }

    public void setCatalogPath(String catalogPath) {
        this.catalogPath = catalogPath;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public String getED001() {
        return ED001;
    }

    public void setED001(String ED001) {
        this.ED001 = ED001;
    }

    public String getED002() {
        return ED002;
    }

    public void setED002(String ED002) {
        this.ED002 = ED002;
    }

    public String getED003() {
        return ED003;
    }

    public void setED003(String ED003) {
        this.ED003 = ED003;
    }
}
