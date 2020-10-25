import { defineAbility, AbilityBuilder } from "@casl/ability";
/**
 * Defines how to detect object's type: https://stalniy.github.io/casl/abilities/2017/07/20/define-abilities.html
 */
function subjectName(item) {
  if (!item || typeof item === "string") {
    return item;
  }

  return item.__type;
}

export default {
  current: defineAbility({ subjectName }, can => {
    can("read", "Article");
  }),
  update(roles, ability) {
    const { can, rules } = new AbilityBuilder();
    if (roles) {
      if (roles.includes("ROLE_ADMIN")) {
        can("manage", "User", { active: true });
        can("manage", "Setting", { active: true });
      }
    }
    ability.update(rules);
  }
};
