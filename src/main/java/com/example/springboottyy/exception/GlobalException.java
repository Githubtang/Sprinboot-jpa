package com.example.springboottyy.exception;

import com.example.springboottyy.utils.ApiResponse;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 0:33
 * @Version: 1.0
 */
@RestControllerAdvice
public class GlobalException {
    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    /*403异常 (用户未认证)*/
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        ApiResponse<Object> response = new ApiResponse<>("403", "Access Denied", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 处理404
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.error("没有找到资源:{}", e.getMessage());
        ApiResponse<Object> response = new ApiResponse<>("404", "Resource not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> exception(Exception e) {
        ApiResponse<Object> response = new ApiResponse<>("500", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    /**
     * 请求方法不对
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<?>> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ApiResponse<Object> response = new ApiResponse<>("405", "请求方法不对", null);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiResponse<?>> serviceException(ServiceException e) {
        log.error(e.getMessage(), e);
        ApiResponse<Object> response = new ApiResponse<>("500", e.getMessage(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<?>> handleMissingPathVariableException(MissingPathVariableException e) {
        log.error("请求路径中缺少必要参数:{}", e.getMessage(), e);
        ApiResponse<Object> response = new ApiResponse<>("404", "Missing PathVariable", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("请求参数类型不匹配:{}", e.getMessage(), e);
        ApiResponse<Object> response = new ApiResponse<>("405", "请求参数不匹配", null);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }
}