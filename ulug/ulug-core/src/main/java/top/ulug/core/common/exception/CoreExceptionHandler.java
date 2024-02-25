package top.ulug.core.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ulug.base.dto.WrapperDTO;


import static top.ulug.base.e.ResultMsgEnum.RESULT_EXCEPTION;

/**
 * Created by fddiljf on 2017/4/22.
 * 逝者如斯夫 不舍昼夜
 */

@ControllerAdvice
public class CoreExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public WrapperDTO<String> errorHandler(HttpServletRequest req, Exception e) {
        WrapperDTO<String> dto = WrapperDTO.fail(RESULT_EXCEPTION, e.getMessage());
        e.printStackTrace();
        return dto;
    }
}
