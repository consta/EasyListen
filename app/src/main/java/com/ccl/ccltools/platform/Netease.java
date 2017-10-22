package com.ccl.ccltools.platform;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ccl.ccltools.bean.Playlist;
import com.ccl.ccltools.bean.Singer;
import com.ccl.ccltools.bean.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Netease implements PlatformInterface {

    @Override
    public List<Playlist> getPlaylist(int offset) {
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
            ArrayList<Playlist> beans = new ArrayList<>();
            for (Element link : elementsByClass) {
                Log.e("DataUtils", "33");
                Elements datas = link.getElementsByClass("u-cover u-cover-1");
                for (Element data : datas) {
                    Log.e("DataUtils", "44");
                    String img = data.getElementsByTag("img").attr("src");
                    img = img.substring(0, img.indexOf("?")) + "?param=280y280";
                    Elements a = data.getElementsByTag("a");
                    String title = a.attr("title");
                    String id = a.attr("href").split("=")[1];
                    Log.e("tag", "tag: " + a.toString());
                    Playlist playlist = new Playlist();
                    playlist.setId(id);
                    playlist.setImgUrl(img);
                    playlist.setName(title);
                    beans.add(playlist);
                }
            }
            Log.e("beans", "beans: " + beans.size());
            return beans;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DataUtils", "55: " + e.toString());
        }
        return null;
    }

    @Override
    public List<Song> getSongList(String playlistId) {
        Request request = new Request.Builder()
                .url("http://music.163.com/api/playlist/detail?id=" + playlistId)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Listen1/1.2.0 Chrome/49.0.2623.75 Electron/1.0.1 Safari/537.36")
                .build();
        Log.e("DataUtils", "getWangyiSongListData 11");
        try {
            Log.e("DataUtils", "getWangyiSongListData 22");
            Response response = new OkHttpClient().newCall(request).execute();
            String string = response.body().string();
            JSONArray jsonArr = JSON.parseObject(string).getJSONObject("result").getJSONArray("tracks");
            int size = jsonArr.size();
            ArrayList<Song> beans = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                String songName = jsonObject.getString("name");
                String songId = jsonObject.getInteger("id") + "";
                JSONObject artists = jsonObject.getJSONArray("artists").getJSONObject(0);
                String singerName = artists.getString("name");
                String singerId = artists.getInteger("id") + "";
                Song song = new Song();
                song.setId(songId);
                song.setSongName(songName);
                Singer singer = new Singer();
                singer.setId(singerId);
                singer.setSingerName(singerName);
                beans.add(song);
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
