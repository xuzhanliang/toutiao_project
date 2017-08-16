package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by xyuser on 2017/5/11.
 */
@Component
public class LikeHandler implements EventHandler {
    @Autowired
    UserService userService;

    @Autowired
    MessageService messageService;

    @Override
    public void doHandler(EventModel model) {
        Message message = new Message();
        //Admin账户
        message.setFromId(2);
        message.setToId(model.getEntityOwnerId());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户"+user.getName()+"赞了你的咨询，http://127.0.0.1:8080/news/"+String.valueOf(model.getEntityId()));
        message.setCreatedDate(new Date());
        message.setConversationId("2_"+user.getId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
