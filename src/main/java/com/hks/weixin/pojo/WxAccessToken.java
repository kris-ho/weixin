package com.hks.weixin.pojo;

public class WxAccessToken {
    private String wxAccessToken;
    private long expireTime;

    public String getWxAccessToken() {
        return wxAccessToken;
    }

    public void setWxAccessToken(String accessToken) {
        this.wxAccessToken = accessToken;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public WxAccessToken(String accessToken, String expireIn) {
        super();
        this.wxAccessToken = accessToken;
        expireTime = System.currentTimeMillis() + Integer.parseInt(expireIn) * 1000;
    }

    /**
     * 判断token是否过期
     * @return
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime;
    }
}
