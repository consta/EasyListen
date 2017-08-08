package com.ccl.ccltools.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.Toast;

import com.ccl.ccltools.adapter.AusleseSongListAdapter;
import com.ccl.ccltools.adapter.SongListDataAdapter;
import com.ccl.ccltools.bean.AusleseSongListBean;
import com.ccl.ccltools.bean.SongBean;
import com.ccl.ccltools.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2017/8/6.
 */

public class SongListDataTask extends AsyncTask<Void, Void, List<SongBean>> {
    private SongListDataAdapter mAdapter;
    private Context mContext;
    private int mType;
    private String mHref;

    public SongListDataTask(SongListDataAdapter adapter, Context context, int type, String href) {
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
    protected List<SongBean> doInBackground(Void... params) {
        ArrayList<SongBean> ausleseSongList = DataUtils.getSongListData(mType, mHref);
        return ausleseSongList;
    }

    @Override
    protected void onPostExecute(List<SongBean> list) {
        super.onPostExecute(list);
        if (list != null && list.size() > 0) {
            mAdapter.setDatas(list);
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
        }
    }
}
