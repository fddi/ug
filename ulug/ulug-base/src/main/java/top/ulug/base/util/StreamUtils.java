package top.ulug.base.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by fddiljf on 2016/6/14.
 */
public abstract class StreamUtils {
    /**
     * 从输入流中读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String readStream(InputStream inStream) throws Exception {
        InputStreamReader inputReader = new InputStreamReader(
                inStream, "UTF-8"); // 读取数据；
        BufferedReader reader = new BufferedReader(inputReader);
        String inputLine;
        StringBuffer strBr = new StringBuffer();
        while ((inputLine = reader.readLine()) != null) {
            strBr.append(inputLine).append("\n");
        }
        reader.close();
        inputReader.close();
        inStream.close();
        return strBr.toString();
    }

    /**
     * 从输入流中读取数据
     *
     * @param inStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();//二进制数据
        outStream.close();
        inStream.close();
        return data;
    }
}
