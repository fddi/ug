package top.ulug.core.deploy.domain;


import top.ulug.base.auditor.BaseEntity;

import jakarta.persistence.*;

/**
 * 数据字典
 * Created by liujf on 2019/4/10.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table(indexes = {@Index(columnList = "catalog")},
        uniqueConstraints = {@UniqueConstraint(columnNames = {"catalog", "dictCode"})})
public class CodeDict extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long dictId;

    @Column(nullable = false,length = 50)
    private String catalog;

    @Column(nullable = false,length = 50)
    private String dictCode;

    @Column(nullable = false)
    private String dictName;

    @Column(length = 50)
    private String pinyin;

    @Column(length = 50)
    private String zhuji;

    private String dictNote;

    private Integer dictSort;

    @Column(nullable = false)
    private Long parentId;

    @Column(length = 50)
    private String parentCode;

    private String dictPath;

    @Column(length = 50)
    private String icon;

    @Column(length = 50)
    private String extraData;

    private String ED001;

    private String ED002;

    private String ED003;

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public Integer getDictSort() {
        return dictSort;
    }

    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    public String getDictNote() {
        return dictNote;
    }

    public void setDictNote(String dictNote) {
        this.dictNote = dictNote;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
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

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getZhuji() {
        return zhuji;
    }

    public void setZhuji(String zhuji) {
        this.zhuji = zhuji;
    }

    public String getDictPath() {
        return dictPath;
    }

    public void setDictPath(String dictPath) {
        this.dictPath = dictPath;
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
