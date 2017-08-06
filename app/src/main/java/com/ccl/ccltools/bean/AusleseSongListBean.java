package com.ccl.ccltools.bean;

import android.util.Log;

/**
 * Created by wang on 2017/8/6.
 */

public class AusleseSongListBean {
    public String title;
    public String imgUrl;
    public String href;

    public AusleseSongListBean(String t, String i, String href){
        title = t;
        imgUrl = i;
        this.href = href;
        Log.e("Bean", "Bean: "+toString());
    }

    @Override
    public String toString() {
        return "AusleseSongListBean{" +
                "title='" + title + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", href='" + href + '\'' +
                '}';
    }
}
