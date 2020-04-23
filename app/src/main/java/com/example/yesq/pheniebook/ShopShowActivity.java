package com.example.yesq.pheniebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ShopShowActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SearchView searchView;
    //    SharedPreferences.Editor editor;
    private String searchStr = "";
    List<Shop> list;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_show);
        Bmob.initialize(this, "8403661f07eba2e08fef16590642ac39");
        list = new ArrayList<>();


        searchView = (SearchView) findViewById(R.id.shop_show_search);
        // 为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(this);
        // 设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(true);
        // 设置该SearchView内默认显示的提示文本
        searchView.setQueryHint("请输入你想查找的商品名称，如篮球");
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });

    }
    private List<Shop> initDate(){

        BmobQuery<Shop> query = new BmobQuery<Shop>();
        query.addWhereEqualTo("objname", searchStr);
        query.setLimit(50);
        query.findObjects(new FindListener<Shop>() {
            @Override
            public void done(List<Shop> object, BmobException e) {
                if(e==null){
                    for(Shop product : object) {
                        product.getObjid();
                        product.getObjname();
                        product.getObjprice();
                        product.getObjnum();
                        product.getImage();
                        list.add(product);
                    }
                    MyAdapter adapter = new MyAdapter(list);
                    mRecyclerView.setAdapter(adapter);
                }else{
                    Log.i("bmob","失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return list;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        System.out.println("查询开始...");
        if (!TextUtils.isEmpty(query)) {
            searchStr = query;
        }
        mRecyclerView = (RecyclerView) findViewById(R.id.shop_show_recycler_view);
        initDate();
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this,new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ShopShowActivity.this,"Click "+list.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShopShowActivity.this, BuyActivity.class);
                intent.putExtra("key_proId",list.get(position).getObjid());
                intent.putExtra("key_proName",list.get(position).getObjname());
                intent.putExtra("key_proPri",list.get(position).getObjprice());
                intent.putExtra("key_objNum",list.get(position).getObjnum());
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
//                Toast.makeText(ShopShowActivity.this,"Long Click "+list.get(position),Toast.LENGTH_SHORT).show();
            }
        }));
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        private List<Shop> items;

        public MyAdapter(List<Shop> items) {
            this.items = items;
        }

        /**
         * 创建ViewHolder的布局
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textid.setText((CharSequence) String.valueOf(items.get(position).getObjid()));
            holder.textName.setText((CharSequence) items.get(position).getObjname());
            holder.textPrice.setText((CharSequence) String.valueOf(items.get(position).getObjprice()));
            holder.textNum.setText((CharSequence) String.valueOf(items.get(position).getObjnum()));
            //holder.image.setImageDrawable(items.get(position).getImage());

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ShopShowActivity.this, BuyActivity.class);
                    startActivity(intent1);

                }
            });
        }


        @Override
        public int getItemCount() {
            return items.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView textid,textName,textPrice,textNum;
            private ImageView image;
            private Button buy;
            public MyViewHolder(View itemView) {
                super(itemView);
                textid = (TextView) itemView.findViewById(R.id.proId);
                textName = (TextView) itemView.findViewById(R.id.proName);
                textPrice = (TextView) itemView.findViewById(R.id.proPrice);
                textNum = (TextView) itemView.findViewById(R.id.proNum);
//                image = (ImageView) itemView.findViewById(R.id.proPic);
                buy = (Button) itemView.findViewById(R.id.btn_buy);
            }
        }
    }
}
