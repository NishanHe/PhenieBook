package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

public class MyOrderActivity extends AppCompatActivity {
    List<Booking> myorderMsg;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyOrderActivity.MyAdapter adapter;
    Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        myorderMsg= new ArrayList<Booking>();
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
    protected List<Booking> initData(){

        BmobQuery<Booking> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", 14846666);
        query.setLimit(500);
        query.findObjects(new FindListener<Booking>()  {
            @Override
            public void done(List<Booking> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            myorderMsg.add(list.get(i));
                            System.out.println("第"+i+"条获取");
                            Log.i("bmob", "场地编号：" + myorderMsg.get(i).getCourtid());
                            Log.i("bmob", "预约编号：" + myorderMsg.get(i).getBookid());
                        }

                        adapter = new MyAdapter(myorderMsg,MyOrderActivity.this);
                        mRecyclerView.setAdapter(adapter);
                        System.out.println("集合数量："+adapter.getItemCount());

                    } else {
                        Toast.makeText(MyOrderActivity.this, "没有查询到相关记录", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return myorderMsg;
    }
    class MyAdapter  extends RecyclerView.Adapter<MyOrderActivity.MyAdapter.MyViewHolder> {
        List<Booking> bookInfoList;
        public Context context;
        public MyAdapter(List<Booking> bookInfoList,Context context){
            this.bookInfoList= bookInfoList;
            this.context = context;
        }
        /**
         * 创建ViewHolder的布局
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MyOrderActivity.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MyOrderActivity.this).inflate(R.layout.item_order, parent,false);
            MyOrderActivity.MyAdapter.MyViewHolder holder = new MyOrderActivity.MyAdapter.MyViewHolder(itemView,parent.getContext());
            return holder;
        }

        /**
         * 通过ViewHolder将数据绑定到界面上进行显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MyOrderActivity.MyAdapter.MyViewHolder holder, int position) {
            holder.tv_courtid.setText(bookInfoList.get(position).getCourtid());
            holder.tv_cost.setText(String.valueOf(bookInfoList.get(position).getCost()));
            holder.tv_time.setText(String.valueOf(bookInfoList.get(position).getCreatedAt()));

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
            return bookInfoList.size();
        }


    }
}