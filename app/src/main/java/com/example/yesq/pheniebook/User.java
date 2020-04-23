package com.example.yesq.pheniebook;

import android.provider.ContactsContract;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;

/**
 * Created by Yesq on 2017/6/25.
 */

public class User extends BmobUser {
    ContactsContract.CommonDataKinds.Relation userorder;
    ContactsContract.CommonDataKinds.Relation userbooking;
//    String userID;
//    String username;
    String userpass;
    Double balance;

    public ContactsContract.CommonDataKinds.Relation getUserorder(){
        return userorder;
    }
    public void setUserorder(ContactsContract.CommonDataKinds.Relation userorder){
        this.userorder = userorder;
    }
    public ContactsContract.CommonDataKinds.Relation getUserbooking(){
        return userbooking;
    }
    public void setUserbooking(ContactsContract.CommonDataKinds.Relation userbooking){
        this.userbooking = userbooking;
    }
//    public String getUserID(){
//        return userID;
//    }
//    public void setUserID(String userID){
//        this.userID = userID;
//    }
//    public String getUsername(){
//        return username;
//    }
//    public void setUserName(String username){
//        this.username = username;
//    }
    public String getUserpass(){
        return userpass;
    }
    public void setUserpass(String userpass){
        this.userpass = userpass;
    }
    public Double getBalance(){
        return balance;
    }
    public void setBalance(Double balance){
        this.balance = balance;
    }


}
