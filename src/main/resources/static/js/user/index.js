/* ================================================================
   user/index.js
   ================================================================ */
$(function () {
    var $table = $('#usersTable');
    if (!$table.length) return;

    $table.DataTable({
        ajax: {
            url: '/api/user',
            dataSrc: function (json) { return json.data; }
        },
        columns: [
            {data: 'id'},
            {data: 'name'},
            {data: 'username'},
            {data: 'email'},
            {data: 'phone'},
            {data: 'role'},
            {data: 'active', render: function (d) {
                return d
                    ? '<span class="badge badge-success">Active</span>'
                    : '<span class="badge badge-danger">Inactive</span>';
            }},
            {data: 'createdAt'},
            {data: 'id', className: 'text-center', render: function () {
                return '<div class="action-buttons">' +
                    '<button class="btn-icon" title="Edit"><i class="fa-solid fa-pen-to-square"></i></button>' +
                    '<button class="btn-icon" title="Delete"><i class="fa-solid fa-trash"></i></button>' +
                    '</div>';
            }}
        ],
        dom: 'Bfrtip',
        buttons: ['copy', 'csv', 'excel', 'pdf', 'print'],
        pageLength: 10,
        lengthMenu: [10, 25, 50, 100],
        responsive: true,
        language: {
            search: "",
            searchPlaceholder: "Search users...",
            lengthMenu: "Show _MENU_ entries",
            info: "Showing _START_ to _END_ of _TOTAL_ users",
            paginate: {
                first: '<i class="fa-solid fa-angles-left"></i>',
                last: '<i class="fa-solid fa-angles-right"></i>',
                next: '<i class="fa-solid fa-angle-right"></i>',
                previous: '<i class="fa-solid fa-angle-left"></i>'
            }
        }
    });
});
