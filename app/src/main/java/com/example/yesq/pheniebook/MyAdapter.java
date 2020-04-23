package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yesq on 2017/6/23.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    public String[] datas;
    public MyAdapter(String[] mdatas){
        datas=mdatas;
        }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view,parent.getContext());
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv_sta.setText(datas[position]);
        holder.tv_crtid.setText(datas[position]);
        holder.test.setText("成功！！！");

    }

    @Override
    public int getItemCount() {
        return datas.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tv_crtid,tv_sta,test;
        public Context context;

        public ViewHolder(View itemView,Context context) {
            super(itemView);
            tv_sta = (TextView) itemView.findViewById(R.id.text_sta);
            tv_crtid = (TextView) itemView.findViewById(R.id.text_crtid);
            this.context=context;
            itemView.setOnClickListener(this);
            }
        @Override
        public void onClick(View v) {
//            Intent bookIntent = new Intent(PeekActivity.class, BookActivity.class);
//            bookIntent.putExtra("key_courtType", "乒乓球");
//            startActivityForResult(bookIntent, 1);

//            Toast.makeText(context,mTextView.getText().toString(), Toast.LENGTH_SHORT).show();

        }


    }
}
