package com.mit.campus.rest.algorithm.model;

/**
 * 
* 异常设计
* @author shuyy
* @date 2018年9月10日
 */
@SuppressWarnings("serial")
public class NumberInvalieException extends Exception {   
    private String cause;   
       
    public NumberInvalieException(String cause){   
        if(cause == null || "".equals(cause)){   
            this.cause = "unknow";   
        }else{   
            this.cause = cause;   
        }   
    }   
    
    @Override  
    public String toString() {   
        return "Number Invalie!Cause by " + cause;   
    }   
}  