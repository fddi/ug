package top.ulug.core.auth.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by liujf on 2019/7/17.
 * 逝者如斯夫 不舍昼夜
 */
public class LogoutEvent extends ApplicationEvent {

    private String authId;

    public LogoutEvent(Object source, String authId) {
        super(source);
        this.authId = authId;
    }

    public String getAuthId() {
        return authId;
    }
}
