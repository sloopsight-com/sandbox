import "vue-material/dist/vue-material.css";
import "@/assets/vendor/nucleo/css/nucleo.css";
import "@/assets/scss/argon.scss";
import globalComponents from "./globalComponents";
import globalDirectives from "./globalDirectives";
import NotificationPlugin from "@/components/NotificationPlugin/index"
import SidebarPlugin from "@/components/SidebarPlugin/index"
import VueLogger from 'vuejs-logger';
import VueMaterial from 'vue-material'
import VueResource from 'vue-resource'
const isProduction = process.env.NODE_ENV === 'production';


export default {
  install(Vue) {
    Vue.use(globalComponents);
    Vue.use(globalDirectives);
    Vue.use(NotificationPlugin);
    Vue.use(SidebarPlugin);
    Vue.use(VueMaterial)
    Vue.use(VueResource)

    const options = {
      isEnabled: true,
      logLevel: isProduction ? 'error' : 'debug',
      stringifyArguments: false,
      showLogLevel: true,
      showMethodName: true,
      separator: '|',
      showConsoleColors: true
    };

    Vue.use(VueLogger, options);



  }


};
