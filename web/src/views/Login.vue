<template>
  <div class="row justify-content-center">
    <div class="col-lg-5 col-md-7">
      <div class="card bg-secondary shadow border-0">
        <div class="card-body px-lg-5 py-lg-5">
          <div class="text-center text-muted mb-4">
            <small>Sign in with credentials</small>
          </div>
          <base-alert v-show="error" dismissible type="danger">{{
            error_message
          }}</base-alert>
          <form role="form" v-on:submit.prevent="callLogin()">
            <base-input
              class="input-group-alternative mb-3"
              placeholder="User ID"
              addon-left-icon="ni ni-single-02"
              v-model="model.email"
            ></base-input>

            <base-input
              class="input-group-alternative"
              placeholder="Password"
              type="password"
              addon-left-icon="ni ni-lock-circle-open"
              v-model="model.password"
            ></base-input>

            <div class="text-center">
              <base-button type="primary" v-on:click="callLogin()" class="my-4"
                >Sign in</base-button
              >
            </div>
          </form>
        </div>
      </div>
      <div class="row mt-3">
        <div class="col-6">
          <a href="#" class="text-light">
            <small>Forgot password?</small>
          </a>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import ability from "../ability";

export default {
  name: "login",
  data() {
    return {
      error: false,
      error_message: "",
      model: {
        email: "",
        password: ""
      }
    };
  },
  methods: {
    callLogin() {
      const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });

      this.error = false;
      this.$store
        .dispatch("login", {
          user: this.model.email,
          password: this.model.password
        })
        .then(response => {
          ability.update(response.data.roles, this.$ability);
          loader.hide();
          this.$router.push("/dashboard");
        })
        .catch(error => {
          this.error = true;
          this.error_message = error;
          loader.hide();
        });
    }
  }
};
</script>
<style></style>
