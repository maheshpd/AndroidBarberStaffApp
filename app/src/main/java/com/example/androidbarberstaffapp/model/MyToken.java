package com.example.androidbarberstaffapp.model;

import com.example.androidbarberstaffapp.Common.Common;

public class MyToken {
    private String token,user;
    private Common.TOKEN_TYPE token_type;

    public MyToken() {
    }

    public MyToken(String token, String user, Common.TOKEN_TYPE token_type) {
        this.token = token;
        this.user = user;
        this.token_type = token_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Common.TOKEN_TYPE getToken_type() {
        return token_type;
    }

    public void setToken_type(Common.TOKEN_TYPE token_type) {
        this.token_type = token_type;
    }
}
