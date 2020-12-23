const MonacoWebpackPlugin = require("monaco-editor-webpack-plugin");
// vue.config.js
module.exports = {
  // options...

  chainWebpack: config => {
    config.plugin("monaco-editor").use(MonacoWebpackPlugin, [
      {
        // Languages are loaded on demand at runtime
        languages: ["javascript"]
      }
    ]);
  },
  publicPath: "/app",
  devServer: {
    proxy: {
      "/api/*": {
        target: "http://localhost:8081/app/api",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/app/api": ""
        }
      },
      "/camel/*": {
        target: "http://localhost:8081/app/camel/",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/app/camel": ""
        }
      }
    }
  }
};
