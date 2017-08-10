package com.example;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Encoder;

public class MyClass {

    public static String pubKey = "010001";
    public static String nonce = "0CoJUm6Qyw8W8jud";
    public static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";

    public static void main(String[] args) {
        parseXianiSongUrl("8h2.c5%%22ph7513655EtFao%22_93_dE%64EE-t%lm2FF13%keb5792%np2i%F33153ecaE1d95u%Fc22391_Fy2792-3El3odF238%la%5ab318-lAmn29345.u3dc5858%%5.9517EmtD%b2b%%5");
    }

    public static void main2(String[] args) {
        //        String sKey = creatScrectKey(16);
        String sKey = "97e99d97ddf04954";

        //        String text = "{\"br\": 12800,\"csrf_token\": \"csrf\",\"ids\": [35331568]}";
        String text = "{\"csrf_token\": \"\", \"ids\": [35331568], \"br\": 12800}";
        String aes = aesEnscrect(aesEnscrect(text, nonce), sKey);
        System.out.println("\r\n\r\n\r\n");
        System.out.println("result: " + aes);

        String aa = new StringBuilder(sKey).reverse().toString();
        String str = "";
        System.out.println("str: " + aa);
        for (int i = 0; i < aa.length(); i++) {
            int ch = (int) aa.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        System.out.println("aa: " + str);

        BigInteger rsa1 = new BigInteger(str, 16);
        System.out.println("aaaa: " + rsa1.toString());


        BigInteger rsa2 = new BigInteger(pubKey, 16);
        System.out.println("aaaaaaa: " + rsa2.toString());

        BigInteger rsa3 = new BigInteger(modulus, 16);
        System.out.println("aaaaaaa: " + rsa3.toString());

        BigInteger pow = rsa1.pow(0x010001).mod(rsa3);

        System.out.println("aaaaaaa: " + pow.toString(16));
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
            String encode = new BASE64Encoder().encode(result);
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

    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
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

    public static void parseXianiSongUrl(String data) {
        int num = Integer.parseInt(String.valueOf(data.charAt(0)));
        int reNum = data.length() - 1;
        int avgLen = reNum / num;
        int remainder = reNum % num;

        //    result = [location[i * (avg_len + 1) + 1: (i + 1) * (avg_len + 1) + 1] for i in range(remainder)]
        ArrayList<String> result = new ArrayList();
        for (int i = 0; i < remainder; i++) {
            String substring = data.substring(i * (avgLen + 1) + 1, (i + 1) * (avgLen + 1) + 1);
            if (substring != null) {
                result.add(substring);
            }
        }

        //     [location[(avg_len + 1) * remainder:][i * avg_len + 1: (i + 1) * avg_len + 1] for i in range(num - remainder)]
        int diff = num - remainder;
        if (diff > 0) {
            ArrayList<String> result2 = new ArrayList();
            for (int i = 0; i < diff; i++) {
                String substring = data.substring((avgLen + 1) * remainder, data.length()).substring(i * avgLen + 1, (i + 1) * avgLen + 1);
                if (substring != null) {
                    result2.add(substring);
                }
            }
            result.addAll(result2);
        }

        System.out.println("resut:  " + result.toString());

        if (result != null && result.size() > 0) {
            //        ''.join([''.join([result[j][i] for j in range(num)])for i in range(avg_len)])
            String without = "";
            for (int i = 0; i < avgLen; i++) {
                char[] inner = new char[num];
                for (int j = 0; j < num; j++) {
                    inner[j] = result.get(j).charAt(i);
                }
                System.out.println("inner " + i + ": " + String.valueOf(inner));
                without += String.valueOf(inner);
            }
            //            ''.join([result[r][-1] for r in range(remainder)])
            char[] lastChars = new char[remainder];
            for (int i = 0; i < remainder; i++) {
                lastChars[i] = result.get(i).charAt(result.get(i).length() - 1);
            }
            System.out.println("last: " +String.valueOf(lastChars));
            try {
                String finalData = without + String.valueOf(lastChars);
                finalData = finalData.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                finalData = finalData.replaceAll("\\+", "%2B");
                String encode = URLDecoder.decode(finalData, "UTF-8").replace('^', '0');
                System.out.println("encode: " + encode);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception: " + e.toString());
            }
        }

    }

}
