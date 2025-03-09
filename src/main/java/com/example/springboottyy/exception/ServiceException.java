package com.example.springboottyy.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @Author: Insight
 * @Description: 服务异常
 * @Date: 2024/10/14 9:22
 * @Version: 1.0
 */
@Getter
@Setter
public final class ServiceException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer code;
    private String msg;
    private String detail;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public ServiceException(String msg, Integer code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
}
