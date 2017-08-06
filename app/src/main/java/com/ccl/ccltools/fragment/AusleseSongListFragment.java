package com.ccl.ccltools.fragment;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ccl.ccltools.R;
import com.ccl.ccltools.adapter.AusleseSongListAdapter;
import com.ccl.ccltools.asynctask.AusleseSongListTask;
import com.ccl.ccltools.utils.DataUtils;
import com.github.clans.fab.FloatingActionMenu;

public class AusleseSongListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private AusleseSongListAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private GridLayoutManager mGridLayoutManager;
    private FloatingActionMenu mFabMenu;
    private com.github.clans.fab.FloatingActionButton mFabWangyi;
    private com.github.clans.fab.FloatingActionButton mFabXiami;
    private com.github.clans.fab.FloatingActionButton mFabQQ;
    public static int mCurrentDataType = DataUtils.DATA_WANGYI;

    @Override
    protected int getViewId() {
        return R.layout.fragment_auslese_songlist_layout;
    }

    @Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recuclerview_auslese_songlist);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mFabMenu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        mFabWangyi = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_wangyi);
        mFabXiami = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_xiami);
        mFabQQ = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_qq);
    }

    @Override
    protected void initData() {
        mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mAdapter = new AusleseSongListAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mFabMenu.setClosedOnTouchOutside(true);
    }

    private View.OnClickListener mFabItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            int dataType = -1;
            switch (id){
                case R.id.fab_wangyi:
                    dataType = DataUtils.DATA_WANGYI;
                    break;
                case R.id.fab_xiami:
                    dataType = DataUtils.DATA_XIAMI;
                    break;
                case R.id.fab_qq:
                    dataType = DataUtils.DATA_QQ;
                    break;
            }
            mFabMenu.toggle(true);
            if(dataType != -1 && dataType != mCurrentDataType){
                mCurrentDataType = dataType;
                reGetData();
            }
        }
    };

    @Override
    protected void initListener() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                AusleseSongListTask.LOAD_OFFSET = 0;
                new AusleseSongListTask(mAdapter, AusleseSongListFragment.this.getContext(), mRefreshLayout).execute(mCurrentDataType);
            }
        });

        mFabMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if(opened){
                    mFabMenu.getMenuIconView().setImageResource(R.mipmap.ic_fab_close);
                }else{
                    mFabMenu.getMenuIconView().setImageResource(R.mipmap.ic_fab_open);
                }
            }
        });

        mFabWangyi.setOnClickListener(mFabItemClick);
        mFabXiami.setOnClickListener(mFabItemClick);
        mFabQQ.setOnClickListener(mFabItemClick);
    }

    @Override
    public void onStart() {
        super.onStart();
        reGetData();
    }

    private void reGetData(){
        mRefreshLayout.setRefreshing(true);
        AusleseSongListTask.LOAD_OFFSET = 0;
        new AusleseSongListTask(mAdapter, this.getContext(), mRefreshLayout).execute(mCurrentDataType);
    }
}