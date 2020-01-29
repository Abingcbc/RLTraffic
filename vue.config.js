module.exports = {
  publicPath: process.env.NODE_ENV === 'production'
    ? './'
    : '/',
  devServer: {
    open: true,
    host: 'localhost',
    port: 8080,
    https: false,
    proxy: {
      '/source': {
        target: 'https://raw.githubusercontent.com/uber-common/deck.gl-data/master/examples/trips/',
        ws: true,
        changOrigin: true,
        pathRewrite: {
          '^/source': ''
        }
      },
      '/style': {
        target: 'https://maps.tilehosting.com/styles/darkmatter/',
        ws: true,
        changOrigin: true,
        pathRewrite: {
          '^/style': ''
        }
      }
    }
  }
}
