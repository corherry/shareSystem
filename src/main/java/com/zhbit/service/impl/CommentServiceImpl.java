package com.zhbit.service.impl;

import com.zhbit.dao.CommentDao;
import com.zhbit.dao.ExperienceDao;
import com.zhbit.dao.TopicDao;
import com.zhbit.dao.UserDao;
import com.zhbit.entity.comment.Comment;
import com.zhbit.entity.comment.CommentInfo;
import com.zhbit.entity.comment.ReplyInfo;
import com.zhbit.entity.topic.SearchTopicInfo;
import com.zhbit.service.CommentService;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.SensitiveWordFilter;
import com.zhbit.util.SolrUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    @Resource(name = "commentDao")
    private CommentDao commentDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Resource(name = "experienceDao")
    private ExperienceDao experienceDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public int add(Comment comment){
        comment.setAddtime(CalendarUtil.getCurrentDateTime());
        comment.setUptime(CalendarUtil.getCurrentDateTime());
        int count = commentDao.add(comment);
        int point = 0;
        if(count > 0) {
            int isFirst = commentDao.queryIsFirstComment(comment.getUserId(), comment.getTopicId());
            if (isFirst == 1) {
                point = experienceDao.queryPointByType("comment");
                userDao.updateUserExperiencePoint(comment.getUserId(), point);
            }
        }

        return point;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public int delete(int id) {
        return commentDao.delete(id);
    }

    public List<CommentInfo> queryComment(int topicId, int page, int size) {
        List<CommentInfo> commentInfoList = commentDao.queryComment(topicId, page, size);
        for(int i = 0; i < commentInfoList.size(); i++){
            CommentInfo commentInfo = commentInfoList.get(i);
            commentInfo.setAddtime(CalendarUtil.formatShowDateTime(commentInfo.getAddtime()));
            commentInfo.setContent(SensitiveWordFilter.replaceSenstiveWords(commentInfo.getContent()));
            commentInfoList.set(i, commentInfo);
        }
        return commentInfoList;
    }

    public int queryCommentCount(int topicId) {
        return commentDao.queryCommentCount(topicId);
    }
}
