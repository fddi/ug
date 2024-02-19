package top.ulug.base.util;


import top.ulug.base.e.SQLikeEnum;
import top.ulug.base.security.Digest;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fddiljf on 2016/4/19.
 */
public abstract class StringUtils {

    private final static String STRING_BASE = "abcdefghijklmnopqrstuvwxyz0123456789";

    public final static Pattern PATTERN_LOGIN_ID = Pattern.compile("^[a-zA-Z0-9_]{4,16}$");

    public final static Pattern PATTERN_PHONE = Pattern
            .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

    public final static Pattern PATTERN_TEL = Pattern.compile("^((\\d{3,4}-)|\\d{3,4}-)?\\d{5,11}$");

    public final static Pattern PATTERN_EMAIL = Pattern
            .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

    public final static Pattern PATTERN_IMG = Pattern
            .compile(".*?(gif|jpeg|png|jpg|bmp)");

    public final static Pattern PATTERN_VIDEO = Pattern
            .compile(".*?(mp4|mov|avi|flv|wmv|mpeg|mkv|rm|rmvb|asf|vob|ts|dat)");

    public final static Pattern PATTERN_AUDIO = Pattern
            .compile(".*?(cda|wav|mp3|wma|ra|midi|ape|ogg|flac|aac)");

    public final static Pattern PATTERN_DOC = Pattern
            .compile(".*?(txt|pdf|xml|doc|docx|dot|wps|wpt|xls|xlsx|et|ett|csv|ppt|pptx|dps|dpt|pps|pot|potx)");

    public final static Pattern PATTERN_ARCHIVE = Pattern
            .compile(".*?(zip|zipx|7z|rar|alz|egg|cab|tar|gz|tgz)");

    public final static Pattern PATTERN_URL = Pattern
            .compile("^(https|http)://.*?$(net|com|.com.cn|org|me|)");

    public final static Pattern PATTERN_XLS = Pattern.compile(".*?(xls)");

    public final static Pattern PATTERN_DATE_P = Pattern.compile("\\d{4}/\\d{1,2}/\\d{1,2}");

    public final static Pattern PATTERN_NUMBER = Pattern.compile("^[0-9]+\\.{0,1}[0-9]+$");

    public final static Pattern PATTERN_SPEC_CHAR =
            Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]");

    public final static Pattern PATTERN_SUMMARY = Pattern.compile("[\\uff0c|\\u3002|\\uff1f|\\uff01|\\uff1a|\\uff1b|\\u4e00-\\u9fa5]*");

    /**
     * 判断字符串是否为空
     *
     * @param str 验证字符
     * @return true or false
     */
    public static boolean isEmpty(String str) {
        if (str == null)
            return true;

        str = str.trim();
        if ("".equals(str))
            return true;
        if ("null".equals(str) || "undefined".equals(str))
            return true;
        return false;
    }

    /**
     * @param str     字符串
     * @param pattern 正则表达式规则
     * @return bool
     */
    public static boolean regexMatch(String str, Pattern pattern) {
        if (str == null || str.trim().length() == 0)
            return false;
        return pattern.matcher(str).matches();
    }

    /**
     * @param length 随机长度
     * @return 随机字符串
     */
    public static String getStringRandom(int length) { //length表示生成字符串的长度
        String base = STRING_BASE.toUpperCase();
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * @param params 参数
     * @param key    密钥
     * @return 签名字符串
     */
    public static String signString(Map<String, String> params, String key) {
        if (params == null) {
            return "";
        }
        SortedMap<String, String> parameters = new TreeMap<String, String>(params);
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();  //所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //sign和空值不参与签名组串
            if (null != v && !"".equals(v) && !"sign".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        String result = sb.toString() + "key=" + key;
        return result;
    }

    /**
     * 使用“-”将字符连接为标签字符
     *
     * @param str 需要连接的字符串
     * @return str
     */
    public static String linkTags(String... str) {
        String result = "";
        for (int i = 0; i < str.length; i++) {
            result += "-" + str[i];
        }
        return result;
    }

    /**
     * 给sql参数增加"%"字符
     *
     * @param param      参数
     * @param sqLikeEnum like类型
     * @return str
     */
    public static String linkSQLike(String param, SQLikeEnum sqLikeEnum) {
        switch (sqLikeEnum) {
            case LEFT:
                return "%" + param;
            case RIGHT:
                return param + "%";
            default:
                return "%" + param + "%";
        }
    }

    /**
     * redis key值处理
     *
     * @param tag key
     * @return redis key
     */
    public static String getCacheKey(String... tag) {
        String tags = linkTags(tag);
        return Base64.getEncoder().encodeToString(Digest.SHA256Encrypt(tags)
                .getBytes()).toUpperCase();
    }

    /**
     * 字符串清洗，去掉空格及特殊字符
     *
     * @param data 字符串
     * @return 清洗后字符串
     */
    public static String cleaning(String data) {
        if (data == null) {
            return null;
        }
        data = data.replaceAll("\\s*", "");
        // 清除掉所有特殊字符
        Matcher m = PATTERN_SPEC_CHAR.matcher(data);
        data = m.replaceAll("").trim();
        return data;
    }

    /**
     * 过滤小括号及里面内容
     *
     * @param data 字符串
     * @return 过滤后字符串
     */
    public static String filterNotes(String data) {
        if (data == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("(?<=（)(.+?)(?=）)");
        data = pattern.matcher(data).replaceAll("").trim();
        pattern = Pattern.compile("(?<=\\()(.+?)(?=\\))");
        data = pattern.matcher(data).replaceAll("").trim();
        return data;
    }

    /**
     * 生成唯一key
     *
     * @param projectId 项目名
     * @param name      名称
     * @return key
     */
    public static String createKey(String projectId, String name) {
        String randomStr = UUID.randomUUID().toString();
        Long time = new Date().getTime();
        return Digest.SHA1Encrypt(
                StringUtils.linkTags(projectId, name, randomStr, String.valueOf(time)));
    }

    public static String createFileKey(String projectId, String name) {
        String randomStr = UUID.randomUUID().toString();
        Long time = new Date().getTime();
        return Digest.encryptToHex(
                StringUtils.linkTags(projectId, name, randomStr, String.valueOf(time)), Digest.ENCRYPT_CODE_SHA1);
    }

    /**
     * 从字符串查找匹配的所有字符返回
     *
     * @param str     字符串
     * @param length  返回最大长度
     * @param pattern 正则表达式
     * @return str
     */
    public static String matherFind(String str, int length, Pattern pattern) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        Matcher matcher = pattern.matcher(str);
        StringBuilder sb = new StringBuilder();
        while (matcher.find() && sb.length() < length) {
            sb.append(matcher.group());
        }
        String s = sb.toString();
        if (s.length() > length) {
            s = s.substring(0, length);
        }
        return s;
    }
}
