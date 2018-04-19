package com.ccl.wang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getViewId(), container, false);
        init();
        initView();
        initData();
        initListener();
        return mView;
    }

    protected abstract int getViewId();

    protected View findViewById(int id){
        return mView.findViewById(id);
    }

    protected void init() {

    }

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initListener() {

    }
}
