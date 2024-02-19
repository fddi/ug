package top.ulug.cms.newspaper.domain;


import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;

/**
 * Created by liujf on 2020/10/8.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table(indexes = {@Index(columnList = "dateItem")})
public class PaperHeadline extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long lineId;

    private String type;

    private String dateItem;

    private String layout;

    private String title;

    @Column(unique = true, nullable = false)
    private String link;

    private String note;

    private String style;

    private String status;

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateItem() {
        return dateItem;
    }

    public void setDateItem(String dateItem) {
        this.dateItem = dateItem;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
