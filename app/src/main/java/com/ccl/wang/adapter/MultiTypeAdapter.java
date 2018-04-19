package com.ccl.wang.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class MultiTypeAdapter<T, TAG> extends NormalAdapter<T, TAG> {

    public MultiTypeAdapter(Context context, List<T> datas) {
        super(context, -1, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return buildViewHolder(getItemView(viewType, parent));
    }

    @Override
    public int getItemViewType(int position) {
        return getItemType(position, mDatas.get(position));
    }

    public abstract View getItemView(int itemType, ViewGroup parent);

    public abstract int getItemType(int position, T item);

}
