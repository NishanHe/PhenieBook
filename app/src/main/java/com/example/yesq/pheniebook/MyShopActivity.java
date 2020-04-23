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

public class MyShopActivity extends AppCompatActivity {
    List<Order> myshopMsg;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyShopActivity.MyAdapter adapter;
    Button btn_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop);
        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        myshopMsg= new ArrayList<Order>();
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
    protected List<Order> initData(){

        BmobQuery<Order> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", "14846666");
        query.setLimit(500);
        query.findObjects(new FindListener<Order>()  {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            myshopMsg.add(list.get(i));
                            System.out.println("第"+i+"条获取");
                            Log.i("bmob", "商品数量：" + myshopMsg.get(i).getObjnum());
                            Log.i("bmob", "商品编号：" + myshopMsg.get(i).getObjid());
                        }

                        adapter = new MyShopActivity.MyAdapter(myshopMsg,MyShopActivity.this);
                        mRecyclerView.setAdapter(adapter);
                        System.out.println("集合数量："+adapter.getItemCount());

                    } else {
                        Toast.makeText(MyShopActivity.this, "没有查询到相关记录", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return myshopMsg;
    }
    class MyAdapter  extends RecyclerView.Adapter<MyShopActivity.MyAdapter.MyViewHolder> {
        List<Order> shopInfoList;
        public Context context;
        public MyAdapter(List<Order> shopInfoList,Context context){
            this.shopInfoList= shopInfoList;
            this.context = context;
        }
        /**
         * 创建ViewHolder的布局
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MyShopActivity.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(MyShopActivity.this).inflate(R.layout.item_obj, parent,false);
            MyShopActivity.MyAdapter.MyViewHolder holder = new MyShopActivity.MyAdapter.MyViewHolder(itemView,parent.getContext());
            return holder;
        }

        /**
         * 通过ViewHolder将数据绑定到界面上进行显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MyShopActivity.MyAdapter.MyViewHolder holder, int position) {
            holder.objname.setText(String.valueOf(shopInfoList.get(position).getObjid()));
            holder.objnum.setText(String.valueOf(shopInfoList.get(position).getObjnum()));
            holder.orderprice.setText(String.valueOf(shopInfoList.get(position).getOrderprice()));
            holder.ordertime.setText(String.valueOf(shopInfoList.get(position).getCreatedAt()));

        }
        public class MyViewHolder extends RecyclerView.ViewHolder
        {
            public TextView objname,objnum,orderprice,ordertime;

            public MyViewHolder(View itemView,Context context) {
                super(itemView);

                objname = (TextView) itemView.findViewById(R.id.objname);
                objnum = (TextView) itemView.findViewById(R.id.objnum);
                orderprice = (TextView) itemView.findViewById(R.id.orderprice);
                ordertime = (TextView) itemView.findViewById(R.id.ordertime);

            }
        }

        @Override
        public int getItemCount() {
            return shopInfoList.size();
        }


    }
}
