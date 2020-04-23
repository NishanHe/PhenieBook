package com.example.yesq.pheniebook;

import java.util.Date;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;


public class Booking extends BmobObject {
    private Integer bookid,userid,cost;
    private String courtid,timeEnd ;


    public Integer getBookid() {
        return bookid;
    }
    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public Integer getUserid() {
        return userid;
    }
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getCost() {
        return cost;
    }
    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public String getCourtid() {
        return courtid;
    }
    public void setCourtid(String courtid) {
        this.courtid = courtid;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

}
