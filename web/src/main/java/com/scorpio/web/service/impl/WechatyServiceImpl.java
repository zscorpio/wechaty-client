package com.scorpio.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.scorpio.web.constant.CacheConst;
import com.scorpio.web.service.RedisService;
import com.scorpio.web.service.ScoketService;
import com.scorpio.web.service.WechatyService;
import com.scorpio.web.utils.CacheUtils;
import com.scorpio.web.vo.response.ContactVO;
import com.scorpio.web.vo.response.MessageVO;
import io.github.wechaty.Wechaty;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Message;
import io.github.wechaty.utils.QrcodeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @Author: Scorpio
 */
@Service
public class WechatyServiceImpl implements WechatyService {

    private static Wechaty wechaty = null;

    @Resource
    private RedisService redisService;

    @Lazy
    @Resource
    private ScoketService scoketService;

    @PostConstruct
    public void postConstruct(){

        final String token = System.getenv("WECHATY_PUPPET_SERVICE_TOKEN");
        wechaty = Wechaty.instance(token);


        wechaty.onScan((qrcode, statusScanStatus, data) -> {
            if(Objects.nonNull(qrcode)){
                System.out.println(QrcodeUtils.getQr(qrcode));
                System.out.println("Online Image: https://wechaty.github.io/qrcode/" + qrcode);
            }
        });

        wechaty.onMessage(message -> {
            Contact from = message.from();

            if(from != null){
                saveContactInCache(from);
                saveMessageInCache(message);
                scoketService.getSocketIOServer().getBroadcastOperations().sendEvent("receiverMessage", from.getId());
            }
        });

        wechaty.start(false);
    }

    @Override
    public Wechaty getWechaty() {
        return wechaty;
    }

    @Override
    public void saveContactInCache(Contact contact) {
        ContactVO contactVO = new ContactVO();
        contactVO.setId(contact.getId());
        contactVO.setName(contact.name());
        contactVO.setAvatar(contact.avatar().getName());
        contactVO.setType(contact.type().getCode());
        redisService.zSetAdd(CacheConst.KEY_CONVERSATION_LIST, contact.getId(),  new Date().getTime());
        redisService.hSet(CacheConst.KEY_CONTACT_INFO, contact.getId(), JSON.toJSONString(contactVO));
    }

    @Override
    public void saveMessageInCache(Message message) {
        long time = new Date().getTime();
        Contact from = message.from();
        Contact to = message.to();
        String text = message.text();
        if(from != null && to != null && StringUtils.isNoneEmpty(text) && !text.contains("messageSvrId")){
            MessageVO messageVO = new MessageVO();
            messageVO.setFrom(from.getId());
            messageVO.setTo(to.getId());
            messageVO.setTime(time);
            messageVO.setContent(text);
            redisService.zSetAdd(CacheUtils.formatKey(CacheConst.KEY_MESSAGE_LIST, messageVO.getCoversationId()), JSON.toJSONString(messageVO), time);
        }
    }

    @Override
    public void sendText(String content, String id) {
        long time = new Date().getTime();
        String selfId = getWechaty().userSelf().getId();

        MessageVO messageVO = new MessageVO();
        messageVO.setFrom(selfId);
        messageVO.setTo(id);
        messageVO.setTime(time);
        messageVO.setContent(content);
        redisService.zSetAdd(CacheUtils.formatKey(CacheConst.KEY_MESSAGE_LIST, messageVO.getCoversationId()), JSON.toJSONString(messageVO), time);


        Contact to = new Contact(wechaty, id);
        to.say(content);

    }
}
