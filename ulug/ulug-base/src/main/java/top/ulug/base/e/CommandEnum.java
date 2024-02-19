package top.ulug.base.e;

/**
 * Created by liujf on 2021/6/17.
 * 逝者如斯夫 不舍昼夜
 */
public enum CommandEnum {
    //pm2 启动命令
    COMMAND_PM2_START("pm2.cmd start app.js -n ", "pm2 start app.js -n ", "pm2启动节点命令"),
    //pm2 停止命令
    COMMAND_PM2_STOP("pm2.cmd stop ", "pm2 stop ", "pm2 停止命令"),
    //pm2 刪除進程命令
    COMMAND_PM2_DEL("pm2.cmd delete ", "pm2 delete ", "pm2 刪除進程命令"),
    //node 版本命令
    COMMAND_NODE_VERSION("node -v ", "node -v ", "node 版本命令"),
    //pm2 版本命令
    COMMAND_PM2_VERSION("pm2.cmd -v ", "pm2 -v ", "pm2 版本命令");

    private String winCmd;
    private String linuxCmd;
    private String note;

    CommandEnum(String winCmd, String linuxCmd, String note) {
        this.winCmd = winCmd;
        this.linuxCmd = linuxCmd;
        this.note = note;
    }

    public String getWinCmd() {
        return winCmd;
    }

    public void setWinCmd(String winCmd) {
        this.winCmd = winCmd;
    }

    public String getLinuxCmd() {
        return linuxCmd;
    }

    public void setLinuxCmd(String linuxCmd) {
        this.linuxCmd = linuxCmd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
