package com.notayessir.bean;

public class SignInResult {

    private boolean success;

    private String accessToken;

    private String content;



    public SignInResult(boolean success, String accessToken) {
        this.success = success;
        this.accessToken = accessToken;
//        for (int i = 0; i < 500; i++) {
//            this.content += "content:" + i;
//        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
