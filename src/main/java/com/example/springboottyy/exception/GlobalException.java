package com.example.springboottyy.exception;

import com.example.springboottyy.common.constant.HttpStatus;
import com.example.springboottyy.common.core.text.Convert;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.StringUtils;
import com.example.springboottyy.utils.html.EscapeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @Author: Insight
 * @Description: 全局异常处理
 * @Date: 2024/10/13 0:33
 * @Version: 1.0
 */
@RestControllerAdvice
public class GlobalException {
    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    /**
     * 权限校验异常
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponse<?> accessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("请求地址错误'{}' ,权限校验失败'{}'", uri, e.getMessage());
        return ApiResponse.error(HttpStatus.FORBIDDEN, e.getMessage());
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponse<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                          HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", uri, e.getMethod());
        return ApiResponse.error(e.getMessage());
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public ApiResponse<?> handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        if (StringUtils.isNull(code)) {
            return ApiResponse.error(e.getMessage());
        }
        return ApiResponse.error(code, e.getMessage());
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException.class)
    public ApiResponse<?> handleMissingPathVariableException(MissingPathVariableException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("请求路径中缺少必需的路径变量'{}',发生系统异常.", uri, e);
        return ApiResponse.error(String.format("请求路径中缺少必需的路径变量[%s]", e.getVariableName()));
    }
    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String value = Convert.toStr(e.getValue());
        if (StringUtils.isNotEmpty(value)) {
            value = EscapeUtil.clean(value);
        }
        log.error("请求参数类型不匹配'{}',发生系统异常.", requestURI, e);
        return ApiResponse.error(String.format("请求参数类型不匹配，参数[%s]要求类型为：'%s'，但输入值为：'%s'", e.getName(),
                e.getRequiredType().getName(), value));
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return ApiResponse.error(e.getMessage());
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return ApiResponse.error(e.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public ApiResponse<?> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ApiResponse.error(message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ApiResponse.error(message);
    }

}

/* version : 1.0

 */
/*403异常 (用户未认证)*//*

@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
    ApiResponse<Object> response = new ApiResponse<>(403, "Access Denied", null);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
}

*/
/**
 * 处理404
 * <p>
 * 请求方法不对
 * <p>
 * 业务异常
 * <p>
 * 请求路径中缺少必需的路径变量
 * <p>
 * 请求参数类型不匹配
 * <p>
 * 请求方法不对
 * <p>
 * 业务异常
 * <p>
 * 请求路径中缺少必需的路径变量
 * <p>
 * 请求参数类型不匹配
 *//*

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e) {
    log.error("没有找到资源:{}", e.getMessage());
    ApiResponse<Object> response = new ApiResponse<>(404, "Resource not found", null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}

@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponse<?>> exception(Exception e) {
    ApiResponse<Object> response = new ApiResponse<>(500, e.getMessage(), null);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
}

*/
/**
 * 请求方法不对
 *//*

@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
public ResponseEntity<ApiResponse<?>> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
    ApiResponse<Object> response = new ApiResponse<>(405, "请求方法不对", null);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
}

*/
/**
 * 业务异常
 *//*

@ExceptionHandler(ServiceException.class)
public ApiResponse<?> serviceException(ServiceException e) {
    log.error(e.getMessage(), e);
    Integer code = e.getCode();
    ApiResponse<Object> response = ApiResponse.error(code, e.getMessage());
    return response;
}


*/
/**
 * 请求路径中缺少必需的路径变量
 *//*

@ExceptionHandler(MissingPathVariableException.class)
public ResponseEntity<ApiResponse<?>> handleMissingPathVariableException(MissingPathVariableException e) {
    log.error("请求路径中缺少必要参数:{}", e.getMessage(), e);
    ApiResponse<Object> response = new ApiResponse<>(404, "Missing PathVariable", null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
}

*/
/**
 * 请求参数类型不匹配
 *//*

@ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<ApiResponse<?>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    log.error("请求参数类型不匹配:{}", e.getMessage(), e);
    ApiResponse<Object> response = new ApiResponse<>(405, "请求参数不匹配", null);
    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
}*/
