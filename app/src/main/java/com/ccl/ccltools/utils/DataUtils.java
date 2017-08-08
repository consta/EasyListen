package com.ccl.ccltools.utils;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccl.ccltools.bean.AusleseSongListBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

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

    public static ArrayList<AusleseSongListBean> getSongListData(int type, String href) {
        ArrayList<AusleseSongListBean> data = null;
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

    public static ArrayList<AusleseSongListBean> getWangyiSongListData(String hrefId) {
        Request request = new Request.Builder()
                .url("http://music.163.com"+hrefId)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .build();
        Log.e("DataUtils", "getWangyiSongListData 11");
        try {
            Log.e("DataUtils", "getWangyiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            Document html = Jsoup.parse(string);
            Elements elementsByTag = html.getElementsByClass("f-hide");
            ArrayList<AusleseSongListBean> beans = new ArrayList<>();
            for (Element tag : elementsByTag) {
                Elements txt = tag.getElementsByTag("a");
                for (Element data : txt) {
                    String title = data.ownText();
                    String href = data.attr("href");
                beans.add(new AusleseSongListBean(title, null, href));
                }
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getWangyiSongListData 55: " + e.toString());
            return null;
        }
    }

    public static ArrayList<AusleseSongListBean> getXiamiSongListData(String hrefId) {
        Request request = new Request.Builder()
//                .url("http://api.xiami.com/web?v=2.0&app_key=1&id="+hrefId+"&callback=jsonp122&r=collect/detail")
                .url("http://www.xiami.com" + hrefId)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .addHeader("Cookie", "_unsign_token=952150b1a1fc5bf061e45a813535d019; _xiamitoken=b0e08dc541ac1dc0b85ed7f85ea7e119")
                .build();
        Log.e("DataUtils", "getXiamiSongListData 11");
        try {
            Log.e("DataUtils", "getXiamiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            Document parse = Jsoup.parse(string);
            Log.e("DataUtils", "getXiamiSongListData 33");
            Elements song_name = parse.getElementsByClass("song_name");
            Log.e("DataUtils", "getXiamiSongListData 44");
            ArrayList<AusleseSongListBean> beans = new ArrayList<>();
            for (Element element : song_name) {
                String songName = element.toString().split("</a>")[0].split(">")[2];

                Log.e("DataUtils", "getXiamiSongListData 55");
//                Element element1 = element.getAllElements().get(0);
//                Element element2 = element.getAllElements().get(0);
//                Log.e("DataUtils", "55 element1: "+element1.toString());
//                Log.e("DataUtils", "55 element2: "+element2.toString());
//                Log.e("DataUtils", "55 element all: "+element.toString());

                AusleseSongListBean bean = new AusleseSongListBean(songName, null, null);
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

    public static ArrayList<AusleseSongListBean> getQQSongListData(String href) {
//        http://i.y.qq.com/qzone-music/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&jsonpCallback=jsonCallback&nosign=1&disstid=1471596738&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0
        Request.Builder builder = new Request.Builder()
                .url("http://i.y.qq.com/qzone-music/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&jsonpCallback=jsonCallback&nosign=1&disstid="+href+"&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0")
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
            ArrayList<AusleseSongListBean> beans = new ArrayList<>();
            //            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jsondata.json");
            //            fos.write(string.getBytes());
            //            fos.flush();
            //            fos.close();
            JSONObject jsonObject = JSON.parseObject(string);
            JSONArray list = jsonObject.getJSONArray("cdlist").getJSONObject(0).getJSONArray("songlist");
            int size = list.size();
            for (int i = 0; i < size; i++) {
                JSONObject data = list.getJSONObject(i);
                AusleseSongListBean bean = new AusleseSongListBean(data.getString("songname"), null, null);
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
}