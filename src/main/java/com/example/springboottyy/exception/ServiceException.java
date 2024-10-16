package com.example.springboottyy.exception;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/14 9:22
 * @Version: 1.0
 */
@Data
public final class ServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;
    private String detail;
    public ServiceException() {}
    public ServiceException(String msg,Integer code) {}
}
