package top.ulug.core.auth.service;

import top.ulug.base.dto.WrapperDTO;
import top.ulug.core.auth.domain.AuthFormData;
import top.ulug.jpa.CurdService;

/**
 * Created by liujf on 2021/5/25.
 * 逝者如斯夫 不舍昼夜
 */
public interface FormDataService extends CurdService<AuthFormData,AuthFormData> {

    /**
     * 查找UI配置数据
     *
     * @param code 代码
     * @return 配置数据
     */
    WrapperDTO<AuthFormData> mapper(String code);
}
