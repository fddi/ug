package top.ulug.core.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.ulug.core.auth.domain.AuthFormData;

/**
 * Created by liujf on 2021/5/25.
 * 逝者如斯夫 不舍昼夜
 */
public interface AuthFormDataRepository  extends JpaRepository<AuthFormData, Long>{

    AuthFormData findByFormCodeAndStatus(String uiCode, String status);

    @Query("from AuthFormData t where t.formName like %?1% or t.formCode like %?2%")
    Page<AuthFormData> page(String formName, String formCode, Pageable pageable );
}
