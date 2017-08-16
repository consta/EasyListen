package com.ccl.ccltools.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.song.setText(bean.songName);
        holder.singer.setText(bean.singerName);
        holder.rootViwe.setTag(R.id.songdata_tag, bean);
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
        TextView song;
        TextView singer;
        ImageView more;
        View rootViwe;

        public MyViewHolder(View view)
        {
            super(view);
            rootViwe = view;
            song = (TextView) view.findViewById(R.id.tv_songlist_data_song);
            index = (TextView) view.findViewById(R.id.tv_songlist_data_index);
            singer = (TextView) view.findViewById(R.id.tv_songlist_data_singer);
            more = (ImageView) view.findViewById(R.id.iv_songlost_more);
            rootViwe.setOnClickListener(mOnClickListener);
            more.setOnClickListener(mOnClickListener);
        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rl_songlist_item_view:
                    final SongBean tag = (SongBean) v.getTag(R.id.songdata_tag);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DataUtils.getSongData(AusleseSongListFragment.mCurrentDataType, tag.songId);
                        }
                    }).start();
                    break;
                case R.id.iv_songlost_more:
//                    v.getParent()
                    break;
            }
        }
    };
}
