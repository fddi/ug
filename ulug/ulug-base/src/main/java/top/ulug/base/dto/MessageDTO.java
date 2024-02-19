package top.ulug.base.dto;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * DTO 基本对象
 * Created by fddiljf on 2017/4/14.
 * 逝者如斯夫 不舍昼夜
 */
public class MessageDTO<T> implements Serializable {
    private String topic;
    private String receiver;
    private String handler;
    private String msgCode;
    private String msgNote;
    private T data;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }


    public String getMsgNote() {
        return msgNote;
    }

    public void setMsgNote(String msgNote) {
        this.msgNote = msgNote;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
