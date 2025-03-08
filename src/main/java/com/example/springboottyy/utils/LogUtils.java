package com.example.springboottyy.utils;

/**
 * @Author: GithubTang
 * @Description: 处理并记录日志文件
 * @Date: 2025/3/8 2:04
 * @Version: 1.0
 */
public class LogUtils {
    public static String getBlock(Object msg)
    {
        if (msg == null)
        {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
