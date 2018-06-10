package com.zhbit.service;

import com.zhbit.entity.comment.Comment;
import com.zhbit.entity.comment.CommentInfo;
import com.zhbit.entity.comment.ReplyInfo;

import java.util.List;

public interface CommentService {

    int add(Comment comment);

    int delete(int id);

    List<CommentInfo> queryComment(int topicId, int page, int size);

    int queryCommentCount(int topicId);

}
