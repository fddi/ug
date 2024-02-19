package top.ulug.cms.media.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.ulug.cms.media.domain.MediaSubject;
import top.ulug.cms.media.dto.SubjectDTO;

/**
 * Created by liujf on 2019/4/19.
 * 逝者如斯夫 不舍昼夜
 */

public interface MediaSubjectRepository extends JpaRepository<MediaSubject, Long>,
        JpaSpecificationExecutor<MediaSubject> {

    MediaSubject findBySubjectCode(String subjectCode);

    @Query("select new top.ulug.cms.media.dto.SubjectDTO(t.subjectId,t.subjectCode,t.subjectName,t.subjectNote,t.subjectType,t.logo,t.areaCode,t.unitCode,t.status,t.sort) from MediaSubject t where t.unitCode=?1 and (t.subjectName like %?2% or t.subjectCode like %?2%) order by sort")
    Page<SubjectDTO> page(String unitCode, String subjectName, Pageable page);

    @Query("select new top.ulug.cms.media.dto.SubjectDTO(t.subjectCode,t.subjectName,t.subjectNote,t.subjectType,t.logo) from MediaSubject t where (t.subjectName like %?1% or  t.subjectCode like %?1% or  t.subjectNote like %?1%) and t.status='1' order by sort")
    Page<SubjectDTO> pagePublic(String v, Pageable page);

    @Query("select new top.ulug.cms.media.dto.SubjectDTO(t.subjectCode,t.subjectName,t.subjectNote,t.subjectType,t.logo) from MediaSubject t where t.subjectCode=?1 and t.status='1'")
    SubjectDTO findPublic(String code);
}
