package com.zhbit.service.impl;

import com.zhbit.dao.CollectDao;
import com.zhbit.dao.ExperienceDao;
import com.zhbit.dao.UserDao;
import com.zhbit.service.CollectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("collectService")
public class CollectServiceImpl implements CollectService{

    @Resource(name="collectDao")
    private CollectDao collectDao;

    @Resource(name = "experienceDao")
    private ExperienceDao experienceDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public int changeCollect(int userId, int topicId, int type) {
        int point = 0;
        if(type == 1){
            int result = collectDao.removeCollect(userId, topicId);
            if(result > 0){
                point = experienceDao.queryPointByType("removeCollect");
                userDao.updateUserExperiencePoint(userId, point);
            }
        }else{
            int result = collectDao.addCollect(userId, topicId);
            if(result > 0){
                point = experienceDao.queryPointByType("collect");
                userDao.updateUserExperiencePoint(userId, point);
            }
        }
        return point;
    }
}
