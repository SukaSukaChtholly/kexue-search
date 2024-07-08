package com.kexue.common.util;


import java.util.concurrent.Callable;

public class AsyncUtils {
    
    public static void submit(Callable callable, String errMsg) {
        try {
            callable.call();
        } catch (Exception e) {
            
        }
    }
}
