<template>
  <div class="approve-dialog">
    <ReForm :model="formData" label-width="100px">
      <el-form-item label="审核结果">
        <el-radio-group v-model="formData.approveResult">
          <el-radio label="approve">通过</el-radio>
          <el-radio label="reject">拒绝</el-radio>
        </el-radio-group>
      </el-form-item>
      
      <el-form-item label="审核意见">
        <el-input
          v-model="formData.approveComment"
          type="textarea"
          :rows="4"
          placeholder="请输入审核意见"
        />
      </el-form-item>
      
      <el-form-item label="审核级别">
        <el-select v-model="formData.approveLevel" placeholder="请选择审核级别">
          <el-option label="初级审核" value="primary" />
          <el-option label="中级审核" value="intermediate" />
          <el-option label="高级审核" value="senior" />
        </el-select>
      </el-form-item>
      
      <el-form-item label="紧急程度">
        <el-rate
          v-model="formData.urgencyLevel"
          :max="5"
          show-text
          :texts="['普通', '一般', '紧急', '很紧急', '特急']"
        />
      </el-form-item>
      
      <el-form-item label="审核选项">
        <el-checkbox v-model="formData.autoNext">自动进入下一级审核</el-checkbox>
        <el-checkbox v-model="formData.notifyPatient">通知患者</el-checkbox>
        <el-checkbox v-model="formData.recordLog">记录审核日志</el-checkbox>
      </el-form-item>
      
      <el-form-item label="医嘱信息" v-if="orderInfo">
        <div class="order-info">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="患者姓名">{{ orderInfo.patientName }}</el-descriptions-item>
            <el-descriptions-item label="医嘱类型">{{ orderInfo.orderType }}</el-descriptions-item>
            <el-descriptions-item label="开单医生">{{ orderInfo.doctorName }}</el-descriptions-item>
            <el-descriptions-item label="开单时间">{{ orderInfo.createTime }}</el-descriptions-item>
            <el-descriptions-item label="医嘱内容" :span="2">
              <ReText :line-clamp="2" :tippy-props="{ content: orderInfo.content }">
                {{ orderInfo.content }}
              </ReText>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-form-item>
    </ReForm>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import { ReForm, ReText } from "../index";

const formData = reactive({
  approveResult: "approve",
  approveComment: "",
  approveLevel: "primary",
  urgencyLevel: 3,
  autoNext: true,
  notifyPatient: false,
  recordLog: true
});

// 模拟医嘱信息
const orderInfo = ref({
  patientName: "张三",
  orderType: "用药医嘱",
  doctorName: "李医生",
  createTime: "2024-01-15 10:30:00",
  content: "阿司匹林肠溶片 100mg 口服 每日一次 连续7天"
});
</script>

<style scoped>
.approve-dialog {
  padding: 20px;
}

.order-info {
  border: 1px solid var(--el-border-color-light);
  border-radius: 4px;
  padding: 15px;
  background: var(--el-bg-color-page);
}

.order-info .el-descriptions {
  margin: 0;
}
</style> 