package com.scorpio.web.service;

import io.github.wechaty.Wechaty;
import io.github.wechaty.user.Contact;
import io.github.wechaty.user.Message;

/**
 * @Author: Scorpio
 */
public interface WechatyService {
    Wechaty getWechaty();

    void saveContactInCache(Contact contact);

    void saveMessageInCache(Message message);

    void sendText(String content, String id);
}
