package com.example.yesq.pheniebook;

import android.content.Context;

import java.util.Collection;

/**
 * Created by Jacqueline on 2018/3/7.
 */

public class AppointAdapter extends BaseRecyclerAdapter<Appoint>{

    public AppointAdapter(Context context, IMutlipleItem<Appoint> items, Collection<Appoint> datas) {
        super(context, items, datas);
    }
    @Override
    public void bindView(final BaseRecyclerHolder holder, final Appoint add, int position) {
        if(add !=null && add.getApp_time().toString().length() != 0 && add.getType().length() != 0
                && add.getUser() != null && add.getUser().getUsername().length() != 0 && add.getContent().length() != 0){
            holder.setText(R.id.appoint_text_date, add == null ? "未知" : add.getApp_time().toString());
            holder.setText(R.id.appoint_text_type, add == null ? "未知" : add.getType());
            holder.setText(R.id.appoint_text_host, add == null ? "未知" : add.getUser().getUsername());
            holder.setText(R.id.appoint_text_request, add == null ? "未知" : add.getContent());
        }


    }
}
