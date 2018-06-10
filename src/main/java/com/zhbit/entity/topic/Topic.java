package com.zhbit.entity.topic;

public class Topic {

    private int id;

    private String content;

    private String pic;

    private String title;

    private int topicClassId;

    private int transmitId;

    private int userId;

    private int isDeleted;

    private int authority;

    private String addtime;

    private String uptime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTopicClassId() {
        return topicClassId;
    }

    public void setTopicClassId(int topicClassId) {
        this.topicClassId = topicClassId;
    }

    public int getTransmitId() {
        return transmitId;
    }

    public void setTransmitId(int transmitId) {
        this.transmitId = transmitId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getAuthority() {
        return authority;
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }
}
