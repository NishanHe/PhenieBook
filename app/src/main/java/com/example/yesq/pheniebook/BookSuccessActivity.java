package com.example.yesq.pheniebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BookSuccessActivity extends AppCompatActivity {
    TextView userid,courtid,bookTime,bookDate,cost,bookHour;
    Button sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_success);
        userid = (TextView) findViewById(R.id.txt_userid);
        courtid = (TextView) findViewById(R.id.txt_courtid);
        bookTime = (TextView) findViewById(R.id.txt_booktime);
        bookDate = (TextView) findViewById(R.id.txt_bookdate);
        cost = (TextView) findViewById(R.id.txt_price);
        bookHour = (TextView) findViewById(R.id.txt_bookhour);
        sure = (Button)findViewById(R.id.btn_confirm);

        Intent bookIntent = getIntent();
        Bundle bundle = bookIntent.getExtras();
        userid.setText(bundle.getString("key_userid"));
        courtid.setText(bundle.getString("key_courtid"));
        bookDate.setText(bundle.getString("key_bookdate"));
        bookTime.setText(bundle.getString("key_booktime"));
        bookHour.setText(bundle.getString("key_bookhour"));
        cost.setText(bundle.getString("key_cost"));

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
                Intent fraintent = new Intent(BookSuccessActivity.this, FrameActivity.class);
//                fraintent.putExtra("key_fraid","choose");
                startActivityForResult(fraintent, 2);

            }
        });
    }
}
