package com.ccl.ccltools.other;

import java.util.Random;

public class MyClass {

    public static String pubKey = "010001";
    public static String nonce = "0CoJUm6Qyw8W8jud";
    public static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";


    public static void main(String[] args) {
        String s = creatScrectKey(16);

    }

    public static String creatScrectKey(int size) {
        Random random = new Random();
        byte[] kebytes = new byte[size];
        random.nextBytes(kebytes);
        String fString = "";
        for (byte b : kebytes) {
            fString += Integer.toHexString(0xff & b);
        }
        System.out.println("creatScrectKey String: "+fString);
        return fString;
    }

    public static void getAes(String text, String secSey){
        int pad = 16 - text.length() % 16;
        text = text + pad * ((char)pad);

    }

}
