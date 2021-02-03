package com.github.ealen.domain.vo;

/**
 * @author EalenXie create on 2021/2/1 10:46
 */
public class RespBody<T> {

    private int status;

    private String message;

    private T data;

    public RespBody(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public RespBody() {
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
