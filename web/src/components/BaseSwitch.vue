<template>
  <label class="custom-toggle">
    <input
      :id="cbId"
      class="custom-control-input"
      type="checkbox"
      autocomplete="off"
      :disabled="disabled"
      v-model="model"
    />

    <span class="custom-toggle-slider rounded-circle"></span>
  </label>
</template>
<script>
import { randomString } from "./stringUtils";

export default {
  name: "base-switch",
  model: {
    prop: "checked",
  },
  props: {
    checked: {
      type: [Array, Boolean],
      description: "Whether checkbox is checked",
    },
    disabled: {
      type: Boolean,
      description: "Whether checkbox is disabled",
    },
    inline: {
      type: Boolean,
      description: "Whether checkbox is inline",
    },
  },
  data() {
    return {
      cbId: "",
      touched: false,
    };
  },
  computed: {
    model: {
      get() {
        return this.checked;
      },
      set(check) {
        if (!this.touched) {
          this.touched = true;
        }
        this.$emit("input", check);
      },
    },
  },
  mounted() {
    this.cbId = randomString();
  },
};
</script>

