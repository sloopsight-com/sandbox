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
                      <MonacoEditor
                        :editorMounted="setUpHint"
                        height="500"
                        theme="vs-dark"
                        language="javascript"
                        v-model="model.logic"
                        :options="options"
                      ></MonacoEditor>
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
// require styles
import "codemirror/lib/codemirror.css";
import "codemirror/theme/base16-dark.css";
import "codemirror/addon/hint/show-hint.css";

import "codemirror/mode/javascript/javascript.js";

import "codemirror/addon/hint/show-hint.js";

import "codemirror/addon/hint/javascript-hint.js";
import MonacoEditor from "monaco-editor-vue";
import EndpointService from "../services/endpoint-service";

export default {
  name: "edit-endpoint",
  data() {
    return {
      hints: [],
      options: {
        //Monaco Editor Options
        //minimap:true,
        automaticLayout: true,
        matchBrackets: "always",
        //renderIndentGuides:true,
        dragAndDrop: true,
        suggest: {
          showWords: true
        },
        suggestOnTriggerCharacters: true,
        parameterHints: {
          cycle: true,
          enabled: true
        },
        scrollBeyondLastLine: false,
        statusBar: {
          visible: true
        }
      },
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
    MonacoEditor
  },
  methods: {
    setUpHint(editor, monaco) {
      EndpointService.getHint().then(response => {
        const loadedHints = response.data;
        monaco.languages.registerCompletionItemProvider("javascript", {
          provideCompletionItems: function(model, position) {
            // find out if we are completing a property in the 'dependencies' object.

            var word = model.getWordUntilPosition(position);
            var range = {
              startLineNumber: position.lineNumber,
              endLineNumber: position.lineNumber,
              startColumn: word.startColumn,
              endColumn: word.endColumn
            };
            const monacoHints = [];
            for (let i = 0; i < loadedHints.length; i++) {
              let loadedHint = loadedHints[i];
              monacoHints.push({
                label: '"' + loadedHint.keyword + '"',
                kind: monaco.languages.CompletionItemKind.Function,
                documentation: loadedHint.description,
                insertText: loadedHint.snippet,
                range: range
              });
            }

            return {
              suggestions: monacoHints
            };
          }
        });
      });
    },
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
