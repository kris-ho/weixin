package com.hks.weixin.pojo;

public class WxUser {
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public WxUser(String openid) {
        this.openid = openid;
    }
}
