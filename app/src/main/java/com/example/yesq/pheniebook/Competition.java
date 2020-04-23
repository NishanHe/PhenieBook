package com.example.yesq.pheniebook;

import cn.bmob.v3.BmobObject;



public class Competition extends BmobObject {
    private String title;
    private String id;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
