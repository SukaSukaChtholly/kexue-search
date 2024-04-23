package com.kexue.search.exception;

import cn.hutool.core.util.StrUtil;
import com.kexue.common.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public R handleServiceException(ServiceException e) {
        log.error(e.getMessage(), e);
        System.out.println(StrUtil.format("捕获业务异常：{}", e));
        return R.fail(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public R handleServiceException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'[{}]'，发生系统异常：[{}]", requestURI, e);
        System.out.println(StrUtil.format("捕获系统异常：{}", e));
        return R.fail(e.getMessage());
    }
}
