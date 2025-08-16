// 格式化日期
export const formatDate = (date: string | Date, format = "YYYY-MM-DD HH:mm:ss") => {
  if (!date) return "";
  
  const d = new Date(date);
  if (isNaN(d.getTime())) return "";
  
  const year = d.getFullYear();
  const month = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  const hours = String(d.getHours()).padStart(2, "0");
  const minutes = String(d.getMinutes()).padStart(2, "0");
  const seconds = String(d.getSeconds()).padStart(2, "0");
  
  return format
    .replace("YYYY", String(year))
    .replace("MM", month)
    .replace("DD", day)
    .replace("HH", hours)
    .replace("mm", minutes)
    .replace("ss", seconds);
};

// 格式化金额
export const formatMoney = (amount: number, decimals = 2) => {
  if (amount === null || amount === undefined) return "0.00";
  return Number(amount).toFixed(decimals);
};

// 格式化文件大小
export const formatFileSize = (bytes: number) => {
  if (bytes === 0) return "0 B";
  
  const k = 1024;
  const sizes = ["B", "KB", "MB", "GB", "TB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

// 格式化身份证号（脱敏）
export const formatIdCard = (idCard: string) => {
  if (!idCard) return "";
  if (idCard.length < 8) return idCard;
  return idCard.substring(0, 4) + "********" + idCard.substring(idCard.length - 4);
};

// 格式化手机号（脱敏）
export const formatPhone = (phone: string) => {
  if (!phone) return "";
  if (phone.length < 7) return phone;
  return phone.substring(0, 3) + "****" + phone.substring(phone.length - 4);
};

// 格式化姓名（脱敏）
export const formatName = (name: string) => {
  if (!name) return "";
  if (name.length <= 1) return name;
  if (name.length === 2) return name.substring(0, 1) + "*";
  return name.substring(0, 1) + "*" + name.substring(name.length - 1);
};

// 获取文件扩展名
export const getFileExtension = (filename: string) => {
  if (!filename) return "";
  const parts = filename.split(".");
  return parts.length > 1 ? parts[parts.length - 1].toLowerCase() : "";
};

// 验证文件类型
export const validateFileType = (file: File, allowedTypes: string[]) => {
  const extension = getFileExtension(file.name);
  return allowedTypes.includes(extension);
};

// 验证文件大小
export const validateFileSize = (file: File, maxSize: number) => {
  return file.size <= maxSize;
};

// 下载文件
export const downloadFile = (blob: Blob, filename: string) => {
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
}; 