package com.ccl.ccltools.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccl.ccltools.R;
import com.ccl.ccltools.adapter.SongListDataAdapter;
import com.ccl.ccltools.asynctask.ListSongTask;
import com.ccl.ccltools.fragment.SongListFragment;
import com.ccl.ccltools.other.GlideApp;

public class SongListActivity extends BaseActivity {

    private ActionBar mSupportActionBar;
    private ImageView mIvTop;
    private TextView mTvTitle;
    private AppBarLayout mAppBatLayout;
    private String mTitle;
    private RecyclerView mRecyclerView;
    private SongListDataAdapter mAdapter;
    private String mHref;
    private int mCurrentBarState = BAR_STATE_EXPANDED;
    private static final int BAR_STATE_EXPANDED = 0;
    private static final int BAR_STATE_COLLAPSED = 1;

    @Override
    protected int getAppLayoutId() {
        return R.layout.applayout_songlist;
    }

    @Override
    protected int getContentId() {
        return R.layout.content_song_list;
    }

    @Override
    protected void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.songlist_toolbar);
        setSupportActionBar(toolbar);
        mSupportActionBar = getSupportActionBar();
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        mSupportActionBar.setTitle("歌单");
    }

    @Override
    protected void initView() {
        mIvTop = (ImageView) findViewById(R.id.iv_songlist_top);
        mTvTitle = (TextView) findViewById(R.id.tv_songlist_title);
        mAppBatLayout = (AppBarLayout) findViewById(R.id.songlist_app_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recuclerview_songlist_data);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        String img = intent.getStringExtra("img");
        mHref = intent.getStringExtra("href");
        mTvTitle.setText(mTitle);
        GlideApp.with(getApplicationContext())
                .load(img)
                .into(mIvTop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mAdapter = new SongListDataAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mAppBatLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (mCurrentBarState != BAR_STATE_EXPANDED) {
                        mCurrentBarState = BAR_STATE_EXPANDED;//修改状态标记为展开
                        mSupportActionBar.setTitle("歌单");//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (mCurrentBarState != BAR_STATE_COLLAPSED) {
                        mCurrentBarState = BAR_STATE_COLLAPSED;//修改状态标记为折叠
                        mSupportActionBar.setTitle(mTitle);//设置title不显示
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        new ListSongTask(mAdapter, getApplicationContext(), SongListFragment.mCurrentDataType, mHref).execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
