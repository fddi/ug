package top.ulug.core.deploy.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

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

    @Column(nullable = false, columnDefinition = "VARCHAR(50) COMMENT '目录码'")
    private String catalog;

    @Column(nullable = false, columnDefinition = "VARCHAR(50) COMMENT '编码'")
    private String dictCode;

    @Column(nullable = false, columnDefinition = "VARCHAR(150) COMMENT '编码名称'")
    private String dictName;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '拼音'")
    private String pinyin;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '助记码'")
    private String zhuji;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '编码说明'")
    private String dictNote;

    @Column(columnDefinition = "INT(5) COMMENT '排序'")
    private Integer dictSort;

    @Column(nullable = false, columnDefinition = "INT(10) COMMENT '父级id'")
    private Long parentId;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '父级代码'")
    private String parentCode;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '路径'")
    private String dictPath;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '图标'")
    private String icon;

    @Column(columnDefinition = "VARCHAR(50) COMMENT '附加值'")
    private String extraData;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '附加值1'")
    private String ED001;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '附加值2'")
    private String ED002;

    @Column(columnDefinition = "VARCHAR(150) COMMENT '附加值3'")
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
