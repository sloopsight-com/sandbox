/*!

=========================================================
* Vue Argon Dashboard - v1.0.0
=========================================================

* Product Page: https://www.creative-tim.com/product/argon-dashboard
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/argon-dashboard/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import "./registerServiceWorker";
import ArgonDashboard from "./plugins/argon-dashboard";
import store from "./store";
import VueSweetalert2 from "vue-sweetalert2";
import { abilitiesPlugin } from "@casl/vue";
import ability from "./ability";

// If you don't need the styles, do not connect
import "sweetalert2/dist/sweetalert2.min.css";
Vue.use(abilitiesPlugin, ability.current);

Vue.use(VueSweetalert2);
Vue.config.productionTip = false;

Vue.use(ArgonDashboard);

document.addEventListener("DOMContentLoaded", async function() {
  new Vue({
    router,
    store,
    created: function() {
      // `this` points to the vm instance
      if (this.$store.getters.getLoggedInUserRoles) {
        ability.update(this.$store.getters.getLoggedInUserRoles, this.$ability);
      }
    },
    render: h => h(App)
  }).$mount("#app");
});
