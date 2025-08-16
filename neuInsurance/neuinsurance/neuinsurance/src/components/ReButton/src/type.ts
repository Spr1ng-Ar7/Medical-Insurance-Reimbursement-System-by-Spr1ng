import type { ButtonProps as ElButtonProps } from "element-plus";

export interface ButtonProps extends Partial<ElButtonProps> {
  /** 按钮图标 */
  iconName?: string;
  /** 按钮文本内容 */
  buttonText?: string;
  /** 是否显示背景 */
  showBg?: boolean;
  /** 自定义样式类 */
  customClass?: string;
} 