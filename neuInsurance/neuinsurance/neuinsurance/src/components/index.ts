// Pure-admin 封装的组件统一导出
export { ReDialog, addDialog, closeDialog, updateDialog, closeAllDialog } from "./ReDialog";
export { ReText } from "./ReText";
export { ReSegmented } from "./ReSegmented";
export { IconifyIconOffline, IconifyIconOnline, FontIcon } from "./ReIcon";
export { default as ReCol } from "./ReCol";
export { Auth } from "./ReAuth";
export { Perms } from "./RePerms";
export { PureTableBar } from "./RePureTableBar";

// 新增的封装组件
export { ReButton } from "./ReButton";
export { ReTag } from "./ReTag";
export { ReForm } from "./ReForm";

// 通用组件
export { default as BaseTable } from "./Common/BaseTable.vue";
export { default as BaseForm } from "./Common/BaseForm.vue";
export { default as ApproveDialog } from "./Common/ApproveDialog.vue";
export { default as ImportDialog } from "./Common/ImportDialog.vue";
export { default as ExportDialog } from "./Common/ExportDialog.vue";
export { default as ExampleDialog } from "./Common/ExampleDialog.vue";

// 类型导出
export type { DialogOptions, ButtonProps, DialogProps } from "./ReDialog/type";
export type { OptionsType } from "./ReSegmented/src/type";
export type { ButtonProps as ReButtonProps } from "./ReButton/src/type";
export type { TagProps as ReTagProps } from "./ReTag/src/type";
export type { FormProps as ReFormProps } from "./ReForm/src/type"; 