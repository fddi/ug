package top.ulug.base.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by liujf on 2020/2/16.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class CommandExecute {

    /**
     * 是否linux服务器
     *
     * @return bol
     */
    public static boolean isOSLinux() {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("linux")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param command 命令行
     * @param file    执行目录
     * @return
     */
    public static String executeCommand(String command, File file) throws Exception {
        StringBuffer output = new StringBuffer();
        Process p;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        p = Runtime.getRuntime().exec(command, null, file);
        p.waitFor();
        inputStreamReader = new InputStreamReader(p.getInputStream(), "GBK");
        reader = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        reader.close();
        inputStreamReader.close();
        return output.toString();
    }

    /**
     * @param command 命令行
     * @param file    执行目录
     * @return
     * @throws Exception
     */
    public static String execute(String[] command, File file) throws Exception {
        StringBuffer output = new StringBuffer();
        Process p;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        p = Runtime.getRuntime().exec(command, null, file);
        p.waitFor();
        inputStreamReader = new InputStreamReader(p.getInputStream(), "GBK");
        reader = new BufferedReader(inputStreamReader);
        String line = "";
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        reader.close();
        inputStreamReader.close();
        return output.toString();
    }
}
