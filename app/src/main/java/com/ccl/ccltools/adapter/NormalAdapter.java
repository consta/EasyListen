package com.ccl.ccltools.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class NormalAdapter<T, TAG> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;
    private int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private View.OnClickListener mOnClickListener;

    public NormalAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mLayoutId = layoutId;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return buildViewHolder(mInflater.inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBind(holder, mDatas.get(position), position);
    }

    public abstract void onBind(ViewHolder<TAG> holder, T item, int position);

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public ViewHolder buildViewHolder(View view) {
        return new ViewHolder(view, mContext);
    }

    public void setOnClickListener(View.OnClickListener listener){
        mOnClickListener = listener;
    }

}
