package com.ccl.ccltools.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ccl.ccltools.R;

public abstract class BaseActivity extends AppCompatActivity {

    private CoordinatorLayout mRootView;
    private LinearLayout mContentContainer;
    private View mContentView;
    public View mPlayView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(getBaseContext(), R.layout.activity_base_layout, null);
        mRootView = (CoordinatorLayout) (view.findViewById(R.id.root_view));
        setContentView(view);
        initLayout();
        init();
        initView();
        initData();
        initListener();
    }

    protected void initAppLayout() {
    }

    protected void init() {
    }

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();

    private void initLayout() {
        View appLayout = getAppLayout();
        if (appLayout != null) {
            mRootView.addView(appLayout, 0);
            initAppLayout();
        }
        mContentContainer = (LinearLayout) findViewById(R.id.ll_content);
        mPlayView = findViewById(R.id.play_view);
        mContentView = getContentView();
        if (mContentView != null) {
            mContentContainer.addView(mContentView);
        }
    }

    private View getAppLayout() {
        int appLayoutId = getAppLayoutId();
        if (appLayoutId > 0) {
            return LayoutInflater.from(getBaseContext()).inflate(appLayoutId, mRootView, false);
        }
        return null;
    }

    private View getContentView() {
        int appLayoutId = getContentId();
        if (appLayoutId != 0) {
            return LayoutInflater.from(getBaseContext()).inflate(appLayoutId, mContentContainer, false);
        }
        return null;
    }

    protected abstract int getAppLayoutId();

    protected abstract int getContentId();

}
