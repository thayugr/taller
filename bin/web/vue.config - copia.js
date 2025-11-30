const http = 'localhost:8415'
const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
    //  Directorio de Salida
    outputDir: '../src/main/resources/dist',

    //  Los recursos estáticos utilizan rutas relativas
    publicPath: './',

    //  Configuración del agente
    devServer: {
        host: '0.0.0.0',
        port: '3000',
        proxy: {
            '/api': {
                target: `http://${http}`,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                },
                timeout: 60 * 1000
            },
            '/auth': {
                target: `http://${http}`,
                changeOrigin: true,
                pathRewrite: {
                    '^/auth': '/auth'
                },
                timeout: 60 * 1000
            }
        }
    },

    configureWebpack: {

    },

    chainWebpack: (config) => {
        config.plugin('html').tap((args) => {
            args[0].title = "Sistema Taller 1";
            args[0].build = new Date().getTime();
            args[0].env = process.env.NODE_ENV;
            return args;
        })
    },

    transpileDependencies: [
      'quasar'
    ],

    lintOnSave: false,

    pluginOptions: {
      quasar: {
        importStrategy: 'kebab',
        rtlSupport: false
      }
    }
})
