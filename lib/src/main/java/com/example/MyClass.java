package com.example;

import java.math.BigInteger;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class MyClass {

    public static String pubKey = "010001";
    public static String nonce = "0CoJUm6Qyw8W8jud";
    public static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";


    public static void main(String[] args) {
        String sKey = creatScrectKey(16);

//        d = {
//                "ids": [song_id],
//        "br":12800,
//                "csrf_token":csrf
//}


        String text = "{\"br\": 12800,\"csrf_token\": \"csrf\",\"ids\": 16000}";
        String aes = aesEnscrect(aesEnscrect(text, nonce), sKey);

        System.out.println("result: "+aes);

        String aa = new StringBuilder(creatScrectKey(16)).reverse().toString();
        String str = "";
        System.out.println("str: "+aa);
        for (int i = 0; i < aa.length(); i++) {
            int ch = (int) aa.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        System.out.println("aa: "+str);

        BigInteger rsa1 = new BigInteger(str, 16);
        System.out.println("aaaa: "+rsa1.toString());


        BigInteger rsa2 = new BigInteger(pubKey, 16);
        System.out.println("aaaaaaa: "+rsa2.toString());

        BigInteger rsa3 = new BigInteger(modulus, 16);
        System.out.println("aaaaaaa: "+rsa3.toString());

        BigInteger pow = rsa1.pow(0x010001).mod(rsa3);

        System.out.println("aaaaaaa: "+pow.toString(16));

    }

    public static String aesEnscrect(String text, String key) {
        try {
            int pad = 16 - text.length() % 16;
            text = text + pad * ((char) pad);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            return new BASE64Encoder().encode(encrypted);//此处使用BASE64做转码。
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String creatScrectKey(int size) {
        Random random = new Random();
        byte[] kebytes = new byte[size];
        random.nextBytes(kebytes);
        String fString = "";
        for (byte b : kebytes) {
            fString += Integer.toHexString(0xff & b);
        }
        System.out.println("creatScrectKey String: " + fString.substring(0, 16));
        return fString.substring(0, 16);
    }

}
