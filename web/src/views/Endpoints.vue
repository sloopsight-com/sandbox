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
                  <h3 class="mb-0">Welcome To Sandbox!</h3>
                </div>
              </div>
            </div>
            <template>
              <div class="pl-lg-12">
                <div class="row">
                  <!--Table-->
                  <endpoint-grid></endpoint-grid>
                  <!--Table-->
                </div>
              </div>
            </template>
          </card>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import EndpointGrid from "./Grids/EndpointGrid.vue";

export default {
  components: {
    EndpointGrid
  }
};
</script>
<style></style>
