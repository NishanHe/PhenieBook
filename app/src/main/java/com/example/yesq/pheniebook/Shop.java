package com.example.yesq.pheniebook;

import java.io.File;

import cn.bmob.v3.BmobObject;

/**
 * Created by Jacqueline on 2017/6/23.
 */

public class Shop extends BmobObject {

    String objectid;
    Integer objid;
    String objname;
    Integer objnum;
    Float objprice;
    File image;


    public Shop(){
        this.setTableName("shop");
    }
    public String getObjectid(){
        return objectid;
    }
    public void setObjectid(String objectid){
        this.objectid = objectid;
    }
    public Integer getObjid(){
        return objid;
    }
    public void setObjid(Integer objid){
        this.objid = objid;
    }
    public String getObjname(){
        return objname;
    }
    public void setObjname(String objname){
        this.objname = objname;
    }
    public Integer getObjnum(){
        return objnum;
    }
    public void setObjnum(Integer objnum){
        this.objnum = objnum;
    }
    public Float getObjprice(){
        return objprice;
    }
    public void setObjprice(Float objprice){
        this.objprice = objprice;
    }
    public File getImage(){
        return image;
    }
    public void setApp_time(File image){
        this.image = image;
    }

}

