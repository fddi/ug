package top.ulug.base.dto;

import com.alibaba.fastjson.JSON;
import top.ulug.base.e.ResultMsgEnum;
import top.ulug.base.util.StringUtils;

import java.io.Serializable;

/**
 * DTO 基本对象
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */
public class WrapperDTO<T> implements Serializable {
    private int resultCode;
    private String resultMsg;
    private T resultData;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public T getResultData() {
        return resultData;
    }

    public void setResultData(T resultData) {
        this.resultData = resultData;
    }

    public static WrapperDTO<String> success() {
        WrapperDTO<String> wrapperDTO = new WrapperDTO<>();
        wrapperDTO.setResultCode(ResultMsgEnum.RESULT_SUCCESS.getCode());
        wrapperDTO.setResultMsg(ResultMsgEnum.RESULT_SUCCESS.getMsg());
        return wrapperDTO;
    }

    public static <U> WrapperDTO<U> success(U u) {
        WrapperDTO<U> wrapperDTO = new WrapperDTO<>();
        wrapperDTO.setResultCode(ResultMsgEnum.RESULT_SUCCESS.getCode());
        wrapperDTO.setResultMsg(ResultMsgEnum.RESULT_SUCCESS.getMsg());
        if (u != null) {
            wrapperDTO.setResultData(u);
        }
        return wrapperDTO;
    }

    public static <U> WrapperDTO<U> npe(String returnMsg) {
        return fail(ResultMsgEnum.RESULT_ERROR_NPE, returnMsg);
    }

    public static <U> WrapperDTO<U> noPermission() {
        return fail(ResultMsgEnum.RESULT_ERROR_NO_PERMISSION, null);
    }

    public static <U> WrapperDTO<U> fail(String returnMsg) {
        return fail(ResultMsgEnum.RESULT_ERROR, returnMsg);
    }

    public static <U> WrapperDTO<U> fail(ResultMsgEnum resultMsgEnum, String returnMsg) {
        WrapperDTO<U> wrapperDTO = new WrapperDTO<>();
        wrapperDTO.setResultCode(resultMsgEnum.getCode());
        if (!StringUtils.isEmpty(returnMsg)) {
            wrapperDTO.setResultMsg(resultMsgEnum.getMsg() + "[" + returnMsg + "]");
        } else {
            wrapperDTO.setResultMsg(resultMsgEnum.getMsg());
        }
        return wrapperDTO;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
