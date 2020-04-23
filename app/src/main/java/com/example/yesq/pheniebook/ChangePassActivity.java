package com.example.yesq.pheniebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePassActivity extends AppCompatActivity {
    List<User> userMsg;
    EditText curpass,newpass,passagain;
    String strcurpass,strnewpass,strpassagain;
    Button btn_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        Bmob.initialize(this, "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        userMsg= new ArrayList<User>();
        curpass = (EditText)findViewById(R.id.tv_curpass);
        newpass = (EditText)findViewById(R.id.tv_newpass);
        passagain = (EditText)findViewById(R.id.tv_passagain);

        strcurpass = curpass.getText().toString();
        strnewpass = newpass.getText().toString();
        strpassagain = passagain.getText().toString();


        btn_confirm=(Button)findViewById(R.id.btn_cofirm);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strcurpass.equals("") || strnewpass.equals("")||strpassagain.equals("")) {
                    Toast.makeText(ChangePassActivity.this, "密码不能为空！！", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    if(strcurpass.equals("666666")){
                        if(strcurpass.equals(strpassagain)){
                            User upass = new User();
                            upass.setUserpass(strnewpass);
                            upass.update("Z1Ys222R", new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(ChangePassActivity.this,"修改密码成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(ChangePassActivity.this,"修改密码失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                        }else{
                            Toast.makeText(ChangePassActivity.this, "两次密码设置不一致！", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else{
                        Toast.makeText(ChangePassActivity.this, "当前密码不正确！", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
