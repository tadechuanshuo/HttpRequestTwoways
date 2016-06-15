package com.yunmall.ymsdk.utility;

import com.yunmall.ymsdk.net.http.RequestParams;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.TreeSet;

/**
 * 授权相关工具类
 * Created by Zhp on 2014/5/21.
 */
public class AuthUtils {

    public static String md5(String input) {
        String result = input;
        if (input != null) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(input.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                result = hash.toString(16);
                if ((result.length() % 2) != 0) {
                    result = "0" + result;
                }
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void md5Sign(RequestParams params) {
        Set<String> keys = new TreeSet<String>(params.getStringKeySet());
        String text = "";

        for (String key : keys) {
            text += params.getStringValue(key);
        }

//        params.put("sign", md5(text));
    }
}
