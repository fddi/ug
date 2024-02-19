package top.ulug.base.inf;

import java.lang.annotation.*;

/**
 * Created by liujf on 2019/3/30.
 * 逝者如斯夫 不舍昼夜
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiDocument {

    /**
     * @return method说明 md格式
     */
    String note();

    /**
     * @return 参数示例 md格式
     */
    String paramsExample();

    /**
     * @return 返回示例 md格式
     */
    String resultExample();
}
