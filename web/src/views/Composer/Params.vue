<template>
  <modal :show.sync="state" @close="close">
    <template slot="header">
      <h5 class="modal-title" id="apiModel">Params</h5>
    </template>
    <template>
      <base-table
        class="table align-items-center table-flush"
        :class="type === 'dark' ? 'table-dark' : ''"
        :thead-classes="type === 'dark' ? 'thead-dark' : 'thead-light'"
        tbody-classes="list"
        :data="tableData"
      >
        <template slot="columns">
          <th>Name</th>
          <th>Action</th>
        </template>

        <template slot-scope="{ row }">
          <td class="budget">{{ row.name }}</td>
          <td class="text-left">
            <base-button type="primary" v-on:click="deleteParam(row)" size="sm">
              Delete
            </base-button>
            <base-button type="primary" v-on:click="editParam(row)" size="sm">
              Edit
            </base-button>
          </td>
        </template>
      </base-table>
    </template>
    <template slot="footer">
      <form role="form" style="width:100%" v-if="showEdit">
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

        <div class="row float-right">
          <div class="col-lg-12" style="padding-top:10px;">
            <base-button
              style="padding-top:10px"
              @click="onSubmit"
              :disabled="!validForm"
              type="primary"
              >Save changes</base-button
            >
          </div>
        </div>
      </form>
    </template>
  </modal>
</template>
<script>
import vSelect from "vue-select";
import "vue-select/dist/vue-select.css";
export default {
  props: {
    tableData: {
      type: Array
    },
    type: String,
    show: Boolean,

    title: []
  },
  computed: {
    validForm() {
      return this.model.name.length > 0 && this.model.description.length > 5;
    }
  },
  components: { vSelect },

  data() {
    return {
      state: false,
      label: "Param(s)",
      showEdit: false,
      model: {
        name: "",
        description: "",
        required: false,
        in: "query",
        style: "form",
        schema: { type: "string" },
        schemaString: ""
      },
      types: ["query", "path"]
    };
  },
  methods: {
    onSubmit() {
      this.$emit("on-close");
      this.state = false;
    },
    close() {
      this.$emit("on-close");
      this.state = false;
    },
    editParam(item) {
      this.model = item;
      this.showEdit = true;
    },
    deleteParam(item) {
      let indexToDelete = this.tableData.findIndex(
        tableRow => tableRow.name === item.name
      );
      if (indexToDelete >= 0) {
        this.tableData.splice(indexToDelete, 1);
      }
    }
  },
  mounted() {
    this.state = this.show;
  }
};
</script>
