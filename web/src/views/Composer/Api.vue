<template>
  <modal :show.sync="state" @close="close">
    <template slot="header">
      <h5 class="modal-title" id="apiModel">Add New API</h5>
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
          style="padding-top:20px"
          v-model="model.tags"
          label="name"
          value="name"
          :options="tags"
          multiple
          :reduce="tag => tag.name"
        ></v-select>

        <textarea
          class="form-control form-control-alternative"
          rows="3"
          v-if="model.method === 'post' || model.method === 'put'"
          v-model="model.requestBodyString"
          placeholder="Request Body Info"
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
      return (
        !this.model.path.startsWith("/") &&
        this.model.path.length > 0 &&
        this.model.description.length > 5 &&
        this.model.operationId.length > 0
      );
    }
  },
  methods: {
    close() {
      this.state = false;
      this.$emit("on-close");
    },
    addApi() {
      this.$emit("on-add", this.model);
      if (this.model.requestBodyString.length > 0) {
        try {
          this.model.requestBody = JSON.parse(this.model.requestBodyString);
        } catch (e) {
          this.$log.console.error(e);
        }
      }
      this.$emit("on-close");
    }
  },
  components: { vSelect },
  data() {
    return {
      model: {
        requestBody: {},
        path: "",
        requestBodyString: {},
        description: "",
        tags: ["default"],
        method: "get",
        operationId: "",
        params: []
      },
      state: false,
      options: ["get", "put", "post", "delete"]
    };
  },
  mounted() {
    this.state = this.show;
    if (this.edit) {
      this.model = this.currentApi;
      this.model.requestBodyString = JSON.stringify(this.model.requestBody);
    }
  }
};
</script>
