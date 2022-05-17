package com.scorpio.web.vo.response;

import lombok.Data;

/**
 * @Author: Scorpio
 */
@Data
public class MessageVO {
    private String from;

    private String to;

    private Long time;

    private String content;

    private String coversationId;

    private Boolean self = true;

    private String timeValue;

    public String getCoversationId(){
        if(from.compareTo(to) > 0){
            return from+"_"+to;
        }else{
            return to+"_"+from;
        }
    }
}
