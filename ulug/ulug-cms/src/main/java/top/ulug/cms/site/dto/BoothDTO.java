package top.ulug.cms.site.dto;


import top.ulug.cms.media.dto.ContentDTO;

import java.util.List;

/**
 * Created by liujf on 2020-09-09.
 * 逝者如斯夫 不舍昼夜
 */
public class BoothDTO {
    private String name;
    private String code;
    private List<ContentDTO> recFocus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<ContentDTO> getRecFocus() {
        return recFocus;
    }

    public void setRecFocus(List<ContentDTO> recFocus) {
        this.recFocus = recFocus;
    }

}
