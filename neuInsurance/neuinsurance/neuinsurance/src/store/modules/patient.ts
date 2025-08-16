import { defineStore } from "pinia";
import { getPatientList, getPatientDetail } from "@/api/patient";
import type { Patient, PatientQueryParams } from "@/api/patient";

export const usePatientStore = defineStore("patient", {
  state: () => ({
    // 患者列表
    patientList: [] as Patient[],
    // 当前患者详情
    currentPatient: null as Patient | null,
    // 加载状态
    loading: false,
    // 分页信息
    pagination: {
      current: 1,
      size: 10,
      total: 0
    }
  }),

  getters: {
    // 获取患者列表
    getPatientList: (state) => state.patientList,
    // 获取当前患者
    getCurrentPatient: (state) => state.currentPatient,
    // 获取加载状态
    getLoading: (state) => state.loading,
    // 获取分页信息
    getPagination: (state) => state.pagination
  },

  actions: {
    // 获取患者列表
    async fetchPatientList(params: PatientQueryParams) {
      this.loading = true;
      try {
        const result = await getPatientList(params);
        this.patientList = result.records;
        this.pagination = {
          current: result.current,
          size: result.size,
          total: result.total
        };
        return result;
      } catch (error) {
        console.error("获取患者列表失败:", error);
        throw error;
      } finally {
        this.loading = false;
      }
    },

    // 获取患者详情
    async fetchPatientDetail(id: number) {
      try {
        const result = await getPatientDetail(id);
        this.currentPatient = result;
        return result;
      } catch (error) {
        console.error("获取患者详情失败:", error);
        throw error;
      }
    },

    // 清空当前患者
    clearCurrentPatient() {
      this.currentPatient = null;
    },

    // 更新患者列表
    updatePatientList(patientList: Patient[]) {
      this.patientList = patientList;
    },

    // 添加患者到列表
    addPatient(patient: Patient) {
      this.patientList.unshift(patient);
      this.pagination.total += 1;
    },

    // 更新患者信息
    updatePatient(patient: Patient) {
      const index = this.patientList.findIndex(item => item.id === patient.id);
      if (index !== -1) {
        this.patientList[index] = patient;
      }
      if (this.currentPatient?.id === patient.id) {
        this.currentPatient = patient;
      }
    },

    // 删除患者
    removePatient(id: number) {
      const index = this.patientList.findIndex(item => item.id === id);
      if (index !== -1) {
        this.patientList.splice(index, 1);
        this.pagination.total -= 1;
      }
      if (this.currentPatient?.id === id) {
        this.currentPatient = null;
      }
    }
  }
}); 