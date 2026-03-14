// ✅ Import Tailwind CSS (Vite handles it)
import '../css/app.css'

// ✅ Import jQuery as an ES module
import $ from 'jquery'

// Make jQuery globally available (optional, for legacy inline scripts)
window.$ = window.jQuery = $

// Your app logic
$(document).ready(() => {
    console.log('Spring Boot + Tailwind v4 + jQuery ready 🚀')

    $('#btn-example').on('click', function () {
        $(this).toggleClass('bg-blue-600 bg-green-500')
    })
})