package top.ulug.core.deploy.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

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

    @Column(unique = true, nullable = false, length = 50)
    private String catalogCode;

    @Column(nullable = false)
    private String catalogName;

    @Column(length = 50)
    private String pinyin;

    private String catalogNote;

    private Integer catalogSort;

    @Column(nullable = false)
    private Long parentId;

    private String catalogPath;

    @Column(length = 50)
    private String icon;

    private String extraData;

    private String ED001;

    private String ED002;

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
