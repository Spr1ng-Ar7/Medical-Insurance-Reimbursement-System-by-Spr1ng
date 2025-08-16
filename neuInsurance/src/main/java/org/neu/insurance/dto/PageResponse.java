package org.neu.insurance.dto;

import lombok.Data;
import java.util.List;

/**
 * 分页响应类
 */
@Data
public class PageResponse<T> {
    private List<T> records;           // 数据列表
    private Long total;                // 总记录数
    private Integer pageNum;           // 当前页码
    private Integer pageSize;          // 每页大小
    private Integer totalPages;        // 总页数
    
    public PageResponse() {}
    
    public PageResponse(List<T> records, Long total, Integer pageNum, Integer pageSize) {
        this.records = records;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) total / pageSize);
    }
    
    // 添加明确的setter方法，确保兼容性
    public void setRecords(List<T> records) {
        this.records = records;
    }
    
    public void setTotal(Long total) {
        this.total = total;
    }
    
    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
    
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }
    
    // 添加setCurrent方法（兼容某些框架的命名）
    public void setCurrent(Integer current) {
        this.pageNum = current;
    }
    
    // 添加setSize方法（兼容某些框架的命名）
    public void setSize(Integer size) {
        this.pageSize = size;
    }
    
    // 添加getter方法，确保兼容性
    public List<T> getRecords() {
        return records;
    }
    
    public Long getTotal() {
        return total;
    }
    
    public Integer getPageNum() {
        return pageNum;
    }
    
    public Integer getPageSize() {
        return pageSize;
    }
    
    public Integer getTotalPages() {
        return totalPages;
    }
    
    // 添加getCurrent方法（兼容某些框架的命名）
    public Integer getCurrent() {
        return pageNum;
    }
    
    // 添加getSize方法（兼容某些框架的命名）
    public Integer getSize() {
        return pageSize;
    }
}