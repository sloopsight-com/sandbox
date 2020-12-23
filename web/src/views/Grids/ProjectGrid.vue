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
          <base-button type="primary" v-on:click="goToCreate()" size="sm"
            >Create New</base-button
          >
        </div>
      </div>
    </div>

    <div class="table-responsive" style="min-height: 400px;">
      <base-table
        class="table align-items-center table-flush"
        :class="type === 'dark' ? 'table-dark' : ''"
        :thead-classes="type === 'dark' ? 'thead-dark' : 'thead-light'"
        tbody-classes="list"
        :data="tableData"
      >
        <template slot="columns">
          <th>ID</th>
          <th>Name</th>
          <th>Description</th>

          <th></th>
        </template>

        <template slot-scope="{ row }">
          <td class="budget">{{ row.id }}</td>
          <td class="budget">{{ row.name }}</td>
          <td class="budget">{{ row.description }}</td>

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
                <a class="dropdown-item" @click.prevent="goToEdit(row.id)"
                  >Edit</a
                >
                <a class="dropdown-item" @click.prevent="deleteProject(row.id)"
                  >Delete</a
                >
                <a class="dropdown-item" @click.prevent="loadEndpoints(row.id)"
                  >Endpoint</a
                >
                <a class="dropdown-item" @click.prevent="viewDocument(row.id)"
                  >Swagger Docs</a
                >
              </template>
            </base-dropdown>
          </td>
        </template>
      </base-table>
    </div>

    <div
      class="card-footer d-flex justify-content-end"
      :class="type === 'dark' ? 'bg-transparent' : ''"
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
import ProjectService from "../../services/project-service";

export default {
  props: {
    type: {
      type: String
    },
    title: String
  },
  data() {
    return {
      isLoading: false,
      total: 2,
      value: 0,
      perPage: 5,
      label: "My Projects",
      tableData: []
    };
  },
  methods: {
    async pageChangeHandle(value) {
      const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });
      ProjectService.get(value - 1, this.perPage).then(response => {
        this.tableData = response.data.content;
        this.total = response.data.totalElements;
        loader.hide();
      });
    },
    init() {
      const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });
      ProjectService.get(0, this.perPage).then(response => {
        this.tableData = response.data.content;
        this.total = response.data.totalElements;
        loader.hide();
      });
    },
    goToCreate() {
      this.$log.info("Creating project ");

      this.$router.push("/project");
    },
    goToEdit(id) {
      this.$router.push("/project/" + id);
    },
    loadEndpoints(id) {
      this.$router.push("/project/" + id + "/endpoint");
    },
    viewDocument(id) {
      this.$log.info("Loading document " + id);
      this.$router.push("/docs/" + id);
    },
    deleteProject(id) {
      this.$swal
        .fire({
          title: "Are you sure?",
          text: "You won't be able to revert this!",
          type: "warning",
          showCancelButton: true,
          confirmButtonClass: "md-button md-success btn-fill",
          cancelButtonClass: "md-button md-danger btn-fill",
          confirmButtonText: "Yes, delete it!",
          buttonsStyling: false
        })
        .then(result => {
          if (result.value) {
            const loader = this.$loading.show({
              loader: "bars",
              color: "#2dce89"
            });
            ProjectService.delete(id).then(response => {
              this.$log.info(response);
              this.init();
              loader.hide();
            });
          }
        });
    }
  },
  mounted() {
    this.init();
  }
};
</script>
<style></style>
