package com.example.androidbarberstaffapp.model;

import com.example.androidbarberstaffapp.Common.Common;

public class MyToken {
    private String userPhone;
    private Common.TOKEN_TYPE token_type;
    private String token;

    public MyToken(String userPhone, Common.TOKEN_TYPE token_type, String token) {
        this.userPhone = userPhone;
        this.token_type = token_type;
        this.token = token;
    }

    public MyToken() {
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Common.TOKEN_TYPE getToken_type() {
        return token_type;
    }

    public void setToken_type(Common.TOKEN_TYPE token_type) {
        this.token_type = token_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
