<template>
  <div>
    <modal :show="state" @close="closePopUp">
      <template slot="header">
        <h5 class="modal-title" id="apiModel">Add New Param</h5>
      </template>
      <template>
        <form role="form">
          <base-input
            alternative
            class="mb-3"
            :valid="model.name.length > 0"
            v-model="model.name"
            placeholder="Name"
          >
          </base-input>
          <base-input
            alternative
            class="mb-3"
            placeholder="Description"
            :valid="model.description.length > 3"
            v-model="model.description"
          >
          </base-input>

          <v-select
            placeholder="Select Type"
            v-model="model.type"
            :options="types"
          ></v-select>

          <base-checkbox
            class="mb-3"
            style="padding-top:20px"
            v-model="model.required"
          >
            Required
          </base-checkbox>
          <textarea
            class="form-control form-control-alternative"
            rows="3"
            v-model="model.schemaString"
            placeholder="Write a schema Json"
          ></textarea>
        </form>
      </template>
      <template slot="footer">
        <base-button @click="onSubmit" :disabled="!validForm" type="primary"
          >Save changes</base-button
        >
      </template>
    </modal>
  </div>
</template>
<script>
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
export default {
  props: {
    show: Boolean,
    api: Object
  },
  components: { vSelect },
  methods: {
    onSubmit() {
      if (this.model.schemaString.length > 0) {
        this.model.schema = JSON.parse(this.model.schemaString);
      }
      this.api.params.push(this.model);
      this.$emit("onSubmit", this.model);
      this.state = false;
    },
    closePopUp() {
      this.$emit("on-close");
      this.state = false;
    }
  },
  computed: {
    validForm() {
      return this.model.name.length > 0 && this.model.description.length > 5;
    }
  },

  mounted() {
    this.state = this.show;
    this.model.requestBodyString = JSON.stringify(this.model.requestBody);
  },
  data() {
    return {
      state: false,
      model: {
        name: "",
        description: "",
        required: false,
        type: "query",
        schemaString: "",
        schema: {}
      },
      types: ["query", "path"]
    };
  }
};
</script>
