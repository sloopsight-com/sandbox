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
            <base-alert v-show="error" dismissible type="danger">{{error_message}}</base-alert>

            <template>
              <open-api
                v-if="jsonApi"
                :api="jsonApi"
                :query-params="queryParams"
                :headers="headers"
              ></open-api>
            </template>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>
 <script>
import OpenApi from "vue-openapi";
import ProjectService from "../services/project-service";

export default {
  name: "docs",
  data() {
    return {
      error: null,
      error_message: null,
      jsonApi: null,
      headers: null,
      queryParams: {},
    };
  },
  components: {
    OpenApi,
  },
  methods: {
    loadDocument(id) {
      if (id) {
        ProjectService.getDocs(id)
          .then((response) => {
            this.jsonApi = response.data;
          })
          .catch((error) => {
            this.error = true;
            this.error_message = error;
          });
      }
    },
  },
  mounted() {
    this.$log.info("Loading document for " + this.$route.params.projectId);
    this.loadDocument(this.$route.params.projectId);
  },
};
</script>
<style></style>
