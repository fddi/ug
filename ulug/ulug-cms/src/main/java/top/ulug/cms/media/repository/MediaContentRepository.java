package top.ulug.cms.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.media.domain.MediaContent;
import top.ulug.cms.media.dto.ContentDTO;

import java.util.Date;

/**
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaContentRepository extends JpaRepository<MediaContent, Long>,
        JpaSpecificationExecutor<MediaContent> {

    @Query("select new top.ulug.cms.media.dto.ContentDTO(contentId,subject.subjectCode,subject.subjectName,contentType,contentTitle,signer,cover,tags,summary,gmtModified,subject.logo,publishStatus,sort) from MediaContent t where t.subjectId=?1 and (t.contentTitle like %?2% or t.signer like %?2%) and t.gmtModified between ?3 and ?4 order by t.sort asc,t.gmtModified desc")
    Page<ContentDTO> page(Long subjectId, String contentTitle, Date startDate, Date endDate, Pageable page);

    @Query("select new top.ulug.cms.media.dto.ContentDTO(subject.subjectCode,subject.subjectName,subject.subjectNote,contentType,contentTitle,signer,tags,summary,article,subject.logo,gmtModified) from MediaContent t where t.contentId=?1 and t.publishStatus='1' and t.subject.status='1' ")
    ContentDTO findPublic(Long id);

    @Query("select new top.ulug.cms.media.dto.ContentDTO(contentId,subject.subjectCode,subject.subjectName,contentType,contentTitle,signer,cover,tags,summary,gmtModified,subject.logo,publishStatus,sort) from MediaContent t where t.publishStatus='1' and t.subject.status='1' and t.cover is not null order by t.gmtModified desc")
    Page<ContentDTO> pagePublicRec(Pageable page);
}
