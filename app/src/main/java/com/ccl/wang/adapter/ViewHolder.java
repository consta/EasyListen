package com.ccl.wang.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class ViewHolder<TAG> extends RecyclerView.ViewHolder {
    private Context mContext;
    private SparseArray<View> mViews;
    private View mItemView;
    private TAG mTag;

    public ViewHolder(View itemView, Context context) {
        super(itemView);
        mItemView = itemView;
        mContext = context;
        mViews = new SparseArray<>();
    }

    public void setTag(TAG tag){
        mTag = tag;
    }

    public TAG getTag(){
        return mTag;
    }

    public View getRootView() {
        return mItemView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

}