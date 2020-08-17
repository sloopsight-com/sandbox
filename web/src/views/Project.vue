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
                  <h3 class="mb-0">Create project by providing OpenApi Specification</h3>
                </div>
              </div>
            </div>

            <template>
              <base-alert v-show="error" dismissible type="danger">{{error_message}}</base-alert>

              <form @submit.prevent>
                <h6 class="heading-small text-muted mb-4">Project information</h6>
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
                      <label class="typo__label form-control-label">Select Project Members</label>
                      <multiselect
                        v-model="model.members"
                        tag-placeholder="Add this user to project"
                        placeholder="Select Members"
                        label="name"
                        track-by="id"
                        :options="options"
                        :multiple="true"
                        :taggable="true"
                        @tag="addTag"
                      ></multiselect>
                    </div>
                  </div>
                </div>

                <div class="pl-lg-4">
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
                <h6 class="heading-small text-muted mb-4">Open API Specification</h6>
                <div class="pl-lg-4">
                  <div class="form-group">
                    <base-input alternative label="Open Api Json">
                      <codemirror v-model="model.openApiSpec" :options="cmOptions"></codemirror>
                    </base-input>
                  </div>
                </div>
                <div class="row">
                  <div class="col text-right">
                    <base-button type="primary" size="lg" v-on:click="saveProject()">Submit</base-button>
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
import { codemirror } from "vue-codemirror";
// require styles
import "codemirror/lib/codemirror.css";
import "vue-multiselect/dist/vue-multiselect.min.css";

import "codemirror/theme/base16-dark.css";
import "codemirror/mode/javascript/javascript.js";
import Multiselect from "vue-multiselect";
import ProjectService from "../services/project-service";

export default {
  name: "user-profile",
  data() {
    return {
      options: [],
      error: null,
      error_message: null,
      model: {
        name: "",
        openApiSpec: "",
        members: [],
      },
      cmOptions: {
        // codemirror options
        tabSize: 4,
        mode: "text/x-yaml",
        theme: "base16-dark",
        lineNumbers: true,
        line: true,
        // more codemirror options, 更多 codemirror 的高级配置...
      },
    };
  },
  components: {
    codemirror,
    Multiselect,
  },
  methods: {
    checkForm() {
      if (this.model.name && this.model.openApiSpec) {
        return true;
      }
      if (!this.model.name) {
        this.error = true;
        this.error_message = "Name is missing";
        return false;
      }

      if (!this.model.openApiSpec) {
        this.error = true;
        this.error_message = "Spec are missing";
        return false;
      }
    },
    init() {},

    saveProject() {
      if (this.checkForm()) {
        var projectPromise = null;
        if (this.$route.params.id) {
          projectPromise = ProjectService.update(
            this.$route.params.id,
            this.model.name,
            this.model.openApiSpec,
            this.model.description,
            this.model.members
          );
        } else {
          projectPromise = ProjectService.save(
            this.model.name,
            this.model.openApiSpec,
            this.model.description,
            this.model.members
          );
        }
        projectPromise
          .then(() => {
            this.$router.push("/projects");
          })
          .catch((error) => {
            this.error = true;
            if (error.response) {
              this.error_message = "Please check your OpenApi Json";
            } else {
              this.error_message = error;
            }
          });
      }
    },
  },
  mounted() {
    this.$log.info(this.$route.params.id);
    var id = 0;
    if (this.$route.params.id) {
      id = this.$route.params.id;
      ProjectService.getOne(this.$route.params.id)
        .then((response) => {
          this.model.name = response.data.name;
          this.model.openApiSpec = response.data.openApiSpec;
          this.model.description = response.data.description;
        })
        .catch((error) => {
          this.error = true;
          this.error_message = error;
        });

      ProjectService.getExistingMember(id)
        .then((response) => {
          this.model.members = response.data;
        })
        .catch((error) => {
          this.error = true;
          this.error_message = error;
        });
    }
    ProjectService.getAvailableMember(id)
      .then((response) => {
        this.options = response.data;
      })
      .catch((error) => {
        this.error = true;
        this.error_message = error;
      });
  },
};
</script>
<style></style>
