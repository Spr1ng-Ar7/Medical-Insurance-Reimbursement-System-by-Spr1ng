package org.neu.insurance.service.impl;

import org.neu.insurance.entity.ExpenseDetail;
import org.neu.insurance.service.ExpenseDetailService;
import org.neu.insurance.mapper.ExpenseDetailMapper;
import org.neu.insurance.dto.PageRequest;
import org.neu.insurance.dto.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 费用明细管理服务实现类
 */
@Slf4j
@Service
public class ExpenseDetailServiceImpl implements ExpenseDetailService {
    
    @Autowired
    private ExpenseDetailMapper expenseDetailMapper;
    
    @Override
    public ExpenseDetail getExpenseDetailById(Long id) {
        try {
            log.info("查询费用明细信息，ID: {}", id);
            return expenseDetailMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询费用明细失败，ID: {}", id, e);
            throw new RuntimeException("查询费用明细失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<ExpenseDetail> getExpenseDetailsByPatientId(Long patientId) {
        try {
            log.info("根据患者ID查询费用明细列表，patientId: {}", patientId);
            return expenseDetailMapper.selectByPatientId(patientId);
        } catch (Exception e) {
            log.error("根据患者ID查询费用明细列表失败，patientId: {}", patientId, e);
            throw new RuntimeException("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<ExpenseDetail> getExpenseDetailsByOrderId(Long orderId) {
        try {
            log.info("根据订单ID查询费用明细列表，orderId: {}", orderId);
            return expenseDetailMapper.selectByOrderId(orderId);
        } catch (Exception e) {
            log.error("根据订单ID查询费用明细列表失败，orderId: {}", orderId, e);
            throw new RuntimeException("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public PageResponse<ExpenseDetail> getExpenseDetailList(PageRequest pageRequest) {
        try {
            log.info("分页查询费用明细列表，页码: {}, 页大小: {}", pageRequest.getPageNum(), pageRequest.getPageSize());
            int offset = (pageRequest.getPageNum() - 1) * pageRequest.getPageSize();
            List<ExpenseDetail> details = expenseDetailMapper.selectPage(offset, pageRequest.getPageSize());
            long total = expenseDetailMapper.count();
            
            PageResponse<ExpenseDetail> response = new PageResponse<>();
            response.setRecords(details);
            response.setTotal(total);
            response.setPageNum(pageRequest.getPageNum());
            response.setPageSize(pageRequest.getPageSize());
            return response;
        } catch (Exception e) {
            log.error("分页查询费用明细列表失败", e);
            throw new RuntimeException("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<ExpenseDetail> getExpenseDetailsByItemType(String itemType) {
        try {
            log.info("根据项目类型查询费用明细列表，itemType: {}", itemType);
            return expenseDetailMapper.selectByItemType(itemType);
        } catch (Exception e) {
            log.error("根据项目类型查询费用明细列表失败，itemType: {}", itemType, e);
            throw new RuntimeException("查询费用明细列表失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void createExpenseDetail(ExpenseDetail expenseDetail) {
        try {
            log.info("创建费用明细，项目名称: {}", expenseDetail.getItemName());
            expenseDetail.setCreateTime(LocalDateTime.now());
            // 计算报销金额和自付金额
            if (expenseDetail.getTotalAmount() != null && expenseDetail.getReimbursementRatio() != null) {
                BigDecimal reimbursementAmount = expenseDetail.getTotalAmount()
                    .multiply(expenseDetail.getReimbursementRatio())
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                expenseDetail.setReimbursementAmount(reimbursementAmount);
                expenseDetail.setSelfPayAmount(expenseDetail.getTotalAmount().subtract(reimbursementAmount));
            }
            expenseDetailMapper.insert(expenseDetail);
            log.info("费用明细创建成功，ID: {}", expenseDetail.getId());
        } catch (Exception e) {
            log.error("创建费用明细失败，项目名称: {}", expenseDetail.getItemName(), e);
            throw new RuntimeException("创建费用明细失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void updateExpenseDetail(ExpenseDetail expenseDetail) {
        try {
            log.info("更新费用明细信息，ID: {}", expenseDetail.getId());
            expenseDetail.setUpdateTime(LocalDateTime.now());
            // 重新计算报销金额和自付金额
            if (expenseDetail.getTotalAmount() != null && expenseDetail.getReimbursementRatio() != null) {
                BigDecimal reimbursementAmount = expenseDetail.getTotalAmount()
                    .multiply(expenseDetail.getReimbursementRatio())
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);
                expenseDetail.setReimbursementAmount(reimbursementAmount);
                expenseDetail.setSelfPayAmount(expenseDetail.getTotalAmount().subtract(reimbursementAmount));
            }
            expenseDetailMapper.update(expenseDetail);
            log.info("费用明细信息更新成功");
        } catch (Exception e) {
            log.error("更新费用明细信息失败，ID: {}", expenseDetail.getId(), e);
            throw new RuntimeException("更新费用明细失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void deleteExpenseDetail(Long id) {
        try {
            log.info("删除费用明细，ID: {}", id);
            expenseDetailMapper.deleteById(id);
            log.info("费用明细删除成功");
        } catch (Exception e) {
            log.error("删除费用明细失败，ID: {}", id, e);
            throw new RuntimeException("删除费用明细失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void batchDeleteExpenseDetails(List<Long> ids) {
        try {
            log.info("批量删除费用明细，数量: {}", ids.size());
            expenseDetailMapper.batchDelete(ids);
            log.info("批量删除费用明细成功");
        } catch (Exception e) {
            log.error("批量删除费用明细失败", e);
            throw new RuntimeException("批量删除费用明细失败: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public void changeExpenseDetailStatus(Long id, Integer status) {
        try {
            log.info("更改费用明细状态，ID: {}, 状态: {}", id, status);
            expenseDetailMapper.updateStatus(id, status);
            log.info("费用明细状态更新成功");
        } catch (Exception e) {
            log.error("更改费用明细状态失败，ID: {}", id, e);
            throw new RuntimeException("更改费用明细状态失败: " + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal calculateTotalAmount(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            log.info("统计费用总额，患者ID: {}, 开始时间: {}, 结束时间: {}", patientId, startDate, endDate);
            return expenseDetailMapper.calculateTotalAmount(patientId, startDate, endDate);
        } catch (Exception e) {
            log.error("统计费用总额失败", e);
            throw new RuntimeException("统计费用总额失败: " + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal calculateReimbursementAmount(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            log.info("统计报销总额，患者ID: {}, 开始时间: {}, 结束时间: {}", patientId, startDate, endDate);
            return expenseDetailMapper.calculateReimbursementAmount(patientId, startDate, endDate);
        } catch (Exception e) {
            log.error("统计报销总额失败", e);
            throw new RuntimeException("统计报销总额失败: " + e.getMessage());
        }
    }
    
    @Override
    public BigDecimal calculateSelfPayAmount(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            log.info("统计自付总额，患者ID: {}, 开始时间: {}, 结束时间: {}", patientId, startDate, endDate);
            return expenseDetailMapper.calculateSelfPayAmount(patientId, startDate, endDate);
        } catch (Exception e) {
            log.error("统计自付总额失败", e);
            throw new RuntimeException("统计自付总额失败: " + e.getMessage());
        }
    }
    
    @Override
    public List<ExpenseDetail> getExpenseStatsByItemType(Long patientId, LocalDateTime startDate, LocalDateTime endDate) {
        try {
            log.info("根据项目类型统计费用，患者ID: {}, 开始时间: {}, 结束时间: {}", patientId, startDate, endDate);
            return expenseDetailMapper.selectExpenseStatsByItemType(patientId, startDate, endDate);
        } catch (Exception e) {
            log.error("根据项目类型统计费用失败", e);
            throw new RuntimeException("统计费用失败: " + e.getMessage());
        }
    }
} 