import type { TagProps as ElTagProps } from "element-plus";

export interface TagProps extends Partial<ElTagProps> {
  /** 标签图标 */
  iconName?: string;
  /** 标签文本 */
  tagText?: string;
  /** 自定义样式类 */
  customClass?: string;
  /** 是否显示图标 */
  showIcon?: boolean;
} 