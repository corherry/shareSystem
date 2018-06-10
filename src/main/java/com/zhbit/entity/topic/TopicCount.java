package com.zhbit.entity.topic;

public class TopicCount {

    private int topicId;

    private int transmitCount;

    private int commentCount;

    private int loveCount;

    private int preTopicId;

    public TopicCount(){

    }

    public TopicCount(int topicId, int transmitCount, int commentCount, int loveCount, int preTopicId){
        this.topicId = topicId;
        this.transmitCount = transmitCount;
        this.commentCount = commentCount;
        this.loveCount = loveCount;
        this.preTopicId = preTopicId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getTransmitCount() {
        return transmitCount;
    }

    public void setTransmitCount(int transmitCount) {
        this.transmitCount = transmitCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLoveCount() {
        return loveCount;
    }

    public void setLoveCount(int loveCount) {
        this.loveCount = loveCount;
    }

    public int getPreTopicId() {
        return preTopicId;
    }

    public void setPreTopicId(int preTopicId) {
        this.preTopicId = preTopicId;
    }
}
