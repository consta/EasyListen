package com.ccl.ccltools;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccl.ccltools.adapter.SongListDataAdapter;
import com.ccl.ccltools.asynctask.SongListDataTask;
import com.ccl.ccltools.fragment.AusleseSongListFragment;
import com.ccl.ccltools.other.GlideApp;

public class SongListActivity extends AppCompatActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //内容共享
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
        setContentView(R.layout.activity_song_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.songlist_toolbar);
        setSupportActionBar(toolbar);
        mSupportActionBar = getSupportActionBar();
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        mSupportActionBar.setTitle("歌单");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        mIvTop = (ImageView) findViewById(R.id.iv_songlist_top);
        mTvTitle = (TextView) findViewById(R.id.tv_songlist_title);
        mAppBatLayout = (AppBarLayout) findViewById(R.id.songlist_app_bar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recuclerview_songlist_data);
    }

    private void initData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("title");
        String img = intent.getStringExtra("img");
        mHref = intent.getStringExtra("href");
        mTvTitle.setText(mTitle);
        GlideApp.with(getApplicationContext())
                .load(img)
                .into(mIvTop);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAdapter = new SongListDataAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initListener() {
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
        new SongListDataTask(mAdapter, getApplicationContext(), AusleseSongListFragment.mCurrentDataType, mHref).execute();
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
