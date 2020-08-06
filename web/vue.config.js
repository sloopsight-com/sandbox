
// vue.config.js
module.exports = {
    // options...


    devServer: {
        proxy: {
            '/api/*': {
                target: 'http://localhost:8081/api',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    "^/api": ""
                }
            },
            '/camel/*': {
                target: 'http://localhost:8081/camel',
                ws: true,
                changeOrigin: true,
                pathRewrite: {
                    "^/camel": ""
                }
            }
        }
    }
}
