package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment implements View.OnClickListener{
    TextView userid,username,userbalance;
    Button myCourt,myobject,myAppoint,cpassword,aboutUs;
    List<User> userMsg;

    private OnFragmentInteractionListener mListener;

    public MyFragment() {
    }


    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bmob.initialize(getContext(), "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        userid= (TextView) view.findViewById(R.id.tv_userid);
        username= (TextView)view.findViewById(R.id.tv_username);
        userbalance= (TextView)view.findViewById(R.id.tv_balance);
        view.findViewById(R.id.btn_myorder).setOnClickListener(this);
        view.findViewById(R.id.btn_mypartner).setOnClickListener(this);
        view.findViewById(R.id.btn_myshop).setOnClickListener(this);
        view.findViewById(R.id.btn_cpass).setOnClickListener(this);
        view.findViewById(R.id.btn_aboutUs).setOnClickListener(this);
        userMsg =new ArrayList<User>();
        return view;
    }
    public void   onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userid.setText("14846666");
        queryUser();

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void queryUser(){
        BmobQuery<User> bookQuery = new BmobQuery<>();
        bookQuery.addWhereEqualTo("userID", "14846666");
        bookQuery.setLimit(50);
        bookQuery.findObjects(new FindListener<User>()  {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        userMsg=list;
                        Log.i("bmob", "用户余额"+String.valueOf(list.get(0).getBalance()) );
                        username.setText(userMsg.get(0).getUsername());
                        userbalance.setText(String.valueOf(userMsg.get(0).getBalance()));
                    } else {
                        Toast.makeText(getActivity(), "没有查询到相关用户记录", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Log.i("bmob", "查询用户信息失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        Intent userIntent;
        switch(id){
            case R.id.btn_myorder:
                userIntent = new Intent(getActivity(), MyOrderActivity.class);
                userIntent.putExtra("key_courtType", "乒乓球");

                startActivityForResult(userIntent, 1);
                break;
            case R.id.btn_mypartner:
                userIntent = new Intent(getActivity(), MyPartnerActivity.class);
                startActivityForResult(userIntent, 2);
                break;
            case R.id.btn_myshop:
                userIntent = new Intent(getActivity(), MyShopActivity.class);
                startActivityForResult(userIntent, 3);
                break;
            case R.id.btn_cpass:
                userIntent = new Intent(getActivity(), ChangePassActivity.class);
                startActivityForResult(userIntent, 4);
                break;
            case R.id.btn_aboutUs:
                userIntent = new Intent(getActivity(), AboutUsActivity.class);
                startActivityForResult(userIntent, 4);
                break;
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }
}
