package com.example.yesq.pheniebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BuyActivity extends AppCompatActivity {
    TextView name, price, number;
    Button sure;
    Button minus, plus;
    Integer remainNum, buynum,objid;
    Float totalp,objpri;
    Double balance = 0.00;
    User user;
    String objectid,objname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        Bmob.initialize(this, "8403661f07eba2e08fef16590642ac39");
        name = (TextView) findViewById(R.id.buy_text_name);
        price = (TextView) findViewById(R.id.buy_text_price);
        number = (TextView) findViewById(R.id.buy_text_num);

        Intent buyIntent = getIntent();
        Bundle bundle = buyIntent.getExtras();
        objid = bundle.getInt("key_proId");
        objname = bundle.getString("key_proName");
        objpri = bundle.getFloat("key_proPri");
        remainNum = bundle.getInt("key_objNum");

        name.setText(objname);

        buynum = 1;
        totalp = objpri;
        number.setText("1");
        price.setText(String.valueOf(totalp));

        minus = (Button) findViewById(R.id.buy_btn_minus);
        plus = (Button) findViewById(R.id.buy_btn_plus);

        user = BmobUser.getCurrentUser( User.class);
        if(user != null && user.getBalance() != null){
            balance = user.getBalance();
        }
//        BmobQuery<User> bookQuery = new BmobQuery<>();
//        bookQuery.addWhereEqualTo("userID", "14846666");
//        bookQuery.setLimit(50);
//        bookQuery.findObjects(new FindListener<User>()  {
//            @Override
//            public void done(List<User> list, BmobException e) {
//                if (e == null) {
//                    if (list.size() > 0) {
//                        Log.i("bmob", "banlance："+String.valueOf(list.get(0).getBalance()) );
//                        balance= list.get(0).getBalance().doubleValue();
//                    } else {
//                        Toast.makeText(BuyActivity.this, "没有查询到相关用户记录", Toast.LENGTH_SHORT).show();
//                        balance=0.00;
//                    }
//                }
//                else {
//                    Log.i("bmob", "查询用户信息失败：" + e.getMessage() + "," + e.getErrorCode());
//                }
//            }
//        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText() != null && Integer.parseInt(number.getText().toString()) >= 1) {
                    buynum = buynum - 1;
                    totalp = objpri * buynum;
                    number.setText(String.valueOf(buynum));
                    price.setText(String.valueOf(totalp));

                } else {
                    Toast.makeText(BuyActivity.this, "数量需大于零", Toast.LENGTH_SHORT).show();
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number.getText() != null && Integer.parseInt(number.getText().toString()) < remainNum) {
                    buynum = buynum + 1;
                    totalp = objpri * buynum;
                    number.setText(String.valueOf(buynum));
                    price.setText(String.valueOf(totalp));

                } else {
                    Toast.makeText(BuyActivity.this, "库存商品数不足！", Toast.LENGTH_SHORT).show();
                }
            }
        });




        sure = (Button) findViewById(R.id.buy_btn_sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BuyActivity.this,"支付中..", Toast.LENGTH_SHORT).show();
                balance= balance-totalp;
                Order order = new Order();
//                User user = BmobUser.getCurrentUser(User.class);
//                String userid = user.getUserID();
                order.setUserid("14846666");
                order.setObjid(objid);
                order.setObjnum(buynum);
                order.setOrderprice(totalp);
                order.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Toast.makeText(BuyActivity.this,"成功！已支付了"+totalp+"元", Toast.LENGTH_SHORT).show();
                            User ubook = new User();
                            ubook.setBalance(balance);
                            ubook.update("Z1Ys222R", new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                        Toast.makeText(BuyActivity.this,"更新成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(BuyActivity.this,"更新失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                        } else {
                            Toast.makeText(BuyActivity.this.getApplicationContext(), "购买失败，哦嚯", Toast.LENGTH_SHORT);
                        }
                    }
                });
                Intent intent1 = new Intent(BuyActivity.this, OrderActivity.class);
                intent1.putExtra("key_proName",objname);
                intent1.putExtra("key_proPri",objpri);
                intent1.putExtra("key_total",totalp);
                intent1.putExtra("key_num",buynum);
                startActivity(intent1);

                //找到购买商品的数据库中的id
                BmobQuery<Shop> query = new BmobQuery<Shop>();
                query.addWhereEqualTo("objname", objname);
                query.findObjects(new FindListener<Shop>() {
                    @Override
                    public void done(List<Shop> object, BmobException e) {
                        if (e == null) {
                            for (Shop product : object) {
                                objectid = product.getObjectid();
                            }
                        } else {
                            Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                        }
                    }
                });


                Shop pro = new Shop();
                pro.setObjnum(remainNum - buynum);
                pro.update(objectid, new UpdateListener() {

                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(BuyActivity.this.getApplicationContext(),
                                    "调取仓库商品失败", Toast.LENGTH_SHORT);
                        } else {
                            Toast.makeText(BuyActivity.this.getApplicationContext(),
                                    "调取仓库商品失败", Toast.LENGTH_SHORT);
                        }
                    }

                });
            }
        });
    }
}