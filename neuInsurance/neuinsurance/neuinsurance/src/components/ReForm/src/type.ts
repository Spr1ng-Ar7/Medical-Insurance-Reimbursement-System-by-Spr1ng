import type { FormProps as ElFormProps } from "element-plus";

export interface FormProps extends Partial<ElFormProps> {
  /** 自定义样式类 */
  customClass?: string;
  /** 是否显示边框 */
  showBorder?: boolean;
  /** 表单标题 */
  title?: string;
  /** 是否显示标题 */
  showTitle?: boolean;
} 