package top.ulug.base.e;

/**
 * Created by liujf on 2021/6/17.
 * 逝者如斯夫 不舍昼夜
 */
public enum ResultMsgEnum {
    RESULT_SUCCESS(200, "操作成功"),
    RESULT_EXCEPTION(500, "程序异常"),
    RESULT_ERROR(10000, "操作失败"),
    RESULT_ERROR_CLIENT_FORBIDDEN(10001, "客户端安全校验异常"),
    RESULT_ERROR_USER_DISABLE(10002, "登录账户无效，请联系管理员！"),
    RESULT_ERROR_LOGIN_FAIL(10003, "用户名或密码错误"),
    RESULT_ERROR_AUTH_TIMEOUT(10004, "输入失败次数过多，请稍作休息，待会再试！"),
    RESULT_ERROR_OUT_TIME(10005, "连接超时"),
    RESULT_ERROR_NO_PERMISSION(10006, "没有权限访问"),
    RESULT_ERROR_NPE(10007, "参数为空"),
    RESULT_ERROR_ORG_REPEAT(10008, "单位代码重复"),
    RESULT_ERROR_NOT_IMG(10009, "上传图片格式不符"),
    RESULT_ERROR_NPE_NODE_PATH(10010, "无法从服务器中查找到节点路径"),
    RESULT_ERROR_DISK_OUT_SIZE(10011, "网盘空间已满");

    private Integer code;
    private String msg;

    ResultMsgEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
