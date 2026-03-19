/* ================================================================
   user/index.js
   ================================================================ */
$(function () {
    if (!$('#usersTable').length) return;

    // ── Column definitions ────────────────────────────────────────
    const columns = [
        {
            title: '#',
            data: 'userId',
            width: '5%',
        },
        {
            title: 'Name',
            data: 'name',
            width: '15%',
        },
        {
            title: 'Username',
            data: 'username',
            width: '15%',
        },
        {
            title: 'Email',
            data: 'email',
            width: '20%',
        },
        {
            title: 'Phone',
            data: 'phone',
            width: '10%',
        },
        {
            title: 'Role',
            data: 'role',
            width: '10%',
        },
        {
            title: 'Status',
            data: 'active',
            width: '10%',
            render: (data) => data
                ? '<span class="badge bg-success">Active</span>'
                : '<span class="badge bg-danger">Inactive</span>'
        },
        {
            title: 'Created At',
            data: 'createdAt',
            width: '10%',
        },
        {
            title: 'Action',
            data: null,
            width: '5%',
            className: 'text-center',
            orderable: false,
            render: () => `
                <div class="action-buttons">
                    <button class="btn-hover-blue btn-icon" title="Edit"><i class="fa-solid fa-pen-to-square"></i></button>
                    <button class="btn-hover-red btn-icon" title="Delete"><i class="fa-solid fa-trash"></i></button>
                </div>`
        }
    ];

    // ── Language overrides ────────────────────────────────────────
    const language = {
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
    };

    // ── Initialize ────────────────────────────────────────────────
    globalDataTableInitialization('usersTable', usersApi, columns, {
        pageLength: 10,
        language,
        ordering: true,
        scrollToTop: true,
        order: [[0, 'asc']],
    });
});