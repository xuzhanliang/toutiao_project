package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.News;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.ToutiaoService;
import com.nowcoder.service.UserService;
import org.apache.catalina.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * Created by xyuser on 2017/4/22.
 */
@Controller
public class HomeController {
    @Autowired
    ToutiaoService toutiaoService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String index(Model model,
                        @RequestParam(value = "pop", defaultValue = "0") int pop) {
        model.addAttribute("vos", getNews(0, 0, 10));
        model.addAttribute("pop", pop);
        return "home";
    }

    @RequestMapping(path={"/user/{userId}"},method = {RequestMethod.GET,RequestMethod.POST})
    public String userIndex(@PathVariable("userId") int userId,
                        Model model){
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }


    private List<ViewObject> getNews(int userId, int offset, int limit) {
        List<News> newsList = toutiaoService.getLatestNews(userId, offset, limit);
        int localUserId = hostHolder.getUser() !=null ? hostHolder.getUser().getId() :0;
        List<ViewObject> vos = new ArrayList<>();
        for (News news : newsList) {
            ViewObject vo = new ViewObject();
            vo.set("news", news);
            vo.set("user", userService.getUser(news.getUserId()));

            if(localUserId != 0){
                vo.set("like",likeService.getLikeStatus(localUserId, EntityType.ENTITY_NEWS,news.getId()));
            }else{
                vo.set("like",0);
            }
            vos.add(vo);
        }
        return vos;
    }


}
