package com.zhbit.dao;

import com.zhbit.entity.comment.Comment;
import com.zhbit.entity.comment.CommentInfo;
import com.zhbit.entity.comment.ReplyInfo;

import java.util.List;

public interface CommentDao {

    int add(Comment comment);

    int queryIsFirstComment(int userId, int topicId);

    int delete(int id);

    List<CommentInfo> queryComment(int topicId, int page, int size);

    int queryCommentCount(int topicId);
}
