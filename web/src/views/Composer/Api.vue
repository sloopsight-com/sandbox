<template>
  <modal modalClasses="fullscreen" :show.sync="state" @close="close">
    <template slot="header">
      <h5 class="modal-title" id="apiModel">
        Add New API
      </h5>
    </template>
    <template>
      <form role="form">
        <div class="container ct-example-row">
          <div class="row">
            <div class="col">
              <base-input
                alternative
                class="mb-3"
                :valid="model.path.length > 0"
                v-model="model.path"
                placeholder="Path"
              >
              </base-input>
            </div>
            <div class="col">
              <base-input
                alternative
                class="mb-3"
                :valid="model.operationId.length > 0"
                v-model="model.operationId"
                placeholder="Operation Id"
              >
              </base-input>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <base-input
                alternative
                type="text"
                :valid="model.description.length > 5"
                v-model="model.description"
                placeholder="Description"
              ></base-input>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <v-select
                placeholder="Select Tag"
                v-model="model.tags"
                label="name"
                value="name"
                :options="tags"
                multiple
                :reduce="tag => tag.name"
              ></v-select>
            </div>
            <div class="col">
              <v-select
                placeholder="Select Method"
                v-model="model.method"
                :options="options"
              ></v-select>
            </div>
          </div>

          <div class="row">
            <div class="col">
              <tabs fill class="flex-column flex-md-row">
                <card shadow>
                  <tab-pane class="tab">
                    <span slot="title">
                      Produces
                    </span>
                    <p class="description">
                      <multiselect
                        style="padding-bottom:20px;"
                        v-model="model.produces"
                        tag-placeholder="Select"
                        placeholder="Select content type"
                        :options="contentTypes"
                        :multiple="true"
                        :taggable="true"
                      ></multiselect>
                      <v-jsoneditor
                        :options="joptions"
                        v-model="model.responses"
                        :plus="true"
                      />
                    </p>
                  </tab-pane>

                  <tab-pane title="Profile">
                    <span slot="title">
                      Consumes
                    </span>
                    <p class="description">
                      <multiselect
                        style="padding-bottom:20px;"
                        v-model="model.consumes"
                        tag-placeholder="Add this user to project"
                        placeholder="Select content type"
                        :options="contentTypes"
                        :multiple="true"
                        :taggable="true"
                      ></multiselect>

                      <v-jsoneditor
                        :options="joptions"
                        v-model="model.requestBody"
                        :plus="true"
                      />
                    </p>
                  </tab-pane>
                </card>
              </tabs>
            </div>
          </div>
        </div>
      </form>
    </template>
    <template slot="footer">
      <base-button @click="addApi" :disabled="!validForm" type="primary"
        >Save changes</base-button
      >
    </template>
  </modal>
</template>
<script>
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
import Multiselect from "vue-multiselect";
import Vue from "vue";
import VJsoneditor from "v-jsoneditor";

Vue.use(VJsoneditor);
export default {
  props: {
    show: Boolean,
    edit: { type: Boolean, default: false },
    currentApi: {
      type: Object,
      default: () => {
        {
          return {
            requestBody: {},
            path: "",
            consumes: [],
            requestBodyString: {},
            description: "",
            tags: ["default"],
            method: "get",
            operationId: "",
            params: []
          };
        }
      }
    },
    tags: {
      type: Array
    }
  },
  computed: {
    validForm() {
      const idem = false;
      const hasData =
        this.model.requestBodyString && this.model.requestBodyString.length > 0;

      const valid =
        this.model.path.length > 0 &&
        this.model.path.startsWith("/") &&
        this.model.description.length > 5 &&
        this.model.operationId.length > 0;

      if (idem) {
        return hasData && valid;
      } else {
        return valid;
      }
    }
  },
  methods: {
    close() {
      this.state = false;
      this.$emit("on-close");
    },
    addApi() {
      if (!this.model.requestBody) {
        try {
          this.model.requestBody = {};
        } catch (e) {
          this.$log.error(e);
        }
      }

      if (!this.model.responses) {
        this.model.responses = {
          "200": {
            description: "Request accepted successfully!"
          },
          "400": {
            description: "bad input parameter"
          },
          "500": {
            description: "Internal server error"
          }
        };
      }

      this.$emit("on-add", this.model);
      this.$emit("on-close");
    }
  },
  components: { vSelect, Multiselect },
  data() {
    return {
      model: {
        requestBody: {},
        produces: [],
        consumes: [],
        responses: {},
        path: "",
        requestBodyString: {},
        responsesString: {},
        description: "",
        tags: ["default"],
        method: "get",
        operationId: "",
        params: []
      },
      joptions: { mode: "code", search: false, mainMenuBar: true },
      state: false,
      options: ["get", "put", "post", "delete"],
      contentTypes: [
        "application/json",
        "multipart/form-data",
        "application/x-www-form-urlencoded",
        "application/xml",
        "text/plain"
      ]
    };
  },
  mounted() {
    this.state = this.show;
    if (this.edit) {
      this.model = this.currentApi;
      this.model.requestBodyString = JSON.stringify(this.model.requestBody);
      this.model.responsesString = JSON.stringify(this.model.responses);
    }
  }
};
</script>
<style lang="scss">
.nav-pills > li:nth-child(1) {
  position: relative;
  top: 8px;
}

.fullscreen {
  max-width: 70%;
}
</style>
