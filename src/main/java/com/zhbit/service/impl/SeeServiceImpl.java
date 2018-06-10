package com.zhbit.service.impl;

import com.zhbit.dao.SeeDao;
import com.zhbit.dao.TopicDao;
import com.zhbit.entity.topic.SearchTopicInfo;
import com.zhbit.service.SeeService;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.RedisUtil;
import com.zhbit.util.SolrUtil;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("seeService")
public class SeeServiceImpl implements SeeService {

    @Resource(name = "seeDao")
    private SeeDao seeDao;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional(propagation = Propagation.REQUIRED)
    public int updateSeeCount(int userId, int topicId) {

        int num = seeDao.querySeeCount(userId, topicId);
        if(num == 0){
            int result = seeDao.add(userId, topicId);
            if(result > 0){
                redisUtil.set("see:userId:"+userId+":topicId:"+topicId, String.valueOf(num + 1), 20 * 60);
            }
        }else{
            if(redisUtil.hasKey("see:userId:"+userId+":topicId:"+topicId) == false){
                int result = seeDao.updateSeeCount(userId, topicId);
                if(result > 0){
                    redisUtil.set("see:userId:"+userId+":topicId:"+topicId, String.valueOf(num + 1), 20 * 60);
                }

            }
        }
        return num + 1;
    }
}
