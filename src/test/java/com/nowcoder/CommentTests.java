package com.nowcoder;

import com.nowcoder.dao.CommentDAO;
import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * Created by xyuser on 2017/5/7.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoProjectApplication.class)
public class CommentTests {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;

    @Autowired
    CommentDAO commentDAO;

    @Test
    public void commentTest(){

        for(int i = 9;i<=11;i++){
            News news = newsDAO.selectById(i);
            for(int j=0;j<3;j++){
                Comment comment = new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                comment.setContent("Comment "+String.valueOf(j+1));
                commentDAO.addComment(comment);
            }
        }
        Assert.assertNotNull(commentDAO.selectByEntity(1,EntityType.ENTITY_NEWS).get(0));
    }
}
