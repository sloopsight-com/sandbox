
import BaseInput from "../components/BaseInput";
import BaseCheckbox from "../components/BaseCheckbox";
import BaseButton from "../components/BaseButton";
import BaseAlert from "../components/BaseAlert";
import BaseNav from "../components/BaseNav";
import BaseTable from "../components/BaseTable";
import BaseSlider from "../components/BaseSlider";
import BaseHeader from "../components/BaseHeader";
import BaseDropdown from "../components/BaseDropdown";
import Card from "../components/Card";
import StatsCard from "../components/StatsCard";
import BasePagination from "../components/BasePagination";
import VueMaterial from 'vue-material'
import VueResource from 'vue-resource'
import "vue-material/dist/vue-material.css";

export default {
  install(Vue) {
    Vue.component(BaseInput.name, BaseInput);
    Vue.component(BaseCheckbox.name, BaseCheckbox);
    Vue.component(BaseButton.name, BaseButton);
    Vue.component(BaseAlert.name, BaseAlert);
    Vue.component(BaseNav.name, BaseNav);
    Vue.component(BaseTable.name, BaseTable);
    Vue.component(BaseHeader.name, BaseHeader);
    Vue.component(BaseSlider.name, BaseSlider);
    Vue.component(BaseDropdown.name, BaseDropdown);
    Vue.component(Card.name, Card);
    Vue.component(StatsCard.name, StatsCard);
    Vue.component(BasePagination.name, BasePagination);

    Vue.use(VueMaterial)
    Vue.use(VueResource)

  }
};
