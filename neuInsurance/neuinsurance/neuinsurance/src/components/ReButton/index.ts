import reButton from "./src/index.vue";
import { withInstall } from "@pureadmin/utils";

/** 增强的按钮组件，支持更多功能和样式 */
export const ReButton = withInstall(reButton);

export default ReButton;
export type { ButtonProps } from "./src/type"; 