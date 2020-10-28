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
                  <h3 class="mb-0">
                    Create project by providing OpenApi Specification
                  </h3>
                </div>
              </div>
            </div>

            <template>
              <base-alert v-show="error" dismissible type="danger">{{
                error_message
              }}</base-alert>

              <form @submit.prevent>
                <h6 class="heading-small text-muted mb-4">
                  Project information
                </h6>
                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <base-input
                        alternative
                        required
                        label="Project Name"
                        placeholder="Name of the project"
                        input-classes="form-control-alternative"
                        v-model="model.name"
                      />
                    </div>
                  </div>
                </div>
                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <label class="typo__label form-control-label"
                        >Select Project Members</label
                      >
                      <multiselect
                        v-model="model.members"
                        tag-placeholder="Add this user to project"
                        placeholder="Select Members"
                        label="name"
                        track-by="id"
                        :options="options"
                        :multiple="true"
                        :taggable="true"
                      ></multiselect>
                    </div>
                  </div>
                </div>

                <div class="pl-lg-4" style="padding-top:25px">
                  <div class="row">
                    <div class="col-lg-6">
                      <base-input
                        alternative
                        required
                        label="Project Description"
                        placeholder="Describe this project"
                        input-classes="form-control-alternative"
                        v-model="model.description"
                      />
                    </div>
                  </div>
                </div>
                <!-- Description -->

                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-4">
                      <label class="typo__label form-control-label">Tags</label>
                      <multiselect
                        v-model="selectedTags"
                        tag-placeholder="Select available tags"
                        placeholder="Select available tags"
                        :options="tags"
                        label="name"
                        track-by="name"
                        :multiple="true"
                        :taggable="true"
                      ></multiselect>
                    </div>
                    <div class="col-lg-2" style="padding-top:30px">
                      <base-button
                        type="primary"
                        @click="addTag"
                        icon="ni ni-fat-add"
                      ></base-button>
                    </div>
                  </div>
                </div>

                <div class="pl-lg-4" style="padding-top:25px">
                  <div class="row">
                    <div class="col-lg-6">
                      <apis
                        @onCreateApi="addNewApi"
                        @onCreateParam="createNewParam"
                        :tableData.sync="apisData"
                        @show-params="viewParams"
                        @on-edit="openApiForEdit"
                      ></apis>
                    </div>
                  </div>
                </div>

                <api
                  :tags.sync="selectedTags"
                  v-if="addApi"
                  :show.sync="addApi"
                  :currentApi="currentApi"
                  :edit="editApiPopUp"
                  @on-close="addApi = false"
                  @on-edit="editApiParam"
                  @on-add="updateExistingApi"
                ></api>
                <api-param
                  v-if="addParam"
                  :show.sync="addParam"
                  :api.sync="currentApi"
                  @on-close="addParam = false"
                  @onSubmit="addParam = false"
                >
                </api-param>
                <params
                  v-if="showParams"
                  :show.sync="showParams"
                  @on-close="showParams = false"
                  :tableData.sync="currentApi.params"
                ></params>
                <div class="row">
                  <div class="col text-right">
                    <base-button
                      type="primary"
                      size="lg"
                      v-on:click="saveProject()"
                      >Submit</base-button
                    >
                  </div>
                </div>
              </form>
            </template>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import "vue-multiselect/dist/vue-multiselect.min.css";
import Multiselect from "vue-multiselect";
import ProjectService from "../services/project-service";
import Apis from "./Composer/Apis";
import Params from "./Composer/Params";

import Api from "./Composer/Api";
import ApiParam from "./Composer/Param";
import SwaggerTemplate from "./swagger-template.json";
export default {
  name: "user-profile",
  data() {
    return {
      editApiPopUp: false,
      apiSpec: { paths: {} },
      showParams: false,
      currentApi: {},
      apisData: [],
      addParam: false,
      addApi: false,
      options: ["default"],
      tags: [],
      error: null,
      error_message: null,
      selectedTags: ["default"],
      defaultResponses: {
        "200": {
          description: "Request accepted successfully!"
        },
        "400": {
          description: "bad input parameter"
        },
        "500": {
          description: "Internal server error"
        }
      },
      model: {
        name: "",
        openApiSpec: "",
        members: []
      }
    };
  },
  components: {
    Multiselect,
    Apis,
    Api,
    ApiParam,
    Params
  },
  methods: {
    createNewParam(item) {
      this.currentApi = item;
      this.addParam = true;
    },
    editApiParam(item) {
      this.currentApi = item;
    },
    addNewApi() {
      this.addApi = true;
      this.editApiPopUp = false;
    },
    openApiForEdit(item) {
      this.currentApi = item;
      this.addApi = true;
      this.editApiPopUp = true;
    },
    checkForm() {
      if (this.model.name && this.model.openApiSpec) {
        return true;
      }
      if (!this.model.name) {
        this.error = true;
        this.error_message = "Name is missing";
        return false;
      }
      return true;
    },
    init() {},

    viewParams(api) {
      this.currentApi = api;
      this.showParams = true;
    },
    updateExistingApi(item) {
      let indexToDelete = this.apisData.findIndex(
        tableRow =>
          tableRow.path === item.path && tableRow.method === item.method
      );
      if (indexToDelete >= 0) {
        this.apisData.splice(indexToDelete, 1);
      }
      this.apisData.push(item);
    },
    prepareSpec() {
      this.apiSpec.info.title = this.model.name;
      this.apiSpec.info.description = this.model.description;
      this.apiSpec.tags = this.selectedTags;
      this.apiSpec.paths = {};
      for (let i = 0; i < this.apisData.length; i++) {
        let apiData = this.apisData[i];

        this.apiSpec.paths[apiData.path] = {};

        if (apiData.method == "post" || apiData.method == "put") {
          this.apiSpec.paths[apiData.path][apiData.method] = {
            tags: apiData.tags,
            summary: apiData.description,
            description: apiData.description,
            operationId: apiData.operationId,
            parameters: apiData.params,
            requestBody: apiData.requestBody,
            responses: this.defaultResponses
          };
        } else {
          this.apiSpec.paths[apiData.path][apiData.method] = {
            tags: apiData.tags,
            summary: apiData.description,
            description: apiData.description,
            operationId: apiData.operationId,
            parameters: apiData.params,
            responses: this.defaultResponses
          };
        }
      }
    },
    addTag() {
      this.$swal
        .fire({
          title: "Input tag name",
          input: "text",
          inputLabel: "Tag",
          inputPlaceholder: "Enter your api tag",
          showCancelButton: true,
          inputValidator: value => {
            if (!value) {
              return "You need to write something!";
            }
          }
        })
        .then(tag => {
          this.tags.push({ name: tag.value, description: "" });
        });
    },
    saveProject() {
      if (this.checkForm()) {
        this.prepareSpec();

        var projectPromise = null;
        const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });
        if (this.$route.params.id) {
          projectPromise = ProjectService.update(
            this.$route.params.id,
            this.model.name,
            JSON.stringify(this.apiSpec),
            this.model.description,
            this.model.members
          );
        } else {
          projectPromise = ProjectService.save(
            this.model.name,
            JSON.stringify(this.apiSpec),
            this.model.description,
            this.model.members
          );
        }

        projectPromise
          .then(() => {
            this.$notify({
              title: "Project save successfully",
              delay: 3000,
              type: "success",
              icon: "fa fa-info"
            });
            loader.hide();
            this.$router.push("/projects");
          })
          .catch(error => {
            loader.hide();
            this.error = true;
            if (error.response) {
              this.error_message = "Please check your OpenApi Json";
            } else {
              this.error_message = error;
            }
          });
      }
    }
  },
  mounted() {
    this.$log.info(this.$route.params.id);
    var id = 0;
    if (this.$route.params.id) {
      id = this.$route.params.id;
      ProjectService.getOne(this.$route.params.id)
        .then(response => {
          this.model.name = response.data.name;
          this.model.openApiSpec = response.data.openApiSpec;
          this.model.description = response.data.description;
          let apiSpec = JSON.parse(this.model.openApiSpec);
          this.apiSpec = apiSpec;
          let data = this.apisData;
          this.tags = apiSpec.tags;
          this.selectedTags = apiSpec.tags;
          //  const log = this.$log;
          Object.keys(apiSpec.paths).forEach(function(path) {
            let pathObject = apiSpec.paths[path];
            Object.keys(pathObject).forEach(function(m) {
              let method = pathObject[m];

              let params = [];
              if (method.parameters) {
                for (let i = 0; i < method.parameters.length; i++) {
                  let parameter = method.parameters[i];
                  params.push(parameter);
                }
              }

              data.push({
                path: path,
                method: m,
                tags: method.tags,
                description: method.description,
                operationId: method.operationId,
                requestBodyString: JSON.stringify(method.requestBody),
                requestBody: method.requestBody,
                params: params
              });
            });
          });
        })
        .catch(error => {
          this.error = true;
          this.error_message = error;
          this.$log.error(error);
        });

      ProjectService.getExistingMember(id)
        .then(response => {
          this.model.members = response.data;
        })
        .catch(error => {
          this.error = true;
          this.error_message = error;
        });
    } else {
      this.apiSpec = SwaggerTemplate;
      this.tags = this.apiSpec.tags;
      this.selectedTags = this.apiSpec.tags;
    }
    ProjectService.getAvailableMember(id)
      .then(response => {
        this.options = response.data;
      })
      .catch(error => {
        this.error = true;
        this.error_message = error;
      });
  }
};
</script>
<style></style>
