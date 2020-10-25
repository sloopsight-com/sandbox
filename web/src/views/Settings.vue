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
                  <h3 class="mb-0">System Settings</h3>
                </div>
              </div>
            </div>

            <tabs fill class="flex-column flex-md-row">
              <card shadow>
                <tab-pane>
                  <span slot="title">
                    <i class="ni ni ni-settings-gear-65" />
                    LDAP Authentication
                  </span>

                  <template>
                    <base-alert v-show="error" dismissible type="danger">{{error_message}}</base-alert>
                    <base-alert
                      v-show="success_message"
                      dismissible
                      type="success"
                    >{{success_message}}</base-alert>

                    <form @submit.prevent>
                      <base-switch v-model="authConfig.enabled"></base-switch>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              alternative
                              :disabled="!authConfig.enabled"
                              required
                              label="Ldap URL"
                              placeholder="ldap[s]://host"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.url"
                            />
                          </div>
                        </div>
                      </div>
                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-checkbox
                              class="mb-3"
                              :disabled="!authConfig.enabled"
                              v-model="authConfig.config.anonymous"
                            >Anonymous Access</base-checkbox>
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!authConfig.enabled"
                              alternative
                              required
                              label="Root Bind Dn"
                              placeholder="Enter root dn"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.bindDn"
                              v-if="!authConfig.config.anonymous"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              alternative
                              :disabled="!authConfig.enabled"
                              required
                              v-if="!authConfig.config.anonymous"
                              label="Root Bind Password"
                              placeholder="Enter root bind password"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.bindPassword"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!authConfig.enabled"
                              alternative
                              required
                              label="Search Base"
                              placeholder="ou=people,dc=company,dc=com"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.searchBase"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!authConfig.enabled"
                              alternative
                              required
                              label="Search Filter"
                              placeholder="(uid=${username})"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.searchFilter"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!authConfig.enabled"
                              alternative
                              required
                              label="Username Field"
                              placeholder="uid"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.usernameField"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!authConfig.enabled"
                              alternative
                              required
                              label="Email Field"
                              placeholder="mail"
                              input-classes="form-control-alternative"
                              v-model="authConfig.config.emailField"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col text-right">
                          <base-button
                            type="primary"
                            size="lg"
                            v-on:click="saveConfig('ldap')"
                          >Submit</base-button>
                        </div>
                      </div>
                    </form>
                  </template>
                </tab-pane>
              </card>
            </tabs>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>
 <script>
import AuthService from "../services/auth-service";

export default {
  name: "user-profile",
  data() {
    return {
      authConfig: {
        enabled: true,
        config: {},
      },
      error: null,
      error_message: null,
      success_message: null,
    };
  },
  components: {},
  methods: {
    checkForm() {
      if (!this.authConfig.enabled) {
        return true;
      }

      if (this.authConfig.config.anonymous) {
        if (!this.authConfig.config.bindDn) {
          this.error = true;
          this.error_message = "Bind dn is missing";
          return false;
        }
        if (!this.authConfig.config.bindPassword) {
          this.error = true;
          this.error_message = "Nind password is missing";
          return false;
        }
      }
      if (!this.authConfig.config.searchBase) {
        this.error = true;
        this.error_message = "Search base is missing";
        return false;
      }
      if (!this.authConfig.config.searchFilter) {
        this.error = true;
        this.error_message = "Search filter is missing";
        return false;
      }
      if (!this.authConfig.config.userField) {
        this.error = true;
        this.error_message = "User field is missing";
        return false;
      }
      if (!this.authConfig.config.emailField) {
        this.error = true;
        this.error_message = "Email field is missing";
        return false;
      }

      return true;
    },
    initLdap() {
      const loader = this.$loading.show({ loader: "bars", color: "#2dce89" });
      AuthService.getConfig("ldap")
        .then((response) => {
          this.authConfig = response.data;
          loader.hide();
        })
        .catch((error) => {
          this.error = true;
          this.error_message = error;
        });
    },

    saveConfig(name) {
      this.$log.info(JSON.stringify(this.authConfig));

      AuthService.updateConfig(name, {
        enabled: this.authConfig.enabled,
        config: this.authConfig.config,
      })
        .then((response) => {
          this.authConfig = response.data;
          this.$notify({
            title: "Config updated successfully",
            delay: 3000,
            type: "success",
            icon: "fa fa-info",
          });
        })
        .catch((error) => {
          this.error = true;
          this.error_message = error;
        });
    },
  },

  mounted() {
    this.initLdap();
  },
};
</script>
<style></style>
