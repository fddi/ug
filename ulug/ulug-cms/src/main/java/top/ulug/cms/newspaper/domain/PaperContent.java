package top.ulug.cms.newspaper.domain;

import top.ulug.jpa.auditor.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by liujf on 2022/3/11.
 * 逝者如斯夫 不舍昼夜
 */
@Entity
@Table(indexes = {@Index(columnList = "dateItem")})
public class PaperContent extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paperId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String paper;

    private String link;

    @Column(insertable = false, updatable = false)
    private Long lineId;

    private String dateItem;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "lineId")
    private PaperHeadline headline;

    public Long getPaperId() {
        return paperId;
    }

    public void setPaperId(Long paperId) {
        this.paperId = paperId;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDateItem() {
        return dateItem;
    }

    public void setDateItem(String dateItem) {
        this.dateItem = dateItem;
    }

    public PaperHeadline getHeadline() {
        return headline;
    }

    public void setHeadline(PaperHeadline headline) {
        this.headline = headline;
    }
}
