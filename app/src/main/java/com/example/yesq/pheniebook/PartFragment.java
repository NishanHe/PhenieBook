package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.ParseException;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.internal.framed.FrameReader;


public class PartFragment extends Fragment {
    Spinner spinner;
    DatePicker picker;
    private List<String> list = null;
    List<Appoint> appMsg;
    boolean b = false;
    String ptntype = "";
    Date appDate;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    SwipeRefreshLayout swipe;
    FloatingActionButton fab;
//    AppointAdapter adapter;
    Button sure;
    FrameLayout part_root;
//    TextView info1 ,info2,info3,info4,info5,info6,info7,info8;

    public PartFragment() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bmob.initialize(getContext(), "8403661f07eba2e08fef16590642ac39");
        View view = inflater.inflate(R.layout.fragment_part, container, false);


//        info1 = (TextView) view.findViewById(R.id.info1);
//        info2 = (TextView) view.findViewById(R.id.info2);
//        info3 = (TextView) view.findViewById(R.id.info3);
//        info4 = (TextView) view.findViewById(R.id.info4);
//        info5 = (TextView) view.findViewById(R.id.info5);
//        info6 = (TextView) view.findViewById(R.id.info6);
//        info7 = (TextView) view.findViewById(R.id.info7);
//        info8 = (TextView) view.findViewById(R.id.info8);
        sure = (Button)view.findViewById(R.id.part_btn_sure);

//        //单一布局
//        IMutlipleItem<Appoint> mutlipleItem = new IMutlipleItem<Appoint>() {
//
//            @Override
//            public int getItemViewType(int position, Appoint c) {
//                return 0;
//            }
//
//            @Override
//            public int getItemLayoutId(int viewtype) {
//                return R.layout.item_appoint;
//            }
//
//            @Override
//            public int getItemCount(List<Appoint> list) {
//                return list.size();
//            }
//        };


        appMsg = new ArrayList<Appoint>();


        spinner = (Spinner)view.findViewById(R.id.part_spinner);
        picker = (DatePicker)view.findViewById(R.id.part_dpk);
        part_root = (FrameLayout)view.findViewById(R.id.part_ll);

        spinner.setPrompt("请选择约球的类型");
        list = new ArrayList<String>();
        list.add("羽毛球");
        list.add("网球");
        list.add("台球");
        list.add("乒乓球");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource (getContext(),R.array.ptnType,
                android.R.layout. simple_spinner_item );
//        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.partner_recycler);
        swipe = (SwipeRefreshLayout) view.findViewById(R.id.partner_swrefresh);

//        adapter = new AppointAdapter(getContext(),mutlipleItem,null);
//        mRecyclerView.setAdapter(adapter);
//        mLayoutManager = new LinearLayoutManager(getContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //拿到被选择项的值  
                if (b) {
                    ptntype = com.example.yesq.pheniebook.PartFragment.this.getResources()
                            .getStringArray(R.array.ptnType)[position];
                    //设置显示当前选择的项
                    parent.setVisibility(View.VISIBLE);

                }
                b = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        picker = (DatePicker)view.findViewById(R.id.part_dpk);


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.init(2017,6,1,new DatePicker.OnDateChangedListener(){

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,monthOfYear,dayOfMonth);
                        appDate = calendar.getTime();
//                System.out.println(appDate);
//                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    }
                });
                System.out.println("约球信息查询中...");
                query();
                System.out.println("查询完了");

                initDate();
                mLayoutManager = new LinearLayoutManager(getActivity());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());

//                mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getContext(),new RecyclerViewClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Toast.makeText(getContext(),"Click "+list.get(position),Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(getActivity(), BuyActivity.class);
//
//                        intent.putExtra("key_proId",list.get(position).getObjid());
//                        intent.putExtra("key_proName",list.get(position).getObjname());
//                        intent.putExtra("key_proPri",list.get(position).getObjprice());
//                        intent.putExtra("key_objNum",list.get(position).getObjnum());
//                        startActivity(intent);
//
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        Toast.makeText(getActivity(),"Long Click "+list.get(position),Toast.LENGTH_SHORT).show();
//                    }
//                }));
            }
        });

        fab = (FloatingActionButton)view.findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), PtnActivity.class);
                startActivity(intent1);
            }
        });
//        setListener();

        return view;
    }



    public List<Appoint> initDate(){
        ptntype = (String) spinner.getSelectedItem();
        BmobQuery<Appoint> query1 = new BmobQuery<Appoint>();
        query1.addWhereEqualTo("type", ptntype);
//        query.addWhereEqualTo("app_time", date);
//        query1.setLimit(500);


        List<BmobQuery<Appoint>> queries = new ArrayList<BmobQuery<Appoint>>();
//大于00：00：00
        BmobQuery<Appoint> dq1 = new BmobQuery<Appoint>();
//        String start = "2015-05-01 00:00:00";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date  = null;
//        try {
//            date = sdf.parse(start);
//        }  catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
        if(appDate !=null){
            dq1.addWhereGreaterThanOrEqualTo("app_time",new BmobDate(appDate));
        }

        queries.add(query1);
        queries.add(dq1);
//小于23：59：59
        BmobQuery<Appoint> dq2 = new BmobQuery<Appoint>();
//        String end = "2015-05-01 23:59:59";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
//获取当前时间
        Date dateNow = new Date(System.currentTimeMillis());
//        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date date1  = null;
//        try {
//            date1 = sdf1.parse(end);
//        } catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }
        dq2.addWhereLessThanOrEqualTo("app_time",new BmobDate(dateNow));
        queries.add(dq2);
//添加复合与查询

        BmobQuery<Appoint> mainQuery = new BmobQuery<Appoint>();
        BmobQuery<Appoint> and = mainQuery.or(queries);

        queries.add(and);
        BmobQuery<Appoint> query = new BmobQuery<Appoint>();
        query.and(queries);
        System.out.println("查询准备完毕");
        query.findObjects(new FindListener<Appoint>() {

            @Override
            public void done(List<Appoint> object, BmobException e) {
                if(e==null){
                    System.out.println("查询开始！");
                        for(Appoint appoint : object) {
                            appoint.getUser();
                            appoint.getType();
                            System.out.println("获取到的体育类型是" + appoint.getType());
//                            appoint.getId();
                            appoint.getContent();
                            System.out.println("获取到的要求是" + appoint.getContent());
                            appoint.getApp_time();
                            System.out.println("获取到的时间是" + appoint.getApp_time());
                            appMsg.add(appoint);
                            System.out.println("获取到了");
                            Log.i("bmob","获取到了" + appMsg.get(0).getContent());
                        }
                    MyAdapter adapter = new MyAdapter(appMsg);
                    //设置Adapter
                    mRecyclerView.setAdapter(adapter);

                }else{
                    Log.i("bmob","失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return appMsg;
    }

    class MyAdapter extends RecyclerView.Adapter<PartFragment.MyAdapter.MyViewHolder> {
        List<Appoint> items;

        public MyAdapter(List<Appoint> items) {
            this.items = items;
        }

        /**
         * 创建ViewHolder的布局
         *
         * @param parent
         * @param viewType
         * @return
         */

        @Override
        public PartFragment.MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appoint, parent, false);
            PartFragment.MyAdapter.MyViewHolder holder = new PartFragment.MyAdapter.MyViewHolder(view);
            return holder;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView date, type, host, request;

            public MyViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.appoint_text_date);
                type = (TextView) itemView.findViewById(R.id.appoint_text_type);
                host = (TextView) itemView.findViewById(R.id.appoint_text_host);
                request = (TextView) itemView.findViewById(R.id.appoint_text_request);
            }
        }

        /**
         * 通过ViewHolder将数据绑定到界面上进行显示
         *
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(PartFragment.MyAdapter.MyViewHolder holder, int position) {
            if(items != null && items.get(position) != null && String.valueOf(items.get(position).getApp_time()).length() != 0
                    && items.get(position).getType().length() != 0 && items.get(position).getUser() !=null &&
                    String.valueOf(items.get(position).getUser().getUsername()).length() !=0
                    && String.valueOf(items.get(position).getContent()).length() != 0 ) {
//                Date time = new Date(String.valueOf(items.get(position).getApp_time()));
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dateString = formatter.format(time);
                holder.date.setText((CharSequence) String.valueOf(items.get(position).getApp_time()));
                holder.type.setText((CharSequence) items.get(position).getType());
                holder.host.setText((CharSequence) String.valueOf(items.get(position).getUser().getUsername()));
                holder.request.setText((CharSequence) String.valueOf(items.get(position).getContent()));
            }

        }


        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        swipe.setRefreshing(true);
        query();
    }

    public void query(){
        initDate();
        System.out.println("数据查看了");
//        adapter.bindDatas(appMsg);   //adapter绑定
//        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    private void setListener(){
        part_root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                part_root.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                swipe.setRefreshing(true);
                query();
            }
        });
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
//        adapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
//            @Override
//            public void onItemClick(int position) {
////                startActivity(CommentActivity.class, null);
////                log("点击："+position);
//
//            }
//
//            @Override
//            public boolean onItemLongClick(int position) {
//// click long to delete new friend
////                FanManager.getInstance(MyFanActivity.this).deleteNewFriend(adapter.getItem(position));
////                adapter.remove(position);
////                Intent intent = new Intent(getActivity().getApplicationContext(), CommentActivity.class);
////                startActivity(intent);
//                return true;
//            }
//        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     查询本地会话
     */

//    class MyAdapter  extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
//        private List<Appoint> items;
//
//        public MyAdapter(List<Appoint> items) {
//            this.items = items;
//        }
//
//        /**
//         * 创建ViewHolder的布局
//         * @param parent
//         * @param viewType
//         * @return
//         */
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2,parent,false);
//            MyViewHolder holder = new MyViewHolder(view);
//            return holder;
//        }
//
//        /**
//         * 通过ViewHolder将数据绑定到界面上进行显示
//         * @param holder
//         * @param position
//         */
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.mTextView.setText((CharSequence) items.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return items.size();
//        }
//
//        public class MyViewHolder extends RecyclerView.ViewHolder{
//            public TextView mTextView;
//            public MyViewHolder(View itemView) {
//                super(itemView);
//                mTextView = (TextView) itemView.findViewById(R.id.textView);
//
//            }
//        }
//    }
//    public void setMenuVisibility(boolean menuVisibile) {
//        super.setMenuVisibility(menuVisibile);
//        if (this.getView() != null) {
//            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
//        }
//    }

}
