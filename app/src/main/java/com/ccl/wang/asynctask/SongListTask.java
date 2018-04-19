package com.ccl.wang.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.ccl.wang.adapter.SongListAdapter;
import com.ccl.wang.bean.SongList;
import com.ccl.wang.fragment.SongListFragment;
import com.ccl.wang.platform.Platform;

import java.util.List;

/**
 * Created by wang on 2017/8/6.
 */

public class SongListTask extends AsyncTask<Integer, Void, List<SongList>> {
    private SongListAdapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout mRefreshView;
    private int mCurrentDataType;
    public static int LOAD_OFFSET = 0;

    public SongListTask(SongListAdapter adapter, Context context, SwipeRefreshLayout refreshView) {
        mAdapter = adapter;
        mContext = context;
        mRefreshView = refreshView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<SongList> doInBackground(Integer... params) {
        if (params != null && params.length > 0) {
            mCurrentDataType = params[0];
        }

        List<SongList> songList = Platform.setPlatform(mCurrentDataType).getSonglist(LOAD_OFFSET);
        return songList;
    }

    @Override
    protected void onPostExecute(List<SongList> list) {
        super.onPostExecute(list);
        if (mCurrentDataType == SongListFragment.mCurrentDataType) {

            if (mRefreshView != null) {
                mRefreshView.setRefreshing(false);
            }
            if (list != null && list.size() > 0) {
                if (LOAD_OFFSET == 0) {
                    mAdapter.setDatas(list);
                } else {
                    mAdapter.addDatas(list);
                }
                LOAD_OFFSET++;
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
