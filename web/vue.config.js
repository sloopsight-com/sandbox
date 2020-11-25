// vue.config.js
module.exports = {
  // options...

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
        target: "http://localhost:8081/app/",
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          "^/app/camel": ""
        }
      }
    }
  }
};
