package com.ccl.ccltools.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccl.ccltools.bean.AusleseSongListBean;
import com.ccl.ccltools.bean.SongBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wang on 2017/8/5.
 */

public class DataUtils {

    public static final int DATA_WANGYI = 0;
    public static final int DATA_XIAMI = 1;
    public static final int DATA_QQ = 2;
    public static String pubKey = "010001";
    public static String nonce = "0CoJUm6Qyw8W8jud";
    public static String modulus = "00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b3ece0462db0a22b8e7";

    public static ArrayList<AusleseSongListBean> getSongList(int type, int offset) {
        ArrayList<AusleseSongListBean> data = null;
        switch (type) {
            case DATA_XIAMI:
                data = getXiamiSongList(offset);
                break;
            case DATA_QQ:
                data = getQQSongList(offset);
                break;
            default:
                data = getWangyiSongList(offset);
                break;
        }
        return data;
    }

    public static ArrayList<SongBean> getSongListData(int type, String href) {
        ArrayList<SongBean> data = null;
        switch (type) {
            case DATA_XIAMI:
                data = getXiamiSongListData(href);
                break;
            case DATA_QQ:
                data = getQQSongListData(href);
                break;
            default:
                data = getWangyiSongListData(href);
                break;
        }
        return data;
    }

    public static ArrayList<SongBean> getSongData(int type, String href) {
        ArrayList<SongBean> data = null;
        switch (type) {
            case DATA_XIAMI:
                data = getXiamiSongData(href);
                break;
            case DATA_QQ:
                data = getQQSongData(href);
                break;
            default:
                data = getWangyiSongData(href);
                break;
        }
        return data;
    }

    public static ArrayList<AusleseSongListBean> getWangyiSongList(int offset) {
        Request request = new Request.Builder()
                .url("http://music.163.com/discover/playlist/?order=hot&limit=40&offset=" + (40 * offset))
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .build();
        Log.e("DataUtils", "11");
        try {
            Log.e("DataUtils", "22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            Document html = Jsoup.parse(string);
            Elements elementsByClass = html.getElementsByClass("m-cvrlst f-cb");
            ArrayList<AusleseSongListBean> beans = new ArrayList<>();
            for (Element link : elementsByClass) {
                Log.e("DataUtils", "33");
                Elements datas = link.getElementsByClass("u-cover u-cover-1");
                for (Element data : datas) {
                    Log.e("DataUtils", "44");
                    String img = data.getElementsByTag("img").attr("src");
                    img = img.substring(0, img.indexOf("?")) + "?param=280y280";
                    Elements a = data.getElementsByTag("a");
                    String title = a.attr("title");
                    String href = a.attr("href");
                    Log.e("tag", "tag: " + a.toString());
                    beans.add(new AusleseSongListBean(title, img, href));
                }
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<AusleseSongListBean> getXiamiSongList(int offset) {
        Request request = new Request.Builder()
                .url("http://www.xiami.com/collect/recommend/page/" + (offset + 1))
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .build();
        Log.e("DataUtils", "11");
        try {
            Log.e("DataUtils", "22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            Document html = Jsoup.parse(string);
            Elements elementsByClass = html.getElementsByClass("block_list clearfix");
            ArrayList<AusleseSongListBean> beans = new ArrayList<>();
            for (Element link : elementsByClass) {
                Log.e("DataUtils", "33");
                Elements datas = link.getElementsByClass("block_items clearfix");
                for (Element data : datas) {
                    Log.e("DataUtils", "44");
                    Element tag = data.getElementsByTag("a").get(0);
                    String img = tag.getElementsByTag("img").attr("src");
                    img = img.substring(0, img.indexOf("@"));
                    String title = tag.attr("title");
                    String href = tag.attr("href");
                    Log.e("tag", "tag: " + tag.toString());
                    beans.add(new AusleseSongListBean(title, img, href));
                }
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<AusleseSongListBean> getQQSongList(int offset) {
        //        https://y.qq.com/portal/playlist.html#t3=1&
        //        http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?categoryId=10000000&sortId=1&sin=0&ein=49&format=jsonp&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=MusicJsonCallback&needNewCode=0
        //        Connection: keep-alive
        //        Accept: application/json, text/plain, */*
        //        Accept-Encoding: gzip, deflate
        //        Accept-Language: zh-CN
        //        Referer: http://y.qq.com/
        //        Host: i.y.qq.com

        Request.Builder builder = new Request.Builder()
                .url("http://i.y.qq.com/s.plcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?categoryId=10000000&sortId=" + (offset + 1) + "&sin=0&ein=49&format=jsonp&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=MusicJsonCallback&needNewCode=0")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36");
        builder.addHeader("Connection", "keep-alive");
        builder.addHeader("Accept", "application/json, text/plain, */*");
        //        builder.addHeader("Accept-Encoding", "gzip, deflate");
        builder.addHeader("Accept-Language", "zh-CN");
        builder.addHeader("Referer", "http://y.qq.com/");
        builder.addHeader("Host", "i.y.qq.com");
        Request request = builder.build();
        Log.e("DataUtils", "11");
        try {
            Log.e("DataUtils", "22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            int dataLen = string.length();
            string = string.substring(18, dataLen - 1);
            Log.e("DataUtils", "2222 request: " + string);
            ArrayList<AusleseSongListBean> beans = new ArrayList<>();
            //            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jsondata.json");
            //            fos.write(string.getBytes());
            //            fos.flush();
            //            fos.close();
            JSONObject jsonObject = JSON.parseObject(string);
            JSONArray list = jsonObject.getJSONObject("data").getJSONArray("list");
            int size = list.size();
            for (int i = 0; i < size; i++) {
                JSONObject data = list.getJSONObject(i);
                AusleseSongListBean bean = new AusleseSongListBean(data.getString("dissname"), data.getString("imgurl"), data.getString("dissid"));
                beans.add(bean);
            }
            Log.e("DataUtils", "3333");
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<SongBean> getWangyiSongListData(String hrefId) {
        Request request = new Request.Builder()
                .url("http://music.163.com" + hrefId)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .build();
        Log.e("DataUtils", "getWangyiSongListData 11");
        try {
            Log.e("DataUtils", "getWangyiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            Document html = Jsoup.parse(string);
            Element jsonElement = html.getElementById("song-list-pre-cache").getElementsByTag("textarea").get(0);
            JSONArray jsonArr = JSON.parseArray(jsonElement.ownText());
            int size = jsonArr.size();
            ArrayList<SongBean> beans = new ArrayList<>();
            for (int i = 0; i <size; i++) {
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                String songName = jsonObject.getString("name");
                String songId = jsonObject.getInteger("id")+"";
                JSONObject artists = jsonObject.getJSONArray("artists").getJSONObject(0);
                String singerName = artists.getString("name");
                String singerId = artists.getInteger("id")+"";
                beans.add(new SongBean(singerName, singerId, songName, songId));
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getWangyiSongListData 55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<SongBean> getXiamiSongListData(String hrefId) {
        Request request = new Request.Builder()
                //                .url("http://api.xiami.com/web?v=2.0&app_key=1&id="+hrefId+"&callback=jsonp122&r=collect/detail")
                .url("http://www.xiami.com" + hrefId)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                //                .addHeader("Cookie", "_unsign_token=952150b1a1fc5bf061e45a813535d019; _xiamitoken=b0e08dc541ac1dc0b85ed7f85ea7e119")
                .build();
        Log.e("DataUtils", "getXiamiSongListData 11");
        try {
            Log.e("DataUtils", "getXiamiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            Document parse = Jsoup.parse(string);
            Log.e("DataUtils", "getXiamiSongListData 33");
            Elements song_name = parse.getElementsByClass("s_info");
            Log.e("DataUtils", "getXiamiSongListData 44");
            ArrayList<SongBean> beans = new ArrayList<>();
            for (Element element : song_name) {
                String songId = element.child(0).child(0).attr("value");
                String songName = element.getElementsByClass("song_name").get(0).toString().split("</a>")[0].split(">")[2];

                Log.e("DataUtils", "getXiamiSongListData 55 songId: " + songId);
                //                Element element1 = element.getAllElements().get(0);
                //                Element element2 = element.getAllElements().get(0);
                //                Log.e("DataUtils", "55 element1: "+element1.toString());
                //                Log.e("DataUtils", "55 element2: "+element2.toString());
                //                Log.e("DataUtils", "55 element all: "+element.toString());

                SongBean bean = new SongBean(null, null, songName, songId);
                beans.add(bean);
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getXiamiSongListData 55: " + e.toString());
            return null;
        }
    }


    public static ArrayList<SongBean> getQQSongListData(String href) {
        //        http://i.y.qq.com/qzone-music/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&jsonpCallback=jsonCallback&nosign=1&disstid=1471596738&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0
        Request.Builder builder = new Request.Builder()
                .url("http://i.y.qq.com/qzone-music/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&jsonpCallback=jsonCallback&nosign=1&disstid=" + href + "&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36");
        builder.addHeader("Connection", "keep-alive");
        builder.addHeader("Accept", "application/json, text/plain, */*");
        builder.addHeader("Accept-Language", "zh-CN");
        builder.addHeader("Referer", "http://y.qq.com/");
        builder.addHeader("Host", "i.y.qq.com");
        Request request = builder.build();
        Log.e("DataUtils", "11");
        try {
            Log.e("DataUtils", "22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            int dataLen = string.length();
            string = string.substring(13, dataLen - 1);
            Log.e("DataUtils", "2222 request: " + string);
            ArrayList<SongBean> beans = new ArrayList<>();
            //            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jsondata.json");
            //            fos.write(string.getBytes());
            //            fos.flush();
            //            fos.close();
            JSONObject jsonObject = JSON.parseObject(string);
            JSONArray list = jsonObject.getJSONArray("cdlist").getJSONObject(0).getJSONArray("songlist");
            int size = list.size();
            for (int i = 0; i < size; i++) {
                JSONObject data = list.getJSONObject(i);
                SongBean bean = new SongBean(null, null, data.getString("songname"), data.getString("songmid"));
                beans.add(bean);
            }
            Log.e("DataUtils", "3333");
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "55: " + e.toString());
            return null;
        }
    }


    public static ArrayList<SongBean> getWangyiSongData(String hrefId) {
        //        'params': aes,
        //        'encSecKey': rsa
        String s2 = WangYiCipher.creatScrectKey(16);
        //        String s2 = "597c67dd956c869c";
        FormBody body = new FormBody.Builder()
                .add("params", WangYiCipher.aesEncrytion(hrefId, s2))
                .add("encSecKey", WangYiCipher.rsaEncrytion(s2))
                .build();
        Request request = new Request.Builder()
                .url("http://music.163.com/weapi/song/enhance/player/url?csrf_token=")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();
        Log.e("DataUtils", "getWangyiSongListData 11");
        try {
            Log.e("DataUtils", "getWangyiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String s = response.toString();
            Log.e("DataUtils", "getWangyiSongListData response: " + s);
            Headers headers = response.headers();
            String s1 = headers.toString();
            Log.e("DataUtils", "getWangyiSongListData headers: " + s1);
            String string = response.body().string();
            Log.e("DataUtils", "getWangyiSongListData Song: " + string);
            //            Document html = Jsoup.parse(string);
            //            Elements elementsByTag = html.getElementsByClass("f-hide");
            //            ArrayList<SongBean> beans = new ArrayList<>();
            //            for (Element tag : elementsByTag) {
            //                Elements txt = tag.getElementsByTag("a");
            //                for (Element data : txt) {
            //                    String title = data.ownText();
            //                    String href = data.attr("href");
            //                    beans.add(new SongBean(null, null, title, href));
            //                }
            //            }
            //            Log.e("beans", "beans: " + beans.size());
            //            return beans;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getWangyiSongListData 55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<SongBean> getXiamiSongData(String hrefId) {
        Request request = new Request.Builder()
                .url("http://www.xiami.com/song/playlist/id/" + hrefId + "/object_name/default/object_id/0/cat/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Log.e("DataUtils", "getWangyiSongListData 11");
        try {
            Log.e("DataUtils", "getWangyiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            JSONObject data = JSON.parseObject(string).getJSONObject("data");
            JSONObject trackList = data.getJSONArray("trackList").getJSONObject(0);
            String location = trackList.getString("location");
            String result = parseXianiSongUrl(location);
            //            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath() + "/jsondata.json");
            //            fos.write(string.getBytes());
            //            fos.flush();
            //            fos.close();
            Log.e("DataUtils", "getWangyiSongListData Song result: " + result);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getWangyiSongListData 55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<SongBean> getQQSongData(String hrefId) {
        Request request = new Request.Builder()
                .url("http://base.music.qq.com/fcgi-bin/fcg_musicexpress.fcg?json=3&guid=780782017&g_tk=938407465&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=GB2312&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Log.e("DataUtils", "getQQSongData 11");
        try {
            Log.e("DataUtils", "getQQSongData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            string = string.substring(13, string.length() - 2);
            String key = JSON.parseObject(string).getString("key");
            Log.e("DataUtils", "getQQSongData 33: " + "http://cc.stream.qqmusic.qq.com/C200" + hrefId + ".m4a?vkey=" + key + "&fromtag=0&guid=780782017");
            //            Request requestMp3 = new Request.Builder()
            //                    .url("http://cc.stream.qqmusic.qq.com/C200"+hrefId+".m4a?vkey="+key+"&fromtag=0&guid=780782017")
            //                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
            //                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
            //                    .build();
            //            Response responseMp3 = new OkHttpClient().newCall(requestMp3).execute();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getQQSongData 55: " + e.toString());
            return null;
        }
    }

    public static String parseXianiSongUrl(String data) {
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

        if (result != null && result.size() > 0) {
            //        ''.join([''.join([result[j][i] for j in range(num)])for i in range(avg_len)])
            String without = "";
            for (int i = 0; i < avgLen; i++) {
                char[] inner = new char[num];
                for (int j = 0; j < num; j++) {
                    inner[j] = result.get(j).charAt(i);
                }
                without += String.valueOf(inner);
            }
            //            ''.join([result[r][-1] for r in range(remainder)])
            char[] lastChars = new char[remainder];
            for (int i = 0; i < remainder; i++) {
                lastChars[i] = result.get(i).charAt(result.get(i).length() - 1);
            }
            try {
                String finalData = without + String.valueOf(lastChars);
                finalData = finalData.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                finalData = finalData.replaceAll("\\+", "%2B");
                String encode = URLDecoder.decode(finalData, "UTF-8").replace('^', '0');
                Log.e("DataUtils", "encode: " + encode);
                return encode;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DataUtils", "Exception: " + e.toString());
            }
        }
        return null;

    }

}