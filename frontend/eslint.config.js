export default [{
  files: ['**/*.{js,jsx}'],
  ignores: ['dist/**', 'node_modules/**'],
  languageOptions: { parserOptions: { ecmaFeatures: { jsx: true } } },
  rules: {
    'constructor-super': 'error',
    'no-unreachable': 'error',
    'no-duplicate-case': 'error',
    'no-dupe-args': 'error',
    'valid-typeof': 'error',
    'no-constant-condition': 'error',
    'no-self-assign': 'error',
    'no-unneeded-ternary': 'error',
    'no-useless-return': 'error',
    'complexity': ['error', 25],
  },
}];
