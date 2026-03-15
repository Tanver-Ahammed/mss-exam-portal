import {defineConfig} from 'vite'
import tailwindcss from '@tailwindcss/vite'
import {resolve} from 'path'

export default defineConfig({
    plugins: [
        tailwindcss(),   // ✅ Tailwind v4 as a Vite plugin (zero-config)
    ],
    build: {
        // Output directly into Spring Boot's static folder
        manifest: true,
        outDir: resolve(__dirname, '../resources/static/assets'),
        emptyOutDir: true,

        rollupOptions: {
            input: {
                app: resolve(__dirname, 'js/app.js'),
            },
            output: {
                // ✅ Content-hash filenames for aggressive caching in production
                entryFileNames: '[name].[hash].js',
                chunkFileNames: '[name].[hash].js',
                assetFileNames: '[name].[hash][extname]',
            },
        },

        // ✅ Production optimizations
        minify: 'esbuild',       // Fastest minifier
        sourcemap: false,         // Disable in prod
        cssCodeSplit: true,       // Split CSS per entry
        target: 'es2022',         // Modern browsers only
    },
})