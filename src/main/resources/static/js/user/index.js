/* ================================================================
   user/index.js
   ================================================================ */
$(function () {
    let $table = $('#usersTable');
    if (!$table.length) return;

    $table.DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: '/api/user',
            type: 'GET',
            cache: false,
            dataSrc: function (json) {
                return json.data;
            }
        },
        columns: [
            {data: 'userId', responsivePriority: 1},
            {data: 'name', responsivePriority: 2},
            {data: 'username', responsivePriority: 3},
            {data: 'email', responsivePriority: 6},
            {data: 'phone', responsivePriority: 7},
            {data: 'role', responsivePriority: 4},
            {
                data: 'active',
                responsivePriority: 5,
                render: function (d) {
                    return d
                        ? '<span class="badge bg-success">Active</span>'
                        : '<span class="badge bg-danger">Inactive</span>';
                }
            },
            {data: 'createdAt', responsivePriority: 8},
            {
                data: null,
                className: 'text-center',
                responsivePriority: 1,
                orderable: false,
                render: function () {
                    return '<div class="action-buttons">' +
                        '<button class="btn-icon" title="Edit"><i class="fa-solid fa-pen-to-square"></i></button>' +
                        '<button class="btn-icon" title="Delete"><i class="fa-solid fa-trash"></i></button>' +
                        '</div>';
                }
            }
        ],
        responsive: {
            details: {
                display: $.fn.dataTable.Responsive.display.childRow,
                type: 'column',
                target: 0
            }
        },
        layout: {
            topStart: 'buttons',
            topEnd: 'search',
            top2Start: 'pageLength',
            bottomStart: 'info',
            bottomEnd: 'paging'
        },
        buttons: [
            {extend: 'copy', className: 'btn btn-sm btn-outline-secondary'},
            {extend: 'csv', className: 'btn btn-sm btn-outline-secondary'},
            {extend: 'excel', className: 'btn btn-sm btn-outline-secondary'},
            {extend: 'pdf', className: 'btn btn-sm btn-outline-secondary'},
            {extend: 'print', className: 'btn btn-sm btn-outline-secondary'}
        ],
        pageLength: 10,
        lengthMenu: [10, 20, 50, 100],
        language: {
            search: '',
            searchPlaceholder: 'Search users...',
            lengthMenu: 'Show _MENU_ entries',
            info: 'Showing _START_ to _END_ of _TOTAL_ users',
            paginate: {
                first: '<i class="fa-solid fa-angles-left"></i>',
                last: '<i class="fa-solid fa-angles-right"></i>',
                next: '<i class="fa-solid fa-angle-right"></i>',
                previous: '<i class="fa-solid fa-angle-left"></i>'
            }
        }
    });
});