package com.zhbit.util;

import com.zhbit.entity.page.Page;
import com.zhbit.entity.topic.SearchTopicInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SolrUtil {

    public static <T> void addBean(T obj, SolrClient solrClient) {
        try {
            solrClient.addBean(obj);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteById(String id, SolrClient solrClient) {
        try {
            solrClient.deleteById(id);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T>Page<T> query(SolrClient server, String solrql, int pageNum, int pageSize, Class<T> clzz){
        SolrQuery query = new SolrQuery();
        query.setQuery(solrql);
        query.setStart((pageNum-1)*pageSize);
        query.setRows(pageSize);
        query.setHighlight(true);
        query.setHighlightSimplePre("<font style=\"color:red\">");
        query.setHighlightSimplePost("</font>");
        query.setSort("addtime", SolrQuery.ORDER.desc);
        QueryResponse response = null;
        try {
            response = server.query(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //查询到的记录总数
        int totalRow = Long.valueOf(response.getResults().getNumFound()).intValue();
        //查询结果集
        List<T> items = (List<T>) toBeanList(response.getResults(), clzz);
        //填充page对象
        return new Page<T>(pageNum, pageSize, totalRow, items);
    }

    public static Object toBean(SolrDocument record, Class clazz){
        Object obj = null;
        try {
            obj = clazz.newInstance();
        } catch (InstantiationException e1) {
            e1.printStackTrace();
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        Field[] fields = clazz.getDeclaredFields();
        for(Field field:fields){
            Object value = record.get(field.getName());
            try {
                BeanUtils.setProperty(obj, field.getName(), value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    public static Object toBeanList(SolrDocumentList records, Class clazz){
        List list = new ArrayList();
        for(SolrDocument record : records){
            list.add(toBean(record,clazz));
        }
        return list;
    }


}
