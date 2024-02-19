package top.ulug.base.security;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Created by liujf on 2019/3/24.
 * 逝者如斯夫 不舍昼夜
 */
public abstract class Digest {
    public static final String ENCRYPT_CODE_MD5 = "MD5";
    public static final String ENCRYPT_CODE_SHA512 = "SHA-512";
    public static final String ENCRYPT_CODE_SHA256 = "SHA-256";
    public static final String ENCRYPT_CODE_SHA1 = "SHA-1";
    public static final String ENCRYPT_CODE_MAC_SHA256 = "HmacSHA256";

    public static String MD5Encrypt(String data) {
        return encrypt(data, ENCRYPT_CODE_MD5);
    }

    public static String SHA512Encrypt(String data) {
        return encrypt(data, ENCRYPT_CODE_SHA512);
    }

    public static String SHA256Encrypt(String data) {
        return encrypt(data, ENCRYPT_CODE_SHA256);
    }

    public static String SHA1Encrypt(String data) {
        return encrypt(data, ENCRYPT_CODE_SHA1);
    }

    public static String MACSHA256Encrypt(String data, String key) {
        String enstr = null;
        Mac mac;
        try {
            mac = Mac.getInstance(ENCRYPT_CODE_MAC_SHA256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), ENCRYPT_CODE_MAC_SHA256);
            mac.init(secretKeySpec);
            byte[] bt = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            enstr = Base64.encodeBytes(bt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enstr.toUpperCase();
    }

    /**
     * 信息摘要算法
     *
     * @param data
     * @param enc
     * @return 加密消息
     */
    public static String encrypt(String data, String enc) {
        String enstr = null;
        MessageDigest md;
        try {
            byte[] bt = data.getBytes(StandardCharsets.UTF_8);
            if (enc == null || "".equals(enc)) {
                enc = "SHA-256";
            }
            md = MessageDigest.getInstance(enc);
            md.reset();
            md.update(bt);
            enstr = Base64.encodeBytes(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enstr;
    }

    public static String encryptToHex(String data, String enc) {
        String enstr = null;
        MessageDigest md;
        try {
            byte[] bt = data.getBytes(StandardCharsets.UTF_8);
            if (enc == null || "".equals(enc)) {
                enc = "SHA-256";
            }
            md = MessageDigest.getInstance(enc);
            md.reset();
            md.update(bt);
            enstr = Hex.toHexString(md.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enstr;
    }
}
