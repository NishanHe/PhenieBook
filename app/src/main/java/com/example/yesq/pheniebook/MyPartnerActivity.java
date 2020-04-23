package com.example.yesq.pheniebook;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyPartnerActivity extends AppCompatActivity {
    List<Appoint> mypartnerMsg;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyPartnerActivity.MyAdapter adapter;
    Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_partner);
        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        mypartnerMsg= new ArrayList<Appoint>();
        initData();

        btn_return=(Button)findViewById(R.id.btn_return);
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecorationLi(this,LinearLayoutManager.VERTICAL));
    }
    protected List<Appoint> initData(){

        BmobQuery<Appoint> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", 14846666);
        query.setLimit(50);
        query.findObjects(new FindListener<Appoint>()  {
            @Override
            public void done(List<Appoint> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            mypartnerMsg.add(list.get(i));
                            System.out.println("第"+i+"条获取");
                            Log.i("bmob", "场地编号：" + mypartnerMsg.get(i).getContent());
                            Log.i("bmob", "预约编号：" + mypartnerMsg.get(i).getApp_time());
                        }

                        adapter = new MyPartnerActivity.MyAdapter(mypartnerMsg,MyPartnerActivity.this);
                        mRecyclerView.setAdapter(adapter);
                        System.out.println("集合数量："+adapter.getItemCount());

                    } else {
                        Toast.makeText(MyPartnerActivity.this, "没有查询到相关记录", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return mypartnerMsg;
    }
    class MyAdapter  extends RecyclerView.Adapter<MyPartnerActivity.MyAdapter.MyViewHolder> {
        List<Appoint> appointInfoList;
        public Context context;
        public MyAdapter(List<Appoint> appointInfoList,Context context){
            this.appointInfoList= appointInfoList;
            this.context = context;
        }
        /**
         * 创建ViewHolder的布局
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MyPartnerActivity.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MyPartnerActivity.this).inflate(R.layout.item_order, parent,false);
            MyPartnerActivity.MyAdapter.MyViewHolder holder = new MyPartnerActivity.MyAdapter.MyViewHolder(itemView,parent.getContext());
            return holder;
        }

        /**
         * 通过ViewHolder将数据绑定到界面上进行显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MyPartnerActivity.MyAdapter.MyViewHolder holder, int position) {
            holder.tv_courtid.setText(String.valueOf(appointInfoList.get(position).getApp_time()));
            holder.tv_cost.setText(appointInfoList.get(position).getContent());
            holder.tv_time.setText(appointInfoList.get(position).getType());

        }
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView tv_courtid,tv_cost,tv_time;

            public MyViewHolder(View itemView,Context context) {
                super(itemView);
                tv_courtid = (TextView) itemView.findViewById(R.id.tv_courtid);
                tv_cost = (TextView) itemView.findViewById(R.id.tv_cost);
                tv_time = (TextView) itemView.findViewById(R.id.tv_time);

            }
        }

        @Override
        public int getItemCount() {
            return appointInfoList.size();
        }


    }
}
