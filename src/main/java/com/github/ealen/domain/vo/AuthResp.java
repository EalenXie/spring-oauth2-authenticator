package com.github.ealen.domain.vo;

/**
 * @author EalenXie create on 2021/2/1 10:46
 */
public class AuthResp {

    private int status;

    private String message;


    public AuthResp(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public AuthResp() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
