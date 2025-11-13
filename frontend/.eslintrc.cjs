/* eslint-env node */
require('@eslint/js')
module.exports = {
  extends: ['eslint:recommended'],
  parserOptions: {
    ecmaVersion: 'latest',
  },
  rules: {
    'no-unused-vars': 'warn',
    'no-console': 'off',
  },
}

