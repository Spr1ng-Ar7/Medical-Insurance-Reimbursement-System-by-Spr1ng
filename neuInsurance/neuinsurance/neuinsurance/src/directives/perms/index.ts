import { hasPerms } from "@/utils/auth";
import type { Directive, DirectiveBinding } from "vue";

export const perms: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding<string | Array<string>>) {
    const { value } = binding;
    if (value) {
      if (!hasPerms(value)) {
        // Instead of directly removing the element which will break Vue's
        // internal patching algorithm, simply hide it. This keeps the
        // DOM structure intact and avoids runtime errors during updates.
        el.style.display = "none";
      }
    } else {
      throw new Error(
        "[Directive: perms]: need perms! Like v-perms=\"['btn.add','btn.edit']\""
      );
    }
  }
};
