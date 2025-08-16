import reTag from "./src/index.vue";
import { withInstall } from "@pureadmin/utils";

/** 增强的标签组件，支持更多功能和样式 */
export const ReTag = withInstall(reTag);

export default ReTag;
export type { TagProps } from "./src/type"; 