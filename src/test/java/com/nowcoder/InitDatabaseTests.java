package com.nowcoder;

import com.nowcoder.dao.LoginTicketDAO;
import com.nowcoder.dao.UserDAO;
import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoProjectApplication.class)
@Sql({"/init-schema.sql"})
public class InitDatabaseTests {
	@Autowired
	private UserDAO userDAO;

	@Autowired
	NewsDAO newsDAO;
	@Autowired
	LoginTicketDAO loginTicketDAO;

	@Test
	public void InitDataBase() {
		Random r = new Random();
		for (int i = 0; i < 11; i++) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",r.nextInt(1000)));
			user.setName(String.format("USER%d",i));
			user.setPassword("");
			user.setSalt("");
			userDAO.addUser(user);


			News news = new News();
			news.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png",r.nextInt(1000)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE%d",i));
			news.setLink(String.format("http://www.nowcoder.com/%d.html",i));
			newsDAO.addNews(news);

			user.setPassword("newpassword");
			userDAO.updatePassword(user);

			LoginTicket ticket = new LoginTicket();
			ticket.setStatus(0);
			ticket.setUserId(i+1);
			ticket.setExpired(date);
			ticket.setTicket(String.format("TICKET%d",i+1));
			//loginTicketDAO.addLoginTicket(ticket);

			loginTicketDAO.updateStatus(ticket.getTicket(),2);
		}
		Assert.assertEquals("newpassword",userDAO.selectById(1).getPassword());
		userDAO.deleteById(1);
		Assert.assertNull(userDAO.selectById(1));
	}

}