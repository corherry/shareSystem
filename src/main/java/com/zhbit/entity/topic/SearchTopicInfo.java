package com.zhbit.entity.topic;

import org.apache.solr.client.solrj.beans.Field;

public class SearchTopicInfo {

    @Field
    private int id;

    @Field
    private String title;

    @Field
    private String addtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
