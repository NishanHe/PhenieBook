package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import cn.bmob.newsmssdk.BmobSMS;
//import cn.bmob.newsmssdk.listener.RequestSMSCodeListener;
//import cn.bmob.newsmssdk.listener.VerifySMSCodeListener;
import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.Bmob;
//import cn.bmob.v3.BmobUser;
//import cn.bmob.v3.exception.BmobException;
//import cn.bmob.v3.listener.SaveListener;
//import cn.bmob.v3.listener.UpdateListener;


public class LoginActivity extends AppCompatActivity {

    EditText userPhone, verifycode;
    TextView changeLogin,register;
    Button sure,verify;
    String phone = "";
    String code = "";
    SharedPreferences.Editor editor;
    User user;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Bmob.initialize(this, "8403661f07eba2e08fef16590642ac39");
        BmobSMS.initialize(this, "8403661f07eba2e08fef16590642ac39");
//        init();
        sure = (Button) findViewById(R.id.login_btn_sure);
        verify = (Button) findViewById(R.id.login_btn_verifycode);
        changeLogin = (TextView)findViewById(R.id.login_change_password);
        register = (TextView)findViewById(R.id.login_text_register);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userPhone = (EditText) findViewById(R.id.login_edit_user);
                phone = userPhone.getText().toString();

                if(phone.length()==0) {
                    Toast.makeText(LoginActivity.this, "手机号不能为空", Toast.LENGTH_LONG).show();
                    return;
                }
                if(phone.length()!=11) {
                    Toast.makeText(LoginActivity.this, "请输入11位有效号码", Toast.LENGTH_LONG).show();
                    return;
                }
                //进行获取验证码操作和倒计时1分钟操作
                BmobSMS.requestSMSCode(LoginActivity.this, phone, "login", new RequestSMSCodeListener() {

                    @Override
                    public void done(Integer integer, BmobException e) {
                        System.out.println("进来了！！！！！");
                        if (e == null) {
                            //发送成功时，让获取验证码按钮不可点击，且为灰色
                            System.out.println("发送成功！！！！！");
                            verify.setClickable(false);
                            verify.setBackgroundColor(Color.GRAY);
                            Toast.makeText(LoginActivity.this, "验证码发送成功，请尽快使用", Toast.LENGTH_SHORT).show();
                                /**
                                 * 倒计时1分钟操作
                                 * 说明：
                                 * new CountDownTimer(60000, 1000),第一个参数为倒计时总时间，第二个参数为倒计时的间隔时间
                                 * 单位都为ms，其中必须要实现onTick()和onFinish()两个方法，onTick()方法为当倒计时在进行中时，
                                 * 所做的操作，它的参数millisUntilFinished为距离倒计时结束时的时间，以此题为例，总倒计时长
                                 * 为60000ms,倒计时的间隔时间为1000ms，然后59000ms、58000ms、57000ms...该方法的参数
                                 * millisUntilFinished就等于这些每秒变化的数，然后除以1000，把单位变成秒，显示在textView
                                 * 或Button上，则实现倒计时的效果，onFinish()方法为倒计时结束时要做的操作，此题可以很好的
                                 * 说明该方法的用法，最后要注意的是当new CountDownTimer(60000, 1000)之后，一定要调用start()
                                 * 方法把该倒计时操作启动起来，不调用start()方法的话，是不会进行倒计时操作的
                                 */
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    verify.setBackgroundResource(R.drawable.button_shape02);
                                    verify.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    verify.setClickable(true);
                                    verify.setBackgroundResource(R.drawable.button_shape);
                                    verify.setText("重新发送");
                                }
                            }.start();
                            Log.e("MESSAGE:", "4");
                        } else {
                            Toast.makeText(LoginActivity.this, e.getErrorCode() + "验证码发送失败，请检查网络连接", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        });



        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifycode = (EditText) findViewById(R.id.login_edit_psw);
                code = verifycode.getText().toString();
                if(!code.equals("")) {
                    BmobSMS.verifySmsCode(LoginActivity.this, phone, code, new VerifySMSCodeListener() {

                        @Override
                        public void done(BmobException ex) {
                            // TODO Auto-generated method stub
                            if (ex == null) {//短信验证码已验证成功
                                Log.i("bmob", "验证通过");
                                Intent intent = new Intent(LoginActivity.this, FrameActivity.class);
                                startActivity(intent);

                            } else {
                                Log.i("bmob", "验证失败：code =" + ex.getErrorCode() + ",msg = " + ex.getLocalizedMessage());
                            }

                        }


                    });
                }

            }

        });

        changeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, Login2Activity.class);
                startActivity(intent1);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent1);
            }
        });
    }


}
