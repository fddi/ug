package top.ulug.base.security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public abstract class AES {
    public static final String IV = "Z9VXQIL7OHOY3W8N";

    public static String encrypt(String text, String key) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec);
        byte[] encrypted = cipher.doFinal(padString(text).getBytes());
        String result = Base64.encodeBytes(encrypted);
        return result;
    }

    public static String decrypt(String code, String key) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keyspec);
        byte[] decrypt = cipher.doFinal(Base64.decode(code));
        String result = new String(decrypt);
        return result;
    }

    public static String encrypt(String text, String key, String iv) throws Exception {
        if (text == null || text.length() == 0)
            throw new Exception("Empty string");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(padString(text).getBytes());
        return Base64.encodeBytes(encrypted);
    }

    public static String decrypt(String code, String key, String iv) throws Exception {
        if (code == null || code.length() == 0)
            throw new Exception("Empty string");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] decrypt = cipher.doFinal(Base64.decode(code));
        return new String(decrypt);
    }

    private static String padString(String source) {
        char paddingChar = ' ';
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++) {
            source += paddingChar;
        }

        return source;
    }

}