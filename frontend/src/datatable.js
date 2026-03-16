// datatable.js — opt-in, loaded only on pages that use DataTables
// jQuery 4 (window.$) already available from main.js
import DataTable from 'datatables.net-dt';

$(document).ready(() => {
  $('[data-datatable]').each(function () {
    new DataTable(this, {
      pageLength: $(this).data('page-length') || 10,
      responsive: true,
      language: { search: 'Search:', lengthMenu: 'Show _MENU_ entries' },
    });
  });
});
