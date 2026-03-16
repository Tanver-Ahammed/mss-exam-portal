/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    '../src/main/resources/templates/**/*.html',
    './src/**/*.{js,ts}',
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          50:  '#f0f9ff',
          500: '#0ea5e9',
          900: '#0c4a6e',
        },
      },
    },
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography'),
  ],
};
