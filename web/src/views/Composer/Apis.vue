<template>
  <div
    class="card shadow"
    :class="type === 'dark' ? 'bg-default' : ''"
    style="width:100%"
  >
    <div
      class="card-header border-0"
      :class="type === 'dark' ? 'bg-transparent' : ''"
    >
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0" :class="type === 'dark' ? 'text-white' : ''">
            {{ label }}
          </h3>
        </div>
        <div class="col text-right">
          <base-button type="primary" v-on:click="createApi" size="sm"
            >Create New</base-button
          >
        </div>
      </div>
    </div>

    <div class="table-responsive" style="min-height: 200px;">
      <base-table
        class="table align-items-center table-flush"
        :class="type === 'dark' ? 'table-dark' : ''"
        :thead-classes="type === 'dark' ? 'thead-dark' : 'thead-light'"
        tbody-classes="list"
        :data="tableData"
      >
        <template slot="columns">
          <th>Path</th>
          <th>Method</th>
          <th>Params</th>
          <th></th>
        </template>

        <template slot-scope="{ row }">
          <td class="budget">{{ row.path }}</td>
          <td class="budget">{{ row.method }}</td>

          <td class="budget">
            <base-button
              type="primary"
              v-if="row.params.length > 0"
              v-on:click="showParams(row)"
              size="sm"
            >
              View {{ row.params.length }}
            </base-button>
            <span v-else>No Params</span>
          </td>

          <td class="text-right">
            <base-dropdown class="dropdown" position="right">
              <a
                slot="title"
                class="btn btn-primary btn-sm active"
                role="button"
                data-toggle="dropdown"
                aria-haspopup="true"
                aria-expanded="false"
              >
                Action
                <i class="fas fa-caret-down"></i>
              </a>

              <template>
                <a class="dropdown-item" @click.prevent="deleteApi(row)"
                  >Delete</a
                >
                <a class="dropdown-item" @click.prevent="onCreateParam(row)"
                  >Add Param</a
                >
              </template>
            </base-dropdown>
          </td>
        </template>
      </base-table>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    tableData: {
      type: Array,
      default: new Array()
    },
    type: String,
    title: []
  },
  data() {
    return {
      label: "API(s)"
    };
  },
  methods: {
    createNew() {},
    showParams(api) {
      this.$emit("show-params", api);
    },
    createApi() {
      this.$emit("onCreateApi");
    },
    onCreateParam(item) {
      this.$emit("onCreateParam", item);
    },
    deleteApi(item) {
      let indexToDelete = this.tableData.findIndex(
        tableRow =>
          tableRow.path === item.path && tableRow.method === item.method
      );
      if (indexToDelete >= 0) {
        this.tableData.splice(indexToDelete, 1);
      }
    }
  },
  mounted() {}
};
</script>
<style></style>
