package com.scorpio.web.controller;

import com.alibaba.fastjson.TypeReference;
import com.scorpio.web.constant.CacheConst;
import com.scorpio.web.service.RedisService;
import com.scorpio.web.service.WechatyService;
import com.scorpio.web.utils.CacheUtils;
import com.scorpio.web.utils.JSONUtil;
import com.scorpio.web.vo.response.ContactVO;
import com.scorpio.web.vo.response.MessageVO;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Scorpio
 */

@Service
public class WechatyController {

    @Resource
    private RedisService redisService;

    @Resource
    private WechatyService wechatyService;

    public List<ContactVO> getContactList(){
        List<ContactVO> contacts = new ArrayList<>();
        Set<String> ids = redisService.zSetReverseRange(CacheConst.KEY_CONVERSATION_LIST, 0, 19);

        if(CollectionUtils.isNotEmpty(ids)){
            contacts = redisService.hMultiGet(CacheConst.KEY_CONTACT_INFO, JSONUtil.convert(ids, new TypeReference<List<String>>(){}.getType()), ContactVO.class);
        }

        return contacts;
    }

    public List<MessageVO> getMessageList(String toId){
        SimpleDateFormat formatter = new SimpleDateFormat("mm-dd hh:mm:ss");

        List<MessageVO> messageList = new ArrayList<>();
        MessageVO messageVO = new MessageVO();
        String selfId = wechatyService.getWechaty().userSelf().getId();
        messageVO.setFrom(selfId);
        messageVO.setTo(toId);

        Set<String> messages = redisService.zSetReverseRange(CacheUtils.formatKey(CacheConst.KEY_MESSAGE_LIST, messageVO.getCoversationId()), 0, 99);

        if(CollectionUtils.isNotEmpty(messages)){
            for (String message : messages) {
                MessageVO messageTmpVO = JSONUtil.convertString(message, MessageVO.class);
                if(messageTmpVO != null){
                    messageTmpVO.setSelf(Objects.equals(selfId, messageTmpVO.getFrom()));
                    messageTmpVO.setTimeValue(formatter.format(messageTmpVO.getTime()));
                    messageList.add(messageTmpVO);
                }
            }
        }

        Collections.reverse(messageList);

        return messageList;
    }
}
