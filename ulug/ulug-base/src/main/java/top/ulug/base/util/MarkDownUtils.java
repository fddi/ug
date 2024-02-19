package top.ulug.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by liujf on 2020-08-31.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class MarkDownUtils {

    public static String getCatalog(String notes) {
        JSONArray array = new JSONArray();
        if (StringUtils.isEmpty(notes)) {
            return array.toString();
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(notes.getBytes());
            InputStreamReader inputReader = new InputStreamReader(
                    is, "UTF-8"); // 读取数据；
            BufferedReader reader = new BufferedReader(inputReader);
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject obj = new JSONObject();
                if (StringUtils.regexMatch(line, Pattern.compile("(.*)[\\[\\]\\(\\)](.*)"))) {
                    continue;
                }
                String t = StringUtils.cleaning(line);
                if (line.trim().indexOf("####") == 0) {
                    obj.put("level", 4);
                    obj.put("title", line.replaceAll("#", ""));
                    array.add(obj);
                } else if (line.trim().indexOf("###") == 0) {
                    obj.put("level", 3);
                    obj.put("title", line.replaceAll("#", ""));
                    array.add(obj);
                } else if (line.trim().indexOf("##") == 0) {
                    obj.put("level", 2);
                    obj.put("title", line.replaceAll("#", ""));
                    array.add(obj);
                } else if (line.trim().indexOf("#") == 0) {
                    obj.put("level", 1);
                    obj.put("title", line.replaceAll("#", ""));
                    array.add(obj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return array.toString();
    }
}
