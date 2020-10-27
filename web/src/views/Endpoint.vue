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
                  <h3 class="mb-0">Edit Endpoint</h3>
                </div>
              </div>
            </div>

            <template>
              <base-alert v-show="error" dismissible type="danger">{{
                error_message
              }}</base-alert>

              <form @submit.prevent>
                <!-- Description -->
                <h6 class="heading-small text-muted mb-4">
                  Login in Java
                  <Script></Script>
                </h6>
                <div class="pl-lg-4">
                  <div class="form-group">
                    <base-input alternative label="Logic in Nashorn Javasript">
                      <codemirror
                        v-model="model.logic"
                        :options="cmOptions"
                      ></codemirror>
                    </base-input>
                  </div>
                </div>
                <div class="row">
                  <div class="col text-right">
                    <base-button
                      type="primary"
                      size="lg"
                      v-on:click="saveEndpoint()"
                      >Submit</base-button
                    >
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
import CodeMirror from "codemirror";

import { codemirror } from "vue-codemirror";

// require styles
import "codemirror/lib/codemirror.css";
import "codemirror/theme/base16-dark.css";
import "codemirror/addon/hint/show-hint.css";

import "codemirror/mode/javascript/javascript.js";

import "codemirror/addon/hint/show-hint.js";

import "codemirror/addon/hint/javascript-hint.js";

import EndpointService from "../services/endpoint-service";

export default {
  name: "edit-endpoint",
  data() {
    return {
      error: null,
      error_message: null,
      model: {
        logic: ""
      },
      cmOptions: {
        // codemirror options
        tabSize: 4,
        mode: { name: "javascript", globalVars: true },
        theme: "base16-dark",
        lineNumbers: true,
        line: true,
        extraKeys: { "Ctrl-Space": "autocomplete" }
        //hintOptions: { hint: this.synonyms }
        // more codemirror options, 更多 codemirror 的高级配置...
      }
    };
  },
  components: {
    codemirror
  },
  methods: {
    checkForm() {
      if (!this.model.logic) {
        this.error = true;
        this.error_message = "Logic is missing";
        return false;
      }
      return true;
    },
    init() {},
    saveEndpoint() {
      if (this.checkForm()) {
        var projectPromise = null;
        if (this.$route.params.id) {
          projectPromise = EndpointService.update(
            this.$route.params.id,
            this.model.logic
          );
        }

        projectPromise
          .then(() => {
            this.$notify({
              title: "Endpoint save successfully",
              type: "success",

              delay: 3000,
              icon: "fa fa-info"
            });

            this.$router.push(
              "/project/" + this.$route.params.projectId + "/endpoint"
            );
          })
          .catch(error => {
            this.error = true;
            this.error_message = error;
          });
      }
    }
  },
  mounted() {
    this.$log.info(this.$route.params.id);

    EndpointService.getHint().then(response => {
      this.$log.info("********************");

      this.$log.info(response);
      CodeMirror.hint.javascript = function(editor) {
        var list = response.data;
        var cursor = editor.getCursor();
        var currentLine = editor.getLine(cursor.line);
        var start = cursor.ch;
        var end = start;
        while (
          end < currentLine.length &&
          /[\w$]+/.test(currentLine.charAt(end))
        )
          ++end;
        while (start && /[\w$]+/.test(currentLine.charAt(start - 1))) --start;
        var curWord = start != end && currentLine.slice(start, end);
        var regex = new RegExp("^" + curWord, "i");
        var result = {
          list: (!curWord
            ? list
            : list.filter(function(item) {
                return item.match(regex);
              })
          ).sort(),
          from: CodeMirror.Pos(cursor.line, start),
          to: CodeMirror.Pos(cursor.line, end)
        };

        return result;
      };
    });

    if (this.$route.params.id) {
      EndpointService.getOne(this.$route.params.id)
        .then(response => {
          this.model.logic = response.data.logic;
        })
        .catch(error => {
          this.error = true;
          this.error_message = error;
        });
    }
  }
};
</script>

<style></style>
