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
        target: "https://sandbox.generalmobi.mobi/app/api",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/app/api": ""
        }
      },
      "/camel/*": {
        target: "https://sandbox.generalmobi.mobi/app/camel/",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/app/camel": ""
        }
      }
    }
  }
};
