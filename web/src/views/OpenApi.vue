<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
      <!-- Card stats -->
    </base-header>
    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-12 order-xl-1">
          <card shadow type="secondary">
            <div slot="header" class="bg-white border-0">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">OpenApi Docs</h3>
                </div>
              </div>
            </div>
            <base-alert v-show="error" dismissible type="danger">{{
              error_message
            }}</base-alert>

            <template>
              <div id="swagger-ui"></div>
            </template>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import "../swagger/css/swagger-ui.css";
import LoginService from "../services/login-api";
import { SwaggerUIBundle } from "swagger-ui-dist";
export default {
  name: "docs",
  data() {
    return {
      error: null,
      error_message: null,
      jsonApi: null,
      headers: null,
      queryParams: {}
    };
  },
  components: {},
  methods: {
    loadDocument(id) {
      if (id) {
        LoginService.getDocument(id)
          .then(response => {
            this.jsonApi = response.data;

            const ui = SwaggerUIBundle({
              spec: response.data,
              dom_id: "#swagger-ui",
              layout: "BaseLayout"
            });
            window.ui = ui;
          })
          .catch(error => {
            this.error = true;
            this.error_message = error;
          });
      }
    }
  },
  mounted() {
    this.$log.info("Loading document for " + this.$route.params.projectId);
    this.loadDocument(this.$route.params.projectId);
  }
};
</script>
<style>
.download-contents {
  width: unset !important;
}
.scheme-container {
  display: none !important;
}
.information-container {
  display: none !important;
}
code {
  background-color: unset !important;
  color: unset !important;
}
</style>

<style></style>
