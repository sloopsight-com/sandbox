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
                    <form @submit.prevent>
                      <base-switch v-model="model.active"></base-switch>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              alternative
                              :disabled="!model.active"
                              required
                              label="Ldap URL"
                              placeholder="ldap[s]://host"
                              input-classes="form-control-alternative"
                              v-model="model.name"
                            />
                          </div>
                        </div>
                      </div>
                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-checkbox
                              class="mb-3"
                              :disabled="!model.active"
                              v-model="model.anonymous"
                            >Anonymous Access</base-checkbox>
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!model.active"
                              alternative
                              required
                              label="Root Bind Dn"
                              placeholder="Enter root dn"
                              input-classes="form-control-alternative"
                              v-model="model.bindDn"
                              v-if="!model.anonymous"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              alternative
                              :disabled="!model.active"
                              required
                              v-if="!model.anonymous"
                              label="Root Bind Password"
                              placeholder="Enter root bind password"
                              input-classes="form-control-alternative"
                              v-model="model.bindPassword"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!model.active"
                              alternative
                              required
                              label="Search Base"
                              placeholder="ou=people,dc=company,dc=com"
                              input-classes="form-control-alternative"
                              v-model="model.searchBase"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!model.active"
                              alternative
                              required
                              label="Search Filter"
                              placeholder="(uid=${username})"
                              input-classes="form-control-alternative"
                              v-model="model.searchFilter"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!model.active"
                              alternative
                              required
                              label="Username Field"
                              placeholder="uid"
                              input-classes="form-control-alternative"
                              v-model="model.usernameField"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="pl-lg-4">
                        <div class="row">
                          <div class="col-lg-6">
                            <base-input
                              :disabled="!model.active"
                              alternative
                              required
                              label="Email Field"
                              placeholder="mail"
                              input-classes="form-control-alternative"
                              v-model="model.emailField"
                            />
                          </div>
                        </div>
                      </div>

                      <div class="row">
                        <div class="col text-right">
                          <base-button type="primary" size="lg" v-on:click="saveProject()">Submit</base-button>
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
export default {
  name: "user-profile",
  data() {
    return {
      model: {
        name: "",
        active: true,
        anonymous: false,
        openApiSpec: "",
        members: [],
      },
      error: null,
      error_message: null,
    };
  },
  components: {},
  methods: {
    checkForm() {
      if (this.model.name && this.model.openApiSpec) {
        return true;
      }
      if (!this.model.name) {
        this.error = true;
        this.error_message = "Name is missing";
        return false;
      }

      if (!this.model.openApiSpec) {
        this.error = true;
        this.error_message = "Spec are missing";
        return false;
      }
    },
    init() {},

    saveProject() {},
  },
  mounted() {
    this.$log.info(this.$route.params.id);
  },
};
</script>
<style></style>
