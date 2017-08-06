package com.ccl.ccltools.asynctask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.ccl.ccltools.adapter.AusleseSongListAdapter;
import com.ccl.ccltools.bean.AusleseSongListBean;
import com.ccl.ccltools.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2017/8/6.
 */

public class AusleseSongListTask extends AsyncTask<Integer, Void, List<AusleseSongListBean>> {
    private AusleseSongListAdapter mAdapter;
    private Context mContext;
    private SwipeRefreshLayout mRefreshView;
    private int mCurrentDataType;
    public static int LOAD_OFFSET = 0;

    public AusleseSongListTask(AusleseSongListAdapter adapter, Context context, SwipeRefreshLayout refreshView) {
        mAdapter = adapter;
        mContext = context;
        mRefreshView = refreshView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<AusleseSongListBean> doInBackground(Integer... params) {
        if(params != null && params.length > 0){
            mCurrentDataType = params[0];
        }
        ArrayList<AusleseSongListBean> ausleseSongList = DataUtils.getSongListData(mCurrentDataType, LOAD_OFFSET);
        return ausleseSongList;
    }

    @Override
    protected void onPostExecute(List<AusleseSongListBean> list) {
        super.onPostExecute(list);
        if (mRefreshView != null) {
            mRefreshView.setRefreshing(false);
        }
        if (list != null && list.size() > 0) {
            if (LOAD_OFFSET == 0) {
                mAdapter.setDatas(list);
            }else{
                mAdapter.addDatas(list);
            }
            LOAD_OFFSET++;
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(mContext, "加载失败", Toast.LENGTH_SHORT).show();
        }
    }
}
