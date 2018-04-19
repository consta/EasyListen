package com.ccl.wang.platform;


import com.ccl.wang.bean.ListSong;
import com.ccl.wang.bean.SongList;

import java.util.List;

public interface PlatformInterface {
    public static final int PLATFORM_NETEASE = 0;
    public static final int PLATFORM_XIAMI = 1;
    public static final int PLATFORM_QQ = 2;

    /**
     * 获取歌单
     * @param offset
     * @return
     */
    List<SongList> getSonglist(int offset);

    /**
     * 获取歌曲列表
     * @param songlist
     * @return
     */
    List<ListSong> getListSong(String songlist);


    
}
