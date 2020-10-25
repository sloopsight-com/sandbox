<template>
  <div class="card shadow" :class="type === 'dark' ? 'bg-default': ''" style="width:100%">
    <div class="card-header border-0" :class="type === 'dark' ? 'bg-transparent': ''">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0" :class="type === 'dark' ? 'text-white': ''">{{label}}</h3>
        </div>
      </div>
    </div>

    <div class="table-responsive" style="min-height: 400px;">
      <base-table
        class="table align-items-center table-flush"
        :class="type === 'dark' ? 'table-dark': ''"
        :thead-classes="type === 'dark' ? 'thead-dark': 'thead-light'"
        tbody-classes="list"
        :data="tableData"
      >
        <template slot="columns">
          <th>ID</th>
          <th>Path</th>
          <th>Method</th>
          <th></th>
        </template>

        <template slot-scope="{row}">
          <td class="budget">{{row.id}}</td>
          <td class="budget">{{row.path}}</td>
          <td class="budget">{{row.method}}</td>
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
                <a class="dropdown-item" @click.prevent="editEndpoint(row.id)">Edit</a>
                <a class="dropdown-item" @click.prevent="deleteEndpoint(row.id)">Delete</a>
              </template>
            </base-dropdown>
          </td>
        </template>
      </base-table>
    </div>

    <div
      class="card-footer d-flex justify-content-end"
      :class="type === 'dark' ? 'bg-transparent': ''"
    >
      <base-pagination
        :total="total"
        :value="value"
        :perPage="perPage"
        v-on:input="pageChangeHandle($event)"
      ></base-pagination>
    </div>
  </div>
</template>
<script>
import EndpointService from "../../services/endpoint-service";

export default {
  props: {
    type: {
      type: String,
    },
    title: String,
  },
  data() {
    return {
      total: 2,
      value: 0,
      perPage: 5,
      label: "Enpoints",
      tableData: [],
    };
  },
  methods: {
    async pageChangeHandle(value) {
      const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });
      EndpointService.get(this.$route.params.id, value - 1, this.perPage).then(
        (response) => {
          this.tableData = response.data.content;
          this.total = response.data.totalElements;
          loader.hide();
        }
      );
    },
    init() {
      const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });

      EndpointService.get(this.$route.params.id, 0, this.perPage).then(
        (response) => {
          this.tableData = response.data.content;
          this.total = response.data.totalElements;
          loader.hide();
        }
      );
    },
    deleteEndpoint(id) {
      EndpointService.delete(id).then((response) => {
        this.$log.info(response);
        this.init();
      });
    },
    editEndpoint(id) {
      this.$router.push(
        "/project/" + this.$route.params.id + "/endpoint/" + id
      );
    },
  },
  mounted() {
    this.init();
  },
};
</script>
<style>
</style>
