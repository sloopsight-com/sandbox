<template>
  <div>
    <base-header type="gradient-success" class="pb-6 pb-8 pt-5 pt-md-8">
      <!-- Card stats -->
      <div class="row">
        <div class="col-xl-3 col-lg-6">
          <stats-card
            title="Total Apps"
            type="gradient-red"
            sub-title="1"
            icon="ni ni-active-40"
            class="mb-4 mb-xl-0"
          ></stats-card>
        </div>
        <div class="col-xl-3 col-lg-6">
          <stats-card
            title="Total traffic"
            type="gradient-orange"
            sub-title="2,356"
            icon="ni ni-chart-pie-35"
            class="mb-4 mb-xl-0"
          ></stats-card>
        </div>
        <div class="col-xl-3 col-lg-6">
          <stats-card
            title="Current Plan"
            type="gradient-green"
            sub-title="Unlimitted"
            icon="ni ni-money-coins"
            class="mb-4 mb-xl-0"
          ></stats-card>
        </div>
        <div class="col-xl-3 col-lg-6">
          <stats-card
            title="Average Request"
            type="gradient-info"
            sub-title="0/sec"
            icon="ni ni-chart-bar-32"
            class="mb-4 mb-xl-0"
          ></stats-card>
        </div>
      </div>
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
              <h6 class="heading-small text-muted mb-4">Information</h6>
              <div class="pl-lg-12">
                <div class="row">
                  <div class="col-lg-12">
                    <p>What is Sandbox ?</p>
                    <p>A sandbox is a testing environment that isolates untested code changes and outright experimentation from the production environment or repository.</p>
                    <p>Sandboxing protects "live" servers and their data, vetted source code distributions, and other collections of code, data and/or content, proprietary or public, from changes that could be damaging to a mission-critical system or which could simply be difficult to revert, regardless of the intent of the author of those changes.</p>
                    <p>Sandboxes replicate at least the minimal functionality needed to accurately test the programs or other code under development</p>
                    <p>Currrent version supports following methods</p>
                    <ul>
                      <li>GET</li>
                      <li>POST</li>
                      <li>PUT</li>
                      <li>DELETE</li>
                    </ul>
                    <p>This tool allow you to write logical stub using java script via Nashorn Engine</p>
                  </div>
                </div>
              </div>
            </template>
          </card>
        </div>
      </div>
    </div>
    <!--Table-->

    <!--Table-->
  </div>
</template>
<script>
</script>
<style></style>
