package top.ulug.base.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http连接
 *
 * @author fddi
 */
public abstract class HttpConnUtils {
    public static final String RESULT_FAIL = "fail";

    public static String post(String targetUrl, String body) {
        URL url;
        String httpResult = HttpConnUtils.RESULT_FAIL;
        try {
            url = new URL(targetUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url
                    .openConnection(); // 实例化一个通信；
            urlCon.setDoOutput(true);
            urlCon.setRequestMethod("POST");
            urlCon.setUseCaches(false);
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(10000);
            urlCon.connect(); // 打开通信；
            urlCon.getOutputStream().write(body.getBytes());//写入参数
            InputStream input = urlCon.getInputStream();
            if (urlCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                urlCon.disconnect();
                httpResult = HttpConnUtils.RESULT_FAIL;
            } else {
                httpResult = StreamUtils.readStream(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult;
    }


    public String get(String targetUrl) {
        URL url;
        String httpResult = HttpConnUtils.RESULT_FAIL;
        try {
            url = new URL(targetUrl);
            HttpURLConnection urlCon = (HttpURLConnection) url
                    .openConnection(); // 实例化一个通信；
            urlCon.setRequestMethod("GET");
            urlCon.setConnectTimeout(5000);
            urlCon.setReadTimeout(10000);
            urlCon.connect(); // 打开通信；
            InputStream input = urlCon.getInputStream();
            if (urlCon.getResponseCode() != HttpURLConnection.HTTP_OK) {
                urlCon.disconnect();
                httpResult = HttpConnUtils.RESULT_FAIL;
            } else {
                httpResult = StreamUtils.readStream(input);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpResult;
    }

}
