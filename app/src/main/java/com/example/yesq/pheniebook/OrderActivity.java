package com.example.yesq.pheniebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.v3.Bmob;

public class OrderActivity extends AppCompatActivity {

    String name;
    Float sprice,tprice;
    Integer number;
    TextView textname,textsprice,texttprice,textnumber;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a");

        Intent buyIntent = getIntent();
        Bundle bundle = buyIntent.getExtras();
        name = bundle.getString("key_proName");
        sprice = bundle.getFloat("key_proPri");
        tprice = bundle.getFloat("key_total");
        number = bundle.getInt("key_num");


        textname = (TextView)findViewById(R.id.order_text_name);
        textsprice = (TextView)findViewById(R.id.order_text_sprice);
        texttprice = (TextView)findViewById(R.id.order_text_tprice);
        textnumber = (TextView)findViewById(R.id.order_text_number);

        textname.setText(name);
        textsprice.setText(String.valueOf(sprice));
        textnumber.setText(String.valueOf(number));
        texttprice.setText("共支付了" + String.valueOf(tprice));

        back = (Button)findViewById(R.id.order_finish);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderActivity.this, FrameActivity.class);
                startActivity(intent);
            }
        });
    }
}
