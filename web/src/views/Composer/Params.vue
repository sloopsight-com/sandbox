<template>
  <modal :show.sync="state" @close="close">
    <template slot="header">
      <h5 class="modal-title" id="apiModel">Add New API</h5>
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
          </td>
        </template>
      </base-table>
    </template>
    <template slot="footer"> </template>
  </modal>
</template>
<script>
export default {
  props: {
    tableData: {
      type: Array
    },
    type: String,
    show: Boolean,

    title: []
  },
  data() {
    return {
      state: false,
      label: "Param(s)"
    };
  },
  methods: {
    close() {
      this.$emit("on-close");
      this.state = false;
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
