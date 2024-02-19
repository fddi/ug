package top.ulug.base.inf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by liujf on 2020-10-27.
 * 逝者如斯夫 不舍昼夜
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface QuartzJob {

    String jobName();

    String cornExp();
}
