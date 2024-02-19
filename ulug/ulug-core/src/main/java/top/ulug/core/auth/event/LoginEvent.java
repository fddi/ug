package top.ulug.core.auth.event;

import org.springframework.context.ApplicationEvent;
import top.ulug.base.dto.AccountDTO;

/**
 * Created by liujf on 2019/7/17.
 * 逝者如斯夫 不舍昼夜
 */
public class LoginEvent extends ApplicationEvent {

    private AccountDTO accountDTO;

    public LoginEvent(Object source, AccountDTO accountDTO) {
        super(source);
        this.accountDTO = accountDTO;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }
}
