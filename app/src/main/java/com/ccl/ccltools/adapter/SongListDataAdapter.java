package com.ccl.ccltools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ccl.ccltools.R;
import com.ccl.ccltools.bean.SongBean;
import com.ccl.ccltools.fragment.AusleseSongListFragment;
import com.ccl.ccltools.utils.DataUtils;

import java.util.List;


public class SongListDataAdapter extends RecyclerView.Adapter<SongListDataAdapter.MyViewHolder> {

    private Context mContext;
    private List<SongBean> mDatas;
    private View.OnClickListener mOnItemClickListener;

    public SongListDataAdapter(Context context) {
        mContext = context;
    }

    public void setDatas(List<SongBean> data){
        mDatas = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_song_list_data, parent, false);
        return new MyViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SongBean bean = mDatas.get(position);
        holder.index.setText((position+1)+"");
        holder.title.setText(bean.songName);
        holder.title.setTag(R.id.songdata_tag, bean.songId);
    }

    @Override
    public int getItemCount() {
        if(mDatas != null){
            return mDatas.size();
        }
        return 0;
    }

    public void setOnItemClickListener(View.OnClickListener l){
        mOnItemClickListener = l;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView index;
        TextView title;

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_songlist_data_title);
            index = (TextView) view.findViewById(R.id.tv_songlist_data_index);
            title.setOnClickListener(mOnClickListener);
        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String tag = (String) v.getTag(R.id.songdata_tag);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    DataUtils.getSongData(AusleseSongListFragment.mCurrentDataType, tag);
                }
            }).start();
        }
    };
}
