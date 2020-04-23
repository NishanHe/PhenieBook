package com.example.yesq.pheniebook;

import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.text.*;
import java.util.*;
import cn.bmob.v3.*;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class PeekActivity extends AppCompatActivity {

    String timeEnd,timeCur;
    long crtLast;
    List<Court> courtMsg;
    List<Booking> timeMsg;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyAdapter adapter;
    TextView curType;
    Button changeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peek);
        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        curType= (TextView) findViewById(R.id.txt_showCourt) ;
        courtMsg= new ArrayList<>();

        initTime();
        initData();


        changeType=(Button)findViewById(R.id.btn_rechoose);
        changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setResult(2);
//                finish();
                Intent fraintent = new Intent(PeekActivity.this, FrameActivity.class);
                fraintent.putExtra("key_fraid","choose");
                startActivityForResult(fraintent, 2);

            }
        });
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this));
        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(this,new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(PeekActivity.this,"Click "+courtMsg.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PeekActivity.this, BookActivity.class);
                intent.putExtra("key_price",courtMsg.get(0).getPrice());
                intent.putExtra("key_Courtid",courtMsg.get(position).getCourtid());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(PeekActivity.this,"Long Click "+courtMsg.get(position),Toast.LENGTH_SHORT).show();
            }
        }));

    }

    public void initTime(){
        timeMsg = new ArrayList<Booking>();
        queryTime();

    }
    public long calDiff(){
        long diff=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String courtLast = timeMsg.get(0).getTimeEnd();
//        crtLast=Long.parseLong(courtLast);
        Date currentTime = new Date();
        int result=0;
        try {
            Date d1 = df.parse(courtLast);
            if(d1.before(currentTime)){
                result=1;
                //设置court表的status为空闲；
            }else{
                result =0;
                diff = d1.getTime() - currentTime.getTime();
                long days = diff / (1000 * 60 * 60 * 24);
                long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                        * (1000 * 60 * 60))
                        / (1000 * 60);

            }
        }catch (Exception e) {
        }
        return  diff;
    }


// 获取服务器返回的时间戳 转换成"yyyy-MM-dd HH:mm:ss"
//        String date2 = formatData("yyyy-MM-dd HH:mm:ss", 1463126553);
//        crt2.setText(date2);
//
//        // 计算的时间差
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        try {
//            Date d1 = df.parse(timeCur);
//            Date d2 = df.parse(date2);
//            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别
//            long days = diff / (1000 * 60 * 60 * 24);
//            long hours = (diff - days * (1000 * 60 * 60 * 24))
//                    / (1000 * 60 * 60);
//            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
//                    * (1000 * 60 * 60))
//                    / (1000 * 60);
//        } catch (Exception e) {
//        }

    //    public static String formatData(String dataFormat, long timeStamp) {
//        if (timeStamp == 0) {
//            return "";
//        }
//        timeStamp = timeStamp * 1000;
//        String result = "";
//        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
//        result = format.format(new Date(timeStamp));
//        return result;
//    }
//       SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        timeCur = formatter.format(currentTime);
//    private void readTime(String md5) {
//        //获取限时时间  
//        timeLimit = getIntent().getStringExtra("timeLimit");
//        //计算限时时间毫秒数  
//        timeOver = Integer.parseInt(timeLimit) * 60 * 1000;
//        //当前时间  
//        currentTime = System.currentTimeMillis();
//        //获取最后一次时间  
//        lastTime = PrefUtils.getLong(md5, currentTime, this);
//        //计算剩余时间: 限时时间 - 使用时间  
//        timeRest = timeOver - (currentTime -lastTime);
//        //计时开始  
//        if (timeRest > 0){
//            LogUtils.i("lastTime", lastTime + "");
//            LogUtils.i("currentTime",currentTime+"");
//            LogUtils.i("timeRest",timeRest+"");
//            mHandler.sendEmptyMessageDelayed(END_TEST,timeRest);
//            }
//        else {
//            ToastUtils.showToast(this,"您之前开过试题，限制时间已过");
//            //提交后台数据只能一次，这里的代码只会执行一次，因为你可以重复进入该试题，会导致多条数据相同  
//            if(num == 0) {
//                sendService(0);
//                num++;
//                }
//            finish();
//            }
//    }
    public void queryTime(){
        BmobQuery<Booking> bookQuery = new BmobQuery<>();
        bookQuery.addWhereEqualTo("courtid", "TT001");
        bookQuery.order("-timeEnd");
        bookQuery.setLimit(500);
        System.out.println("查找中："+timeMsg.size());
        bookQuery.findObjects(new FindListener<Booking>()  {
            @Override
            public void done(List<Booking> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            System.out.println("第" + i + "条获取");
                            timeMsg.add(list.get(i));
                            Log.i("bmob", "booking时间：" + timeMsg.get(i).getTimeEnd());
                        }
                    } else {
                        Toast.makeText(PeekActivity.this, "没有查询到相关时间记录", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("bmob", "查询时间失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
//        Collections.sort(timeMsg, new Comparator<Booking>(){
//            @Override
//            public int compare(Booking lhs, Booking rhs) {
//                Date date1 = lhs.getTimeEnd();
//                Date date2 = rhs.getTimeEnd();
//                // 对日期字段进行升序，如果欲降序可采用after方法  
//                if (date1.after(date2)) {
//                    return 1;
//                }
//                else {return -1;}
//            }
//        });
    }

    protected List<Court> initData(){
        Intent courtIntent = getIntent();
        Bundle bundle = courtIntent.getExtras();
        String strCourt = bundle.getString("key_courtType");
        curType.setText("当前选择的场地类型是"+strCourt);
        BmobQuery<Court> query = new BmobQuery<>();
        query.addWhereEqualTo("type", strCourt);
        query.setLimit(500);
        query.findObjects(new FindListener<Court>()  {
            @Override
            public void done(List<Court> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            courtMsg.add(list.get(i));
                            Log.i("bmob", "场地编号" + courtMsg.get(i).getCourtid());
                            Log.i("bmob", "场地状态" + courtMsg.get(i).getStatus());
                        }

                        adapter = new MyAdapter(courtMsg,PeekActivity.this);
                        mRecyclerView.setAdapter(adapter);


                        System.out.println("集合数量："+adapter.getItemCount());

                    } else {
                        Toast.makeText(PeekActivity.this, "没有查询到相关记录", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return courtMsg;
    }

    class MyAdapter  extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<Court> courtInfoList;
        public Context context;
        public MyAdapter(List<Court> courtInfoList,Context context){
            this.courtInfoList= courtInfoList;
            this.context = context;
        }
        /**
         * 创建ViewHolder的布局
         * @param parent
         * @param viewType
         * @return
         */
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(PeekActivity.this).inflate(R.layout.item, parent,false);
            MyViewHolder holder = new MyViewHolder(itemView,parent.getContext());
            return holder;
        }

        /**
         * 通过ViewHolder将数据绑定到界面上进行显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_sta.setText(courtInfoList.get(position).getStatus());
            holder.tv_crtid.setText(courtInfoList.get(position).getCourtid());
            holder.tv_pri.setText(String.valueOf(courtInfoList.get(position).getPrice()));
        }
        public class MyViewHolder extends RecyclerView.ViewHolder

        {
            public TextView tv_crtid,tv_sta,tv_pri;
            public  CustomDigitalClock remainTime;
            public MyViewHolder(View itemView,Context context) {
                super(itemView);

                tv_sta = (TextView) itemView.findViewById(R.id.text_sta);
                tv_crtid = (TextView) itemView.findViewById(R.id.text_crtid);
                tv_pri = (TextView) itemView.findViewById(R.id.text_pri);
                remainTime = (CustomDigitalClock) itemView.findViewById(R.id.remainTime);
//                 remainTime.setEndTime(calDiff());
                remainTime.setClockListener(new CustomDigitalClock.ClockListener() {
                    // register the clock's listener
                    @Override
                    public void timeEnd() {
                        // The clock time is ended.
                    }
                    @Override
                    public void remainFiveMinutes() {
                        // The clock time is remain five minutes.
                    }
                });
//                 itemView.setOnClickListener(this);
            }


//             @Override
//             public void onClick(View v) {
//                 Intent intent = new Intent(PeekActivity.this, BookActivity.class);
//                 intent.putExtra("key_price",courtMsg.get(0).getPrice());
//                 context.startActivity(intent);
//
//                 System.out.println("价格为："+courtMsg.get(0).getPrice());
//             }

        }


        @Override
        public int getItemCount() {
            return courtInfoList.size();
        }


    }
}
