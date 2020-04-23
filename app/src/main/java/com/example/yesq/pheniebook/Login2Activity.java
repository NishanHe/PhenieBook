package com.example.yesq.pheniebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Login2Activity extends AppCompatActivity {


    EditText usernameEdit,passwordEdit;
    String username = "";
    String password = "";
    Button sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Bmob.initialize(this, "8403661f07eba2e08fef16590642ac39");

        usernameEdit = (EditText)findViewById(R.id.login2_edit_user);
        passwordEdit = (EditText)findViewById(R.id.login2_edit_psw);
        sure = (Button)findViewById(R.id.login2_btn_sure);

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameEdit.getText().toString();
                password = passwordEdit.getText().toString();
                BmobUser user = new BmobUser();
                if(username.length() != 0){
                    user.setUsername(username);
                }else{
                    Toast.makeText(Login2Activity.this, "用户名不能为空！", Toast.LENGTH_LONG).show();
                }

                if(password.length() != 0){
                    user.setPassword(password);
                }else{
                    Toast.makeText(Login2Activity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
                }

                user.login(new SaveListener<BmobUser>() {

                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if(e==null){
                            Toast.makeText(Login2Activity.this, "登录成功！", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Login2Activity.this, FrameActivity.class);
                            startActivity(intent);
                            //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                            //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                        }else{
                            Toast.makeText(Login2Activity.this, "登录失败！用户名或密码错误", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
}
