// main.js — loaded by base layout on EVERY page
// Sets up jQuery 4 as a global. Nothing else.
import $ from 'jquery';

window.$ = window.jQuery = $;

$(document).ready(() => {
  setTimeout(() => { $('.flash-message').fadeOut(400); }, 3000);
});
