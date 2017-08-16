package com.nowcoder.async;

/**
 * Created by xyuser on 2017/5/11.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
}
