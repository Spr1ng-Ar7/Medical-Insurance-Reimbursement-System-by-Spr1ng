package org.neu.insurance.dto;

import lombok.Data;

/**
 * 分页请求类
 */
@Data
public class PageRequest {
    private Integer pageNum = 1;        // 页码，默认第1页
    private Integer pageSize = 10;      // 每页大小，默认10条
    private String sortField;           // 排序字段
    private String sortOrder = "asc";   // 排序方向：asc/desc
    
    public Integer getOffset() {
        return (pageNum - 1) * pageSize;
    }
} 