package com.ccl.ccltools.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.ccl.ccltools.adapter.SongListDataAdapter;
import com.ccl.ccltools.bean.ListSong;
import com.ccl.ccltools.platform.Platform;

import java.util.List;

/**
 * Created by wang on 2017/8/6.
 */

public class ListSongTask extends AsyncTask<Void, Void, List<ListSong>> {
    private SongListDataAdapter mAdapter;
    private Context mContext;
    private int mType;
    private String mHref;

    public ListSongTask(SongListDataAdapter adapter, Context context, int type, String href) {
        mAdapter = adapter;
        mContext = context;
        mType = type;
        mHref = href;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ListSong> doInBackground(Void... params) {
        List<ListSong> ausleseSongList = Platform.setPlatform(mType).getListSong(mHref);
        return ausleseSongList;
    }

    @Override
    protected void onPostExecute(List<ListSong> list) {
        super.onPostExecute(list);
        if (list != null && list.size() > 0) {
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
        }
    }
}
