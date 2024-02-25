package top.ulug.core.auth.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.util.StringUtils;
import top.ulug.core.auth.domain.AuthFormData;
import top.ulug.core.auth.repository.AuthFormDataRepository;
import top.ulug.core.auth.service.FormDataService;
import top.ulug.base.dto.PageDTO;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by liujf on 2021/5/25.
 * 逝者如斯夫 不舍昼夜
 */
@Service
public class FormDataServiceImpl implements FormDataService {
    @Autowired
    AuthFormDataRepository formRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public WrapperDTO<String> save(AuthFormData... formData) {
        List<AuthFormData> list = Arrays.asList(formData);
        if (list.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthFormData");
        }
        formRepository.saveAll(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<String> del(AuthFormData... formData) {
        List<AuthFormData> list = Arrays.asList(formData);
        if (list.isEmpty()) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthFormData");
        }
        formRepository.deleteAllInBatch(list);
        return WrapperDTO.success();
    }

    @Override
    public WrapperDTO<AuthFormData> findOne(Long id) {
        Optional<AuthFormData> optional = formRepository.findById(id);
        return optional.map(WrapperDTO::success).orElseGet(() -> WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, "AuthFormData"));
    }

    @Override
    public WrapperDTO<PageDTO<AuthFormData>> findPage(int pageSize, int pageNo, AuthFormData formData) {
        pageSize = pageSize == 0 ? 10 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        String formName = "";
        if (formData.getFormName() != null) {
            formName = formData.getFormName();
        }
        Page<AuthFormData> page = formRepository.page(formName,formName,pageable);
        PageDTO<AuthFormData> pageDTO = new PageDTO<>();
        return WrapperDTO.success(pageDTO.convert(page));
    }

    @Override
    public WrapperDTO<AuthFormData> mapper(String formCode) {
        if (StringUtils.isEmpty(formCode)) {
            return WrapperDTO.fail(ResultMsgEnum.RESULT_ERROR_NPE, null);
        }
        AuthFormData data = formRepository.findByFormCodeAndStatus(formCode, "1");
        return WrapperDTO.success(data);
    }
}
