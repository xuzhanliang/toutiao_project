package com.nowcoder.service;

import com.nowcoder.dao.MessageDAO;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;

/**
 * Created by xyuser on 2017/5/8.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(Message message){
        return messageDAO.addMessage(message);
    }
    public List<Message>getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    public List<Message>getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }
    public int getConversationCount(int from,int to){
        String conversationId = from+"_"+to;
        return messageDAO.getConversationCount(conversationId);
    }
    public int getConversationUnReadCount(int userId,String conversationId){
        return messageDAO.getConversationUnReadCount(userId,conversationId);
    }
    public void emptyUnreadCount(String conversationId){
        messageDAO.updateConversationHasRead(conversationId);
    }
}
