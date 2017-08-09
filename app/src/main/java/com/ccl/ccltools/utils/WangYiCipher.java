package com.ccl.ccltools.utils;


import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class WangYiCipher {
    public static String pubKey = "010001";
    public static String nonce = "0CoJUm6Qyw8W8jud";
    public static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";


    /*public static String aesEncrytion(int id){
        String sKey = creatScrectKey(16);
        String text = "{\"br\": 12800,\"csrf_token\": \"csrf\",\"ids\": ["+id+"]}";
        String aes = aesEnscrect(aesEnscrect(text, nonce), sKey);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(aes);
        String res = m.replaceAll("");
        Log.e("Cipher", "aes: "+res);
        return res;
    }*/

    public static String aesEncrytion(int id, String key){
//        String text = "{\"br\": 12800,\"csrf_token\": \"csrf\",\"ids\": ["+id+"]}";
        String text = "{\"csrf_token\": \"\", \"ids\": [35331568], \"br\": 12800}";
        String aes = aesEnscrect(aesEnscrect(text, nonce), key);
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(aes);
        String res = m.replaceAll("");
        Log.e("Cipher", "aes: "+res);
        return res;
    }

    public static String rsaEncrytion(String key){
        String aa = new StringBuilder(key).reverse().toString();
        String str = "";
        System.out.println("str: " + aa);
        for (int i = 0; i < aa.length(); i++) {
            int ch = (int) aa.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        System.out.println("aa: " + str);

        BigInteger rsa1 = new BigInteger(str, 16);
        System.out.println("rsa1: " + rsa1.toString());

//        BigInteger rsa2 = new BigInteger(pubKey, 16);
//        System.out.println("rsa2: " + rsa2.toString());

        BigInteger rsa3 = new BigInteger(modulus, 16);
        System.out.println("rsa3: " + rsa3.toString());

        BigInteger powMod = rsa1.pow(0x010001).mod(rsa3);

        String result = powMod.toString(16);
        Log.e("Cipher", "result: "+result);
        return result;
    }


    public static String aesEnscrect(String text, String key) {
        System.out.println("text: " + text);
        System.out.println("key: " + key);
        try {
            int pad = 16 - text.length() % 16;
            System.out.println("text pad: " + pad);
            if (pad > 0) {
                byte[] padBytes = new byte[pad];
                Arrays.fill(padBytes, (byte) pad);
                text = text + new String(padBytes);
            }
            System.out.println("text 222: " + text);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] bytes = text.getBytes();
            byte[] result = null;
            do {
                if (bytes.length > 16) {
                    byte[] newBytes = Arrays.copyOfRange(bytes, 0, 16);
                    System.out.println("while-----" + toPrint(newBytes));
                    newBytes = cipher.update(newBytes);
                    System.out.println("while----- 22222222222" + toPrint(newBytes));
                    if (result == null) {
                        result = newBytes;
                    } else {
                        result = concat(result, newBytes);
                    }
                    bytes = Arrays.copyOfRange(bytes, 16, bytes.length);
                } else {
                    byte[] newBytes = Arrays.copyOfRange(bytes, 0, bytes.length);
                    System.out.println("while-----" + toPrint(newBytes));
                    newBytes = cipher.update(newBytes);
                    System.out.println("while----- 22222222222" + toPrint(newBytes));
                    if (result == null) {
                        result = newBytes;
                    } else {
                        result = concat(result, newBytes);
                    }
                    bytes = null;
                }
            } while (bytes != null);
            String encode = Base64.encodeToString(result, Base64.DEFAULT);
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(encode);
            encode = m.replaceAll("");
            System.out.println("text 222  encode: " + encode);
            return encode;//此处使用BASE64做转码。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String toPrint(byte[] var0) {
        if (var0 == null) {
            return "null";
        } else {
            int var1 = var0.length - 1;
            if (var1 == -1) {
                return "[]";
            } else {
                StringBuilder var2 = new StringBuilder();
                var2.append('[');
                int var3 = 0;

                while (true) {
                    var2.append(var0[var3] & 0xff);
                    if (var3 == var1) {
                        return var2.append(']').toString();
                    }

                    var2.append(", ");
                    ++var3;
                }
            }
        }
    }

    public static String creatScrectKey(int size) {
        Random random = new Random();
        byte[] kebytes = new byte[size];
        random.nextBytes(kebytes);
        String fString = "";
        for (byte b : kebytes) {
            fString += Integer.toHexString(0xff & b);
        }
        Log.e("Cipher", "creatScrectKey String: " + fString.substring(0, 16));
        return fString.substring(0, 16);
    }


}
