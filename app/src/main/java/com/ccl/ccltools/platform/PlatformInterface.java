package com.ccl.ccltools.platform;


import com.ccl.ccltools.bean.Playlist;
import com.ccl.ccltools.bean.Song;

import java.util.List;

public interface PlatformInterface {
    public static final int netease = 0;
    public static final int xiami = 0;
    public static final int qq = 0;

    public List<Playlist> getPlaylist(int offset);

    public List<Song> getSongList(String playlistId);

}
