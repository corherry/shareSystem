package com.zhbit.service.impl;

import com.zhbit.dao.ExperienceDao;
import com.zhbit.dao.LoveDao;
import com.zhbit.dao.TopicDao;
import com.zhbit.dao.UserDao;
import com.zhbit.entity.topic.SearchTopicInfo;
import com.zhbit.service.LoveService;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.SolrUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("loveService")
public class LoveServiceImpl implements LoveService {

    @Resource(name="loveDao")
    private LoveDao loveDao;

    @Resource(name = "experienceDao")
    private ExperienceDao experienceDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateLove(int userId, int topicId, int op) {
        int point = 0;
        if(op == 1){
            int result = loveDao.deleteLove(userId, topicId);
            if(result > 0){
                point = experienceDao.queryPointByType("deleteLove");
                userDao.updateUserExperiencePoint(userId, point);
            }
        }else{
            int result = loveDao.addLove(userId, topicId);
            if(result > 0){
                point = experienceDao.queryPointByType("addLove");
                userDao.updateUserExperiencePoint(userId, point);
            }
        }

        return point;
    }
}
