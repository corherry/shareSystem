package com.zhbit.dao.impl;

import com.zhbit.dao.TopicDao;
import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.*;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.PageUtil;
import com.zhbit.util.SolrUtil;
import com.zhbit.util.TargetRowMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Repository("topicDao")
public class TopicDaoImpl implements TopicDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TopicInfo> queryAllTopic(int userId, int category, int page, int size, String order) {
        return jdbcTemplate.query(PageUtil.getPageSql("u.id userId, headPic, user_name userName, t.id topicId, t.created_at addtime, title, (select type_name from topicclass where id=t.topicclass_id) categoryName, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount",
                "user u, topic t",
                "and user_id=u.id and t.is_deleted=0 and (t.user_id="+userId+" or (t.authority=3 or (t.authority=2 and "+userId+" in (select attend_id from relation where user_id=t.user_id)) or (t.authority=1 and t.user_id="+userId+")))" + (category == 0 ? "" : " and t.topicclass_id="+category+""),
                order, "desc"),
                new Object[]{(page - 1) * size, size},
                new TargetRowMapper<TopicInfo>(TopicInfo.class));
    }


    public List<TopicInfo> queryFriendTopic(int userId, int category, int page, int size, String order) {
        return jdbcTemplate.query(PageUtil.getPageSql("u.id userId, headPic, user_name userName, t.id topicId, t.created_at addtime, title, (select type_name from topicclass where id=t.topicclass_id) categoryName, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount",
                "user u, topic t",
                "and t.is_deleted=0 and u.id=t.user_id and (t.user_id in (select attend_id from relation where user_id="+userId+") and (t.authority=3 or (t.authority=2 and "+userId+" in (select attend_id from relation where user_id=t.user_id))) or t.user_id="+userId+") " + (category == 0 ? "" : " and t.topicclass_id="+category+""),
                order, "desc"),
                new Object[]{(page - 1) * size, size},
                new TargetRowMapper<TopicInfo>(TopicInfo.class));
    }

    public List<TopicInfo> queryTopicByUserId(int userId, int seeUserId, int category, int page, int size, String order) {
        if(userId == seeUserId){
            return jdbcTemplate.query(PageUtil.getPageSql("u.id userId, headPic, user_name userName, t.id topicId, t.created_at addtime, title, (select type_name from topicclass where id=t.topicclass_id) categoryName, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount",
                    "user u, topic t",
                    "and t.is_deleted=0 and u.id=t.user_id and t.user_id=?"+(category == 0 ? "" : " and t.topicclass_id="+category+""),
                    order, "desc"),
                    new Object[]{seeUserId, (page - 1) * size, size},
                    new TargetRowMapper<TopicInfo>(TopicInfo.class));
        }else{
            return jdbcTemplate.query(PageUtil.getPageSql("u.id userId, headPic, user_name userName, t.id topicId, t.created_at addtime, title, (select type_name from topicclass where id=t.topicclass_id) categoryName, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount",
                    "user u, topic t",
                    "and t.is_deleted=0 and u.id=t.user_id and t.user_id=? and (t.authority=3 or (t.authority=2 and "+userId+" in (select attend_id from relation where user_id=t.user_id))) " + (category == 0 ? "" : " and t.topicclass_id="+category+""),
                    order, "desc"),
                    new Object[]{seeUserId, (page - 1) * size, size},
                    new TargetRowMapper<TopicInfo>(TopicInfo.class));
        }
    }

    public int queryTopicCountByDate(int userId, String curDateTime) {
        String sql = "select count(*) from topic where user_id=? and created_at >= '" + curDateTime + " 00:00:00' and created_at <= '" + curDateTime + " 23:59:59'";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, Integer.class);
    }

    public int add(final Topic topic) {
        final String sql = "insert into topic(content, title, pic, user_id, topicclass_id, is_deleted, authority, created_at, updated_at) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setObject(1, topic.getContent());
                ps.setObject(2, topic.getTitle());
                ps.setObject(3, topic.getPic());
                ps.setObject(4, topic.getUserId());
                ps.setObject(5, topic.getTopicClassId());
                ps.setObject(6, topic.getIsDeleted());
                ps.setObject(7, topic.getAuthority());
                ps.setObject(8, topic.getAddtime());
                ps.setObject(9, topic.getUptime());
                return ps;
            }
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public List<TopicClass> queryTopicClass() {
        String sql = "select id, type_name typeName from topicclass";
        return jdbcTemplate.query(sql, new TargetRowMapper<TopicClass>(TopicClass.class));
    }

    public List<TopicTransmission> queryMyTopic(int userId, int page, int size) {
        return jdbcTemplate.query(PageUtil.getPageSql("id, title, created_at addtime, (select sum(num) from see where topic_id=topic.id) seeCount, (select count(*) from comment where topic_id=topic.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=topic.id) loveCount",
                "topic",
                "and user_id=? and is_deleted=0", "addtime", "desc"),
                new Object[]{userId, (page-1)*size, size}, new TargetRowMapper<TopicTransmission>(TopicTransmission.class));
    }

    public List<TopicTransmission> queryLoveTopic(int userId, int page, int size) {
        return jdbcTemplate.query(PageUtil.getPageSql("t.id, t.title, l.created_at addtime, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount",
                "topic t, love l",
                "and l.user_id=? and l.topic_id=t.id and t.is_deleted=0", "addtime", "desc"),
                new Object[]{userId, (page-1)*size, size}, new TargetRowMapper<TopicTransmission>(TopicTransmission.class));
    }

    public List<TopicTransmission> queryCollectTopic(int userId, int page, int size) {
        return jdbcTemplate.query(PageUtil.getPageSql("t.id, t.title, c.created_at addtime, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount",
                "topic t, collection c",
                "and c.user_id=? and c.topic_id=t.id and t.is_deleted=0", "addtime", "desc"),
                new Object[]{userId, (page-1)*size, size}, new TargetRowMapper<TopicTransmission>(TopicTransmission.class));
    }

    public RelationTopicCount queryRelationTopicCount(int userId) {
        String sql = "select count(*) publishCount, (select count(*) from love where user_id="+userId+") loveCount, (select count(*) from collection where user_id="+userId+") collectCount from topic where user_id=? and is_deleted=0";
        return jdbcTemplate.queryForObject(sql, new Object[]{userId}, new TargetRowMapper<RelationTopicCount>(RelationTopicCount.class));
    }

    public List<TopicRank> queryTopicSeeRank() {
        String sql = "select id, title, (select sum(num) from see where topic_id=topic.id) count from topic where is_deleted=0 order by count desc, id desc limit 0,10";
        return jdbcTemplate.query(sql, new TargetRowMapper<TopicRank>(TopicRank.class));
    }

    public List<TopicRank> queryTopicCommentRank() {
        String sql = "select id, title, (select count(*) from comment where is_deleted=0 and topic_id=topic.id) count from topic where is_deleted=0 order by count desc, id desc limit 0,10";
        return jdbcTemplate.query(sql, new TargetRowMapper<TopicRank>(TopicRank.class));
    }

    public TopicDetail queryTopicDetail(int userId, int topicId) {
        String sql = "select t.id topicId, user_id userId, title, authority, content, pic, t.created_at addtime, u.user_name userName, headPic, " +
                "(select type_name from topicclass where id=t.topicclass_id) category, " +
                "(select sum(num) from see where topic_id=t.id) seeCount, " +
                "(select count(*) from comment where is_deleted=0 and topic_id=t.id) commentCount, " +
                "(select count(*) from love where topic_id=t.id) loveCount, " +
                "(select grade_name from level where grade = (select max(grade) from level where experience_point <= u.experience_point)) level, " +
                "(select count(*) from collection where user_id="+userId+" and topic_id=t.id) isCollected, " +
                "(select count(*) from love where user_id="+userId+" and topic_id=t.id) isLoved from topic t,user u where t.id=? and t.user_id=u.id";
        return jdbcTemplate.queryForObject(sql, new Object[]{topicId}, new TargetRowMapper<TopicDetail>(TopicDetail.class));
    }

    public int delete(int id) {
        String sql = "update topic set is_deleted=?, updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{1, CalendarUtil.getCurrentDateTime(), id});
    }

    public int updateAuthority(int topicId, int authority) {
        String sql = "update topic set authority=?, updated_at=? where id=?";
        return jdbcTemplate.update(sql, new Object[]{authority, CalendarUtil.getCurrentDateTime(), topicId});
    }

    public Page<SearchTopicInfo> queryTopicByTitle(String title, int page, int size) {
        String solrUrl = "http://127.0.0.1:8983/solr/topic";
        HttpSolrClient client = new HttpSolrClient(solrUrl);
        return SolrUtil.query(client, "title:*"+title+"*", page, size, SearchTopicInfo.class);
    }


    public List<TopicInfo> queryTopicByIdStr(String idStr) {
        try {
            String sql = "select u.id userId, headPic, user_name userName, t.id topicId, t.created_at addtime, title, (select type_name from topicclass where id=t.topicclass_id) categoryName, (select sum(num) from see where topic_id=t.id) seeCount, (select count(*) from comment where topic_id=t.id and is_deleted=0) commentCount, (select count(*) from love where topic_id=t.id) loveCount from user u, topic t where u.id=t.user_id and t.id in (" + idStr + ") order by addtime desc";
            return jdbcTemplate.query(sql, new TargetRowMapper<TopicInfo>(TopicInfo.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
