package top.ulug.cms.media.event;

import org.springframework.context.ApplicationEvent;
import top.ulug.cms.media.domain.MediaSubject;

/**
 * Created by liujf on 2020-05-29.
 * 逝者如斯夫 不舍昼夜
 */
public class SubjectCreateEvent extends ApplicationEvent {
    private MediaSubject subject;

    public SubjectCreateEvent(Object source, MediaSubject subject) {
        super(source);
        this.subject = subject;
    }

    public MediaSubject getSubject() {
        return subject;
    }

}
