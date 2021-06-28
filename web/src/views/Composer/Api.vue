<template>
  <modal :show.sync="state" @close="close">
    <template slot="header">
      <h5 class="modal-title" id="apiModel">
        Add New API
      </h5>
    </template>
    <template>
      <form role="form">
        <base-input
          alternative
          class="mb-3"
          :valid="model.path.length > 0"
          v-model="model.path"
          placeholder="Path"
        >
        </base-input>

        <base-input
          alternative
          class="mb-3"
          :valid="model.operationId.length > 0"
          v-model="model.operationId"
          placeholder="Operation Id"
        >
        </base-input>
        <base-input
          alternative
          type="text"
          :valid="model.description.length > 5"
          v-model="model.description"
          placeholder="Description"
        ></base-input>
        <v-select
          placeholder="Select Method"
          v-model="model.method"
          :options="options"
        ></v-select>
        <v-select
          placeholder="Select Tag"
          style="padding-top:20px;"
          v-model="model.tags"
          label="name"
          value="name"
          :options="tags"
          multiple
          :reduce="tag => tag.name"
        ></v-select>

        <label style="padding-top:20px;" class="typo__label form-control-label"
          >Consumes</label
        >
        <multiselect
          style="padding-bottom:20px;"
          v-model="model.consumes"
          tag-placeholder="Add this user to project"
          placeholder="Select content type"
          :options="contentTypes"
          :multiple="true"
          :taggable="true"
        ></multiselect>

        <textarea
          class="form-control form-control-alternative"
          rows="3"
          v-if="model.method === 'post' || model.method === 'put'"
          v-model="model.requestBodyString"
          placeholder="Request Body Info"
        ></textarea>

        <label style="padding-top:20px;" class="typo__label form-control-label"
          >Produces</label
        >
        <multiselect
          style="padding-bottom:20px;"
          v-model="model.produces"
          tag-placeholder="Select"
          placeholder="Select content type"
          :options="contentTypes"
          :multiple="true"
          :taggable="true"
        ></multiselect>

        <textarea
          class="form-control form-control-alternative"
          rows="3"
          v-model="model.responsesString"
          placeholder="Response Body Info"
        ></textarea>
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
      if (
        this.model.requestBodyString &&
        this.model.requestBodyString.length > 0
      ) {
        try {
          this.model.requestBody = JSON.parse(this.model.requestBodyString);
        } catch (e) {
          this.$log.error(e);
        }
      }

      if (this.model.responsesString && this.model.responsesString.length > 0) {
        try {
          this.model.responses = JSON.parse(this.model.responsesString);
        } catch (e) {
          this.$log.error(e);
        }
      } else {
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
