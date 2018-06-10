package com.zhbit.controller;

import com.zhbit.entity.comment.Comment;
import com.zhbit.entity.comment.CommentInfo;
import com.zhbit.entity.comment.ReplyInfo;
import com.zhbit.service.CommentService;
import com.zhbit.util.CalendarUtil;
import com.zhbit.util.JsonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

@Controller
@Scope("prototype")
@RequestMapping("/commentController")
public class CommentController {

    @Resource(name = "commentService")
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Comment comment){
        int point = commentService.add(comment);
        return JsonUtil.toJson("point", point);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String delete(int id){
        int count = commentService.delete(id);
        return JsonUtil.toJson("isSuccess", count);
    }

    @ResponseBody
    @RequestMapping(value = "/queryComment", method = RequestMethod.GET)
    public String queryComment(int topicId, int page, int size){
        List<CommentInfo> commentInfoList = commentService.queryComment(topicId, page, size);
        return JsonUtil.toJson("commentInfoList", commentInfoList);
    }

    @ResponseBody
    @RequestMapping(value = "/queryCommentCount", method = RequestMethod.GET)
    public String queryCommentCount(int topicId){
        int count = commentService.queryCommentCount(topicId);
        return JsonUtil.toJson("count", count);
    }
}
