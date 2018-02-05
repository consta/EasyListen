package com.ccl.ccltools.bean;


import android.util.Log;

public class ListSong {
    public String singerName;
    public String singerId;
    public String songName;
    public String songId;

    public ListSong(String singerName, String singerId, String songName, String songId) {
        this.singerName = singerName;
        this.singerId = singerId;
        this.songName = songName;
        this.songId = songId;
        Log.e("SongBean", "SongBean: "+toString());
    }

    @Override
    public String toString() {
        return "SongBean{" +
                "singerName='" + singerName + '\'' +
                ", singerId='" + singerId + '\'' +
                ", songName='" + songName + '\'' +
                ", songId='" + songId + '\'' +
                '}';
    }
}
