import reForm from "./src/index.vue";
import { withInstall } from "@pureadmin/utils";

/** 增强的表单组件，支持更多功能和样式 */
export const ReForm = withInstall(reForm);

export default ReForm;
export type { FormProps } from "./src/type"; 