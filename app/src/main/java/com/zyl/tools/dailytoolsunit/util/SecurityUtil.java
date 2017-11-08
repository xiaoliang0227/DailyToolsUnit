package com.zyl.tools.dailytoolsunit.util;

import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by zhaoyongliang on 2017/3/15.
 */

public class SecurityUtil {

    private static final String TAG = "SecurityUtil";

    private static SecurityUtil instance;

    public static SecurityUtil getInstance() {
        if (null == instance) {
            instance = new SecurityUtil();
        }
        return instance;
    }

    public String encrypt(String content, String password) {
        String encryptedValue = null;
        try {
            Key key = generateKey(password.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(content.getBytes());
            encryptedValue = Base64.encodeToString(encVal, Base64.NO_WRAP | Base64.NO_PADDING);
        } catch (Exception e) {

        }
        return encryptedValue;
    }

    public String decrypt(String encryptedData, String password) {
        String decryptedValue = null;
        try {
            Key key = generateKey(password.getBytes(), "AES");
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = Base64.decode(encryptedData, Base64.NO_WRAP | Base64.NO_PADDING);
            byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (Exception e) {

        }
        return decryptedValue;
    }

    private Key generateKey(byte[] keyValue, String method) {
        Key key = new SecretKeySpec(keyValue, method);
        return key;
    }

}
