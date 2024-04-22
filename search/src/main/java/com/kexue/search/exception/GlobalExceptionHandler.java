package com.kexue.search.exception;

import cn.hutool.core.util.StrUtil;
import com.kexue.search.common.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseBody
    @ExceptionHandler(ServiceException.class)
    public R handleServiceException(ServiceException e) {
        System.out.println(StrUtil.format("捕获全局异常：{}", e.getMessage()));
        return R.fail(e.getMessage());
    }
}
