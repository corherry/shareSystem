package com.zhbit.entity.User;

import org.apache.solr.client.solrj.beans.Field;

public class SearchUserInfo {

    @Field
    private int id;

    @Field
    private String userName;

    @Field
    private String addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
