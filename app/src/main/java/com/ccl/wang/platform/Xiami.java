package com.ccl.wang.platform;


import android.util.Log;

import com.ccl.wang.bean.ListSong;
import com.ccl.wang.bean.SongList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Xiami extends Platform {
    static Xiami mInstance;

    static Xiami getInstance(){
        if(mInstance == null){
            mInstance = new Xiami();
        }
        return mInstance;
    }

    Xiami(){

    }

    @Override
    public List<SongList> getSonglist(int offset) {
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
            ArrayList<SongList> beans = new ArrayList<>();
            for (Element link : elementsByClass) {
                Log.e("DataUtils", "33");
                Elements datas = link.getElementsByClass("block_items clearfix");
                for (Element data : datas) {
                    Log.e("DataUtils", "44");
                    Element tag = data.getElementsByTag("a").get(0);
                    String img = tag.getElementsByTag("img").attr("src");
                    img = img.substring(0, img.indexOf("@"));
                    String title = tag.attr("title");
                    String href = tag.attr("href").split("/collect/")[1];
                    Log.e("tag", "tag: " + tag.toString());
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
                //                .url("http://api.xiami.com/web?v=2.0&app_key=1&id="+hrefId+"&callback=jsonp122&r=collect/detail")
                .url("http://www.xiami.com/collect/" + songlist)
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
            ArrayList<ListSong> beans = new ArrayList<>();
            for (Element element : song_name) {
                String songId = element.child(0).child(0).attr("value");
                String songName = element.getElementsByClass("song_name").get(0).toString().split("</a>")[0].split(">")[2];

                Log.e("DataUtils", "getXiamiSongListData 55 songId: " + songId);
                //                Element element1 = element.getAllElements().get(0);
                //                Element element2 = element.getAllElements().get(0);
                //                Log.e("DataUtils", "55 element1: "+element1.toString());
                //                Log.e("DataUtils", "55 element2: "+element2.toString());
                //                Log.e("DataUtils", "55 element all: "+element.toString());

                ListSong bean = new ListSong(null, null, songName, songId);
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
}
