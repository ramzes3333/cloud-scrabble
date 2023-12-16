const webpack = require('webpack');

module.exports = {
  plugins: [
    new webpack.DefinePlugin({
      'process.env.KEYCLOAK_URL': JSON.stringify(process.env.KEYCLOAK_URL)
    })
  ]
};
