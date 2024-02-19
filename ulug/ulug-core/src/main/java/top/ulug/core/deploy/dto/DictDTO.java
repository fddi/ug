package top.ulug.core.deploy.dto;


import top.ulug.core.deploy.domain.CodeDict;

import java.io.Serializable;

/**
 * Created by liujf on 2019/10/10.
 * 逝者如斯夫 不舍昼夜
 */
public class DictDTO implements Serializable {
    private String dictCode;
    private String dictName;
    private String parentCode;
    private String catalog;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public CodeDict convert() {
        CodeDict dict = new CodeDict();
        dict.setParentId(0L);
        dict.setDictName(this.dictName);
        dict.setDictCode(this.dictCode);
        dict.setCatalog(this.catalog);
        dict.setParentCode(this.parentCode);
        return dict;
    }
}
