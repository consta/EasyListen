package com.ccl.wang.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccl.wang.R;
import com.ccl.wang.asynctask.SongListTask;
import com.ccl.wang.bean.SongList;
import com.ccl.wang.fragment.SongListFragment;
import com.ccl.wang.other.GlideApp;
import com.ccl.wang.utils.UIUtils;

import java.util.List;


public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.MyViewHolder> {

    private Context mContext;
    private List<SongList> mDatas;
    private boolean mLoading = true;
    private int mItemHeight;
    private View.OnClickListener mOnItemClickListener;

    public SongListAdapter(Context context) {
        mContext = context;
        Resources resources = mContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        mItemHeight = dm.widthPixels / 2 - UIUtils.dp2px(12);
    }

    public void setDatas(List<SongList> data) {
        mDatas = data;
        mLoading = false;
    }

    public void addDatas(List<SongList> data) {
        if (mDatas != null) {
            mDatas.addAll(data);
        } else {
            mDatas = data;
        }
        mLoading = false;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_auslese_view, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SongList ausleseSongListBean = mDatas.get(position);
        holder.title.setText(ausleseSongListBean.title);
        holder.img.setTag(R.id.songlist_img_tag, ausleseSongListBean);
        GlideApp.with(mContext)
                .load(ausleseSongListBean.imgUrl)
                .placeholder(R.mipmap.loading_spinner)
                .into(holder.img);

        if (!mLoading && position >= mDatas.size() - 10) {
            mLoading = true;
            new SongListTask(this, mContext, null).execute(SongListFragment.mCurrentDataType);
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null) {
            return mDatas.size();
        }
        return 0;
    }

    public void setOnItemClickListener(View.OnClickListener l) {
        mOnItemClickListener = l;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView img;

        public MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(mOnItemClickListener);
            title = (TextView) view.findViewById(R.id.tv_auslese_title);
            img = (ImageView) view.findViewById(R.id.iv_auslese_img);
            ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
            layoutParams.height = mItemHeight;
        }

    }
}
