package com.hks.weixin.pojo;

public class TbWechatReplymsg {
    private Integer id;

    private String replyKey;

    private String replyValue;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReplyKey() {
        return replyKey;
    }

    public void setReplyKey(String replyKey) {
        this.replyKey = replyKey == null ? null : replyKey.trim();
    }

    public String getReplyValue() {
        return replyValue;
    }

    public void setReplyValue(String replyValue) {
        this.replyValue = replyValue == null ? null : replyValue.trim();
    }
}