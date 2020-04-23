package com.example.yesq.pheniebook;

import android.provider.ContactsContract;

import java.sql.Time;
import java.util.Date;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;


public class Appoint extends BmobObject {

    User user;
    String type;
    Integer id;
    String content;
    BmobDate app_time;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }
    public BmobDate getApp_time(){
        return app_time;
    }
    public void setApp_time(BmobDate app_time){
        this.app_time = app_time;
    }


}
