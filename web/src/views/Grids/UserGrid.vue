<template>
  <div class="card shadow" :class="type === 'dark' ? 'bg-default': ''" style="width:100%">
    <div class="card-header border-0" :class="type === 'dark' ? 'bg-transparent': ''">
      <div class="row align-items-center">
        <div class="col">
          <h3 class="mb-0" :class="type === 'dark' ? 'text-white': ''">{{label}}</h3>
        </div>
        <div class="col text-right">
          <base-button type="primary" v-on:click="goToCreate()" size="sm">Create New</base-button>
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
          <th>Name</th>
          <th>Description</th>

          <th></th>
        </template>

        <template slot-scope="{row}">
          <td class="budget">{{row.id}}</td>
          <td class="budget">{{row.username}}</td>
          <td class="budget">{{row.email}}</td>

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
                <a class="dropdown-item" @click.prevent="goToEdit(row.id)">Edit</a>
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
import UserService from "../../services/user-service";

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
      label: "Users",
      tableData: [],
    };
  },
  methods: {
    async pageChangeHandle(value) {
      UserService.get(value - 1, this.perPage).then((response) => {
        this.tableData = response.data.content;
        this.total = response.data.totalElements;
      });
    },
    init() {
      UserService.get(0, this.perPage).then((response) => {
        this.tableData = response.data.content;
        this.total = response.data.totalElements;
      });
    },
    goToCreate() {
      this.$log.info("Creating user ");

      this.$router.push("/user");
    },
    goToEdit(id) {
      this.$router.push("/user/" + id);
    },
  },
  mounted() {
    this.init();
  },
};
</script>
<style>
</style>
