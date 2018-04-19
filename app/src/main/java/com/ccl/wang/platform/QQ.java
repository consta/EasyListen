package com.ccl.wang.platform;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccl.wang.bean.ListSong;
import com.ccl.wang.bean.Singer;
import com.ccl.wang.bean.SongList;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QQ extends Platform {
    static QQ mInstance;

    static QQ getInstance(){
        if(mInstance == null){
            mInstance = new QQ();
        }
        return mInstance;
    }

    QQ(){

    }

    @Override
    public List<SongList> getSonglist(int offset) {
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
            ArrayList<SongList> beans = new ArrayList<>();
            //            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jsondata.json");
            //            fos.write(string.getBytes());
            //            fos.flush();
            //            fos.close();
            JSONObject jsonObject = JSON.parseObject(string);
            JSONArray list = jsonObject.getJSONObject("data").getJSONArray("list");
            int size = list.size();
            for (int i = 0; i < size; i++) {
                JSONObject data = list.getJSONObject(i);
                Log.e("DataUtils", "data: "+data.toString());
                SongList bean = new SongList(data.getString("dissname"), data.getString("imgurl"), data.getString("dissid"));
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

    @Override
    public List<ListSong> getListSong(String songlist) {
        //        http://i.y.qq.com/qzone-music/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&jsonpCallback=jsonCallback&nosign=1&disstid=1471596738&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0
        Request.Builder builder = new Request.Builder()
                .url("http://i.y.qq.com/qzone-music/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?type=1&json=1&utf8=1&onlysong=0&jsonpCallback=jsonCallback&nosign=1&disstid=" + songlist + "&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&jsonpCallback=jsonCallback&needNewCode=0")
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
            ArrayList<ListSong> beans = new ArrayList<>();
            //                        FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/qqList.json");
            //                        fos.write(string.getBytes());
            //                        fos.flush();
            //                        fos.close();
            JSONObject jsonObject = JSON.parseObject(string);
            JSONArray list = jsonObject.getJSONArray("cdlist").getJSONObject(0).getJSONArray("songlist");
            int size = list.size();
            for (int i = 0; i < size; i++) {
                JSONObject data = list.getJSONObject(i);
                JSONArray jsingers = data.getJSONArray("singer");
                List<Singer> singers = null;
                if(jsingers != null && jsingers.size() > 0){
                    singers = new ArrayList<>();
                    int size1 = jsingers.size();
                    for (int j = 0; j < size1; j++) {
                        Singer singer = new Singer();
                        JSONObject jsonObject1 = jsingers.getJSONObject(j);
                        singer.setId(jsonObject1.getString("id"));
                        singer.setSingerName(jsonObject1.getString("name"));
                        singers.add(singer);
                    }
                }
                ListSong bean = new ListSong(null, null, data.getString("songname"), data.getString("songmid"));
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
