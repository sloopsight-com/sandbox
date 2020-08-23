<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
      <!-- Card stats -->
    </base-header>

    <div class="container-fluid mt--7">
      <div class="row">
        <div class="col-xl-12 order-xl-1">
          <card shadow type="secondary">
            <div slot="header" class="bg-white border-0">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">Create user</h3>
                </div>
              </div>
            </div>

            <template>
              <base-alert v-show="error" dismissible type="danger">{{error_message}}</base-alert>

              <form @submit.prevent>
                <h6 class="heading-small text-muted mb-4">User information {{isEdit}}</h6>
                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <base-input
                        alternative
                        required
                        :disabled="isEdit"
                        label="User Id"
                        placeholder="Id of the user"
                        input-classes="form-control-alternative"
                        v-model="model.username"
                      />
                    </div>
                  </div>
                </div>
                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <base-input
                        alternative
                        required
                        label="Email"
                        placeholder="name@example.com"
                        input-classes="form-control-alternative"
                        v-model="model.email"
                      />
                    </div>
                  </div>
                </div>

                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <base-input
                        alternative
                        required
                        label="Password"
                        placeholder="User password"
                        input-classes="form-control-alternative"
                        v-model="model.password"
                      />
                    </div>
                  </div>
                </div>

                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <label class="typo__label form-control-label">Select Roles</label>
                      <multiselect
                        v-model="model.role"
                        tag-placeholder="Add this role to user"
                        placeholder="Select Role"
                        label="name"
                        track-by="id"
                        :options="options"
                        :multiple="true"
                        :taggable="true"
                      ></multiselect>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col text-right">
                    <base-button type="primary" size="lg" v-on:click="saveUser()">Submit</base-button>
                  </div>
                </div>
              </form>
            </template>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>
 <script>
// require styles
import "vue-multiselect/dist/vue-multiselect.min.css";

import Multiselect from "vue-multiselect";
import UserService from "../services/user-service";

export default {
  name: "user-profile",
  data() {
    return {
      options: [],
      error: null,
      isEdit: false,
      error_message: null,

      model: {
        username: "",
        password: "",
        email: "",
        role: [],
      },
    };
  },
  components: {
    Multiselect,
  },
  methods: {
    checkForm() {
      if (!this.model.username) {
        this.error = true;
        this.error_message = "User name is missing";
        return false;
      }

      if (!this.model.email) {
        this.error = true;
        this.error_message = "Please provide email";
        return false;
      }

      if (!this.isEdit) {
        if (!this.model.password) {
          this.error = true;
          this.error_message = "Please provide password";
          return false;
        }
      }
      return true;
    },
    init() {},

    saveUser() {
      this.$log.info("Saving user..");

      if (this.checkForm()) {
        this.$log.info("Saving user..");

        var projectPromise = null;
        if (this.$route.params.id) {
          projectPromise = UserService.update(this.$route.params.id, {
            email: this.model.email,
            password: this.model.password,
            role: this.model.role,
          });
        } else {
          projectPromise = UserService.save(this.model);
        }
        projectPromise
          .then(() => {
            this.$router.push("/users");
          })
          .catch((error) => {
            this.error = true;
            if (error.response.data) {
              this.error_message = error.response.data;
            } else {
              this.error_message = "System error please try after some time";
            }
          });
      }
    },
  },
  mounted() {
    this.$log.info(this.$route.params.id);
    var id = 0;
    this.isEdit = false;
    if (this.$route.params.id) {
      this.isEdit = true;
      id = this.$route.params.id;
      UserService.getOne(this.$route.params.id)
        .then((response) => {
          this.model.username = response.data.username;
          this.model.email = response.data.email;
          this.model.role = response.data.roles;
        })
        .catch((error) => {
          this.error = true;
          this.error_message = error;
        });
    }
    UserService.getAvailableRole(id)
      .then((response) => {
        this.options = response.data;
      })
      .catch((error) => {
        this.error = true;
        this.error_message = error;
      });
  },
};
</script>
<style></style>
