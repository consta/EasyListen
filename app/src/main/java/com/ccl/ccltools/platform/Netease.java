package com.ccl.ccltools.platform;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccl.ccltools.bean.ListSong;
import com.ccl.ccltools.bean.SongList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Netease extends Platform {

    static Netease mInstance;

    static Netease getInstance(){
        if(mInstance == null){
            mInstance = new Netease();
        }
        return mInstance;
    }

    Netease(){

    }

    @Override
    public List<SongList> getSonglist(int offset) {
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
            ArrayList<SongList> beans = new ArrayList<>();
            for (Element link : elementsByClass) {
                Log.e("DataUtils", "33");
                Elements datas = link.getElementsByClass("u-cover u-cover-1");
                for (Element data : datas) {
                    Log.e("DataUtils", "44");
                    String img = data.getElementsByTag("img").attr("src");
                    img = img.substring(0, img.indexOf("?")) + "?param=280y280";
                    Elements a = data.getElementsByTag("a");
                    String title = a.attr("title");
                    String href = a.attr("href").split("=")[1];
                    Log.e("tag", "tag: " + a.toString());
                    beans.add(new SongList(title, img, href));
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

    @Override
    public List<ListSong> getListSong(String songlist) {
        Request request = new Request.Builder()
                .url("http://music.163.com/api/playlist/detail?id=" + songlist)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .build();
        Log.e("DataUtils", "getWangyiSongListData 11");
        try {
            Log.e("DataUtils", "getWangyiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            //            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/neteaseList.json");
            //            fos.write(string.getBytes());
            //            fos.flush();
            //            fos.close();
            JSONArray jsonArr = JSON.parseObject(string).getJSONObject("result").getJSONArray("tracks");
            int size = jsonArr.size();
            ArrayList<ListSong> beans = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                String songName = jsonObject.getString("name");
                String songId = jsonObject.getInteger("id") + "";
                JSONObject artists = jsonObject.getJSONArray("artists").getJSONObject(0);
                String singerName = artists.getString("name");
                String singerId = artists.getInteger("id") + "";
                beans.add(new ListSong(singerName, singerId, songName, songId));
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "getWangyiSongListData 55: " + e.toString());
            return null;
        }
    }
}
