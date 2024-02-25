package top.ulug.base.servlet;

import com.alibaba.fastjson.JSON;
import top.ulug.base.dto.WrapperDTO;
import top.ulug.base.e.ResultMsgEnum;

import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by liujf on 2022/5/15.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class ResponseUtils {

    public static void writeFailMsg(HttpServletResponse response, ResultMsgEnum msg) throws Exception {
        WrapperDTO<String> dto = WrapperDTO.fail(msg, null);
        PrintWriter out = response.getWriter();
        out.print(JSON.toJSONString(dto));
        out.flush();
        out.close();
    };
}
