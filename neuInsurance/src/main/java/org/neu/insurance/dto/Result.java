package org.neu.insurance.dto;

import lombok.Data;

/**
 * 统一响应类
 */
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;
    
    public static <T> Result<T> success(T data) {
        Result<T> response = new Result<>();
        response.setCode(200);
        response.setMessage("操作成功");
        response.setData(data);
        return response;
    }
    
    public static <T> Result<T> success(String message, T data) {
        Result<T> response = new Result<>();
        response.setCode(200);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
    
    public static <T> Result<T> error(String message) {
        Result<T> response = new Result<>();
        response.setCode(500);
        response.setMessage(message);
        return response;
    }
    
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> response = new Result<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }
} 