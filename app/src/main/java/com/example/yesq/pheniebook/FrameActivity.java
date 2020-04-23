package com.example.yesq.pheniebook;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FrameActivity  extends FragmentActivity {
    private FrameLayout mHomeContent;
    private Fragment mFragments[];
    private RadioGroup radioGroup;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private RadioButton rbtPart,rbtInfo,rbtChoose,rbtShop,rbtMy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_frame);
        initView();

//        mFragments = new Fragment[5];
//        fragmentManager = getSupportFragmentManager();
//        mFragments[0] = fragmentManager.findFragmentById(R.id.fragment_info);
//        mFragments[1] = fragmentManager.findFragmentById(R.id.fragment_part);
//        mFragments[2] = fragmentManager.findFragmentById(R.id.fragment_choose);
//        mFragments[3] = fragmentManager.findFragmentById(R.id.fragment_shop);
//        mFragments[4] = fragmentManager.findFragmentById(R.id.fragment_my);
//
//
//        fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1])
//                .hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
//        fragmentTransaction.show(mFragments[0]).commit();
//
//        radioGroup = (RadioGroup)findViewById(R.id.bottomGroup);
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.i("radioGroup", "checkId=" + checkedId);
//                fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1])
//                        .hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
//                switch(checkedId){
//                    case R.id.radioInfo:
//                        fragmentTransaction.show(mFragments[0]).commit();
//                        break;
//                    case R.id.radioPart:
//                        fragmentTransaction.show(mFragments[1]).commit();
//                        break;
//                    case R.id.radioChoose:
//                        fragmentTransaction.show(mFragments[2]).commit();
//                        break;
//                    case R.id.radioShop:
//                        fragmentTransaction.show(mFragments[3]).commit();
//                        break;
//                    case R.id.radioMy:
//                        fragmentTransaction.show(mFragments[4]).commit();
//                        break;
//                    default:
//                        break;
//                }
//            } });
    }
    protected void initView() {
        mHomeContent = (FrameLayout) findViewById(R.id.mHomeContent); //tab上方的区域
        radioGroup = (RadioGroup)findViewById(R.id.bottomGroup);
        rbtPart = (RadioButton) findViewById(R.id.radioPart);
        rbtChoose = (RadioButton) findViewById(R.id.radioChoose);
        rbtShop = (RadioButton) findViewById(R.id.radioShop);
        rbtMy = (RadioButton) findViewById(R.id.radioMy);
        rbtInfo = (RadioButton) findViewById(R.id.radioInfo);

        //监听事件：为底部的RadioGroup绑定状态改变的监听事件
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = 0;
                switch (checkedId) {
                    case R.id.radioInfo:
                        index = 0;
                        break;
                    case R.id.radioPart:
                        index = 1;
                        break;
                    case R.id.radioChoose:
                        index = 2;
                        break;
                    case R.id.radioShop:
                        index = 3;
                        break;
                    case R.id.radioMy:
                        index = 4;
                        break;
                    }
                //通过fragments这个adapter还有index来替换帧布局中的内容
                 Fragment fragment = (Fragment) fragments.instantiateItem(mHomeContent, index);
                //一开始将帧布局中 的内容设置为第一个
                fragments.setPrimaryItem(mHomeContent, 0, fragment);
                fragments.finishUpdate(mHomeContent);
               }
            });
        }
    protected void onStart() {
        super.onStart();
        radioGroup.check(R.id.radioInfo);
        }

    FragmentStatePagerAdapter fragments = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 5;
            }

        //进行Fragment的初始化
        @Override
        public Fragment getItem(int i) {
            Fragment fragment = null;
            switch (i) {
                case 0:
                    fragment = new InfoFragment();
                    break;
                case 1://发现
                    fragment = new PartFragment();
                    break;
                case 2://发现
                    fragment = new ChooseFragment();
                    break;
                case 3://发现
                    fragment = new ShopFragment();
                    break;
                case 4://发现
                    fragment = new MyFragment();
                    break;
                default:
                    new PartFragment();
                    break;
                }
            return fragment;
             }
        };


}

