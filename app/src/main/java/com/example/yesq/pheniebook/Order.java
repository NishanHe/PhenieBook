package com.example.yesq.pheniebook;

import cn.bmob.v3.BmobObject;



public class Order extends BmobObject {
    private Integer orderid,objnum,objid;
    private Float orderprice;
    private String userid;
    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getObjnum() {
        return objnum;
    }

    public void setObjnum(Integer objnum) {
        this.objnum = objnum;
    }

    public Integer getObjid() {
        return objid;
    }

    public void setObjid(Integer objid) {
        this.objid = objid;
    }

    public Float getOrderprice() {
        return orderprice;
    }

    public void setOrderprice(Float orderprice) {
        this.orderprice = orderprice;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}