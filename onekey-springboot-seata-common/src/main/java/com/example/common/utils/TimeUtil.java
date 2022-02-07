package com.example.common.utils;


import java.time.LocalDateTime;

/**
 *  时间处理类
 */
public class TimeUtil {


    public static LocalDateTime getCurrentTime(){
        LocalDateTime now = LocalDateTime.now();
        return now;
    }
}
