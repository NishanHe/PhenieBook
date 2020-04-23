package com.example.yesq.pheniebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BookActivity extends AppCompatActivity {
    TextView userid,courtid,bookTime,bookDate,cost;
    private String strHour;
    private Spinner sp;
    Button sure;
    private boolean b = false;
    int price,intcost;
    double balance;
    String strCurDate,strCurTime,strOrderTime;
    String crtid;
    long currTime,orderTim;
    List<User> userMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        userid = (TextView) findViewById(R.id.txt_userid);
        courtid = (TextView) findViewById(R.id.txt_courtid);
        bookTime = (TextView) findViewById(R.id.txt_booktime);
        bookDate = (TextView) findViewById(R.id.txt_bookdate);
        cost = (TextView) findViewById(R.id.txt_price);
        sure = (Button)findViewById(R.id.btn_confirm);

        Intent bookIntent = getIntent();
        Bundle bundle = bookIntent.getExtras();

        price = bundle.getInt("key_price");
        crtid = bundle.getString("key_Courtid");

        sp = (Spinner) findViewById(R.id.spi_bookhour);

        userid.setText("14846666");
        courtid.setText(crtid);
        currTime = System.currentTimeMillis();
        long bookTim = currTime + 600000;
        strCurDate = formatData("yyyy-MM-dd", bookTim);
        strCurTime =formatData("HH:mm:ss",bookTim);
        bookDate.setText(strCurDate);
        bookTime.setText(strCurTime);
        Toast.makeText(BookActivity.this,"请在预约时间内到达！", Toast.LENGTH_SHORT).show();

        strHour = (String) sp.getSelectedItem();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //拿到被选择项的值  
//                if (b) {
                strHour = (String) sp.getSelectedItem();
                if (strHour.equals("1小时")) {

                    intcost = price;
                    cost.setText(Integer.toString(price));
                    orderTim = currTime + 3600000;
                    strOrderTime =formatData("yyyy-MM-dd HH:mm:ss",orderTim);
                } else {

                    intcost = price * 2;
                    cost.setText(Integer.toString(price * 2));
                    orderTim = currTime + 7200000;

                }
                strOrderTime =formatData("yyyy-MM-dd HH:mm:ss",orderTim);
//                }
//                b = true;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        User_inschool user = BmobUser.getCurrentUser(User_inschool.class);
//        Integer intUser =user.getUserID();

//        curDate = new Date(System.currentTimeMillis());

        BmobQuery<User> bookQuery = new BmobQuery<>();
        bookQuery.addWhereEqualTo("userID", "14846666");
        bookQuery.setLimit(50);
        bookQuery.findObjects(new FindListener<User>()  {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        System.out.println("获取!");
                        userMsg=list;
                        Log.i("bmob", "booking时间："+String.valueOf(list.get(0).getBalance()) );
                        balance= list.get(0).getBalance().doubleValue();
                    } else {
                        Toast.makeText(BookActivity.this, "没有查询到相关用户记录", Toast.LENGTH_SHORT).show();
                        balance=0;
                    }
                }
                else {
                    Log.i("bmob", "查询用户信息失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BookActivity.this,"支付中..", Toast.LENGTH_SHORT).show();
                balance= balance-intcost;
                System.out.println("当前余额为："+balance);
                Booking booking = new Booking();
                booking.setUserid(14846666);
                booking.setCost(intcost);
                booking.setCourtid(crtid);
                booking.setTimeEnd(strOrderTime);
                booking.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
                            Toast.makeText(BookActivity.this,"成功！已支付了"+intcost+"元", Toast.LENGTH_SHORT).show();
                            User ubook = new User();
                            ubook.setBalance(balance);
                            ubook.update("Z1Ys222R", new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(BookActivity.this,"更新成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(BookActivity.this,"更新失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }else{
                            Toast.makeText(BookActivity.this,"支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Intent intent = new Intent(BookActivity.this, BookSuccessActivity.class);
                intent.putExtra("key_userid","14846666");
                intent.putExtra("key_courtid",crtid);
                intent.putExtra("key_bookdate",strCurDate);
                intent.putExtra("key_booktime",strOrderTime);
                intent.putExtra("key_bookhour",strHour);
                intent.putExtra("key_cost",intcost);
                startActivity(intent);

            }
        });
    }
    public static String formatData(String dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        timeStamp = timeStamp * 1000;
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat(dataFormat);
        result = format.format(new Date(timeStamp));
        return result;
    }


}
