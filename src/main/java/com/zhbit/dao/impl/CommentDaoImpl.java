package com.zhbit.dao.impl;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.zhbit.dao.CommentDao;
import com.zhbit.entity.comment.Comment;
import com.zhbit.entity.comment.CommentInfo;
import com.zhbit.entity.comment.ReplyInfo;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.PageUtil;
import com.zhbit.util.TargetRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("commentDao")
public class CommentDaoImpl implements CommentDao{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int add(Comment comment) {
        String sql = "insert into comment(user_id, topic_id, reply_id, content, created_at, updated_at) values(?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, new Object[]{comment.getUserId(), comment.getTopicId(), comment.getReplyId(), comment.getContent(), comment.getAddtime(), comment.getUptime()});
    }

    public int queryIsFirstComment(int userId, int topicId) {
        String sql = "select count(*) from comment where user_id=? and topic_id=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId, topicId}, Integer.class);
    }

    public int delete(int id) {
        String sql = "update comment set is_deleted=?, updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{1, CalendarUtil.getCurrentDateTime(), id});
    }

    public List<CommentInfo> queryComment(int topicId, int page, int size) {
        return jdbcTemplate.query(PageUtil.getPageSql("c.id,user_id userId,reply_id replyId,content,c.created_at addtime, user_name userName, headPic,(select count(*) from comment where reply_id=c.id) replyCount",
                "comment c, user u",
                "and u.id=c.user_id and topic_id=? and reply_id=? and is_deleted=?","addtime", "desc"),
                new Object[]{topicId, 0, 0, (page - 1) * size, size}, new TargetRowMapper<CommentInfo>(CommentInfo.class));
    }

    public int queryCommentCount(int topicId) {
        String sql = "select count(*) from comment where topic_id=? and is_deleted=?";
        return jdbcTemplate.queryForObject(sql, new Object[]{topicId, 0}, Integer.class);
    }
}
