package top.ulug.base.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import top.ulug.base.dto.QuartzTaskDTO;
import top.ulug.base.inf.QuartzJob;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liujf on 2022/5/15.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class ScanningUtils {

    /**
     * 扫描定时任务类
     *
     * @param projectId 项目id
     * @return list
     */
    public static List<QuartzTaskDTO> scanningQuartzBean(String projectId) {
        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext();
        Class<? extends Annotation> annotationClass = QuartzJob.class;
        Map<String, Object> beanWithAnnotation = applicationContext.getBeansWithAnnotation(annotationClass);
        Set<Map.Entry<String, Object>> entitySet = beanWithAnnotation.entrySet();
        List<QuartzTaskDTO> list = new ArrayList<>();
        for (Map.Entry<String, Object> entry : entitySet) {
            Class<? extends Object> clazz = entry.getValue().getClass();//获取bean对象
            QuartzJob job = AnnotationUtils.findAnnotation(clazz, QuartzJob.class);
            if (job != null) {
                QuartzTaskDTO task = new QuartzTaskDTO();
                task.setTaskGroup(projectId);
                task.setTaskName(job.jobName());
                task.setCronExp(job.cornExp());
                task.setStatus("0");
                task.setTaskService(clazz.getName());
                list.add(task);
            }
        }
        return list;
    }

}
