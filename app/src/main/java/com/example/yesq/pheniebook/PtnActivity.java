package com.example.yesq.pheniebook;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PtnActivity extends AppCompatActivity {

    TextView textid,texttime;
    private DatePicker datepicker;
    private Spinner spinner = null;
    EditText request;
    Button sure;
    private boolean b = false;
    private ArrayAdapter<String> adapter = null;
    private List<String> list = null;
    List<Appoint> appMsg;
    String ptntype = "";
    String content = "";
    Date date;
    User user;
    int year,month,day;
    String userid;
    Calendar c;
BmobDate dtt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ptn);

        Bmob.initialize(this, "8403661f07eba2e08fef16590642ac39");
//        User user = BmobUser.getCurrentUser(User.class);
        textid = (TextView)findViewById(R.id.ptn_text_showid);
        user  = BmobUser.getCurrentUser( User.class);
        if(user !=null){
            textid.setText(user.getUsername());
        }

        texttime = (TextView)findViewById(R.id.ptn_text_showtime);


        c = Calendar.getInstance();                   //获取当前时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = "";
        if(c!=null && c.getTime()!=null){
            formattedDate = df.format(c.getTime());

        }
        // formattedDate have current date/time
        Toast.makeText(this, formattedDate, Toast.LENGTH_SHORT).show();
        dtt= new BmobDate(new Date(System.currentTimeMillis()));

        // Now we display formattedDate value in TextView
        if(formattedDate!=null && !formattedDate.equals("")){
            texttime.setText(formattedDate);    //设置显示当前时间
        }

        texttime.setTextSize(20);

        //选择约球的类型
        spinner = (Spinner)findViewById(R.id.ptn_spinner);
        spinner.setPrompt("请选择约球的类型");
        list = new ArrayList<String>();
        list.add("羽毛球");
        list.add("网球");
        list.add("台球");
        list.add("乒乓球");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);




//        timepicker = (TimePicker)findViewById(R.id.ptn_tpk_time);
//        timepicker.setIs24HourView(true);
//        timepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener(){
//
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year,month,day,hourOfDay,minute);
//                date = calendar.getTime();
//                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
//
//
//            }
//        });

        request = (EditText)findViewById(R.id.ptn_edit_request);

        sure = (Button)findViewById(R.id.ptn_btn_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PtnActivity.this.getApplicationContext(),
                        "发布约球信息成功：",Toast.LENGTH_SHORT);

                ptntype=(String) spinner.getSelectedItem();
                content = request.getText().toString();

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //拿到被选择项的值  
                        if (b) {
                            ptntype = PtnActivity.this.getResources()
                                    .getStringArray(R.array.ptnType)[position];
                            //设置显示当前选择的项
                            System.out.println(ptntype);
                            parent.setVisibility(View.VISIBLE);
                        }
                        b = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                datepicker = (DatePicker)findViewById(R.id.ptn_dpk_date);
                datepicker.init(2017,6,1,new DatePicker.OnDateChangedListener(){

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,monthOfYear,dayOfMonth);
                        date = calendar.getTime();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                        PtnActivity.this.year = year;
                        PtnActivity.this.month = monthOfYear;
                        PtnActivity.this.day = dayOfMonth;

                    }
                });

                Appoint ap = new Appoint();
                if(ptntype.length() != 0 && date!=null && content.length() != 0){
                    ap.setUser(user);
                    ap.setType(ptntype);
                    ap.setApp_time(new BmobDate(date));
                    ap.setContent(content);
                    System.out.print("成功");
                }
                else{
                    System.out.println("失败");}

                ap.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId,BmobException e) {
                        if(e==null){
                            Toast.makeText(PtnActivity.this.getApplicationContext(),
                                    "发布约球信息成功：",Toast.LENGTH_SHORT);
                            Intent intent1 = new Intent(PtnActivity.this, PartFragment.class);
                            startActivity(intent1);
//                            finish();

                        }else{
                            Toast.makeText(PtnActivity.this.getApplicationContext(),
                                    "发布约球信息失败：",Toast.LENGTH_SHORT);
                        }
                    }
                });
                Intent intent1 = new Intent(PtnActivity.this, FrameActivity.class);
                startActivity(intent1);
            }
        });
    }
}
