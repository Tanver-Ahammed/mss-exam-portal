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
            data: 'status',
            width: '10%',
            render: function (data) {
                if (!USER_STATUS[data]) return '<span class="badge badge-secondary">—</span>';
                return `<span class="badge ${USER_STATUS[data].displayCSS}">
                    ${localizeEnum(data, USER_STATUS)}
                </span>`;
            }
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
    const paginateIcons = {
        first: '<i class="fa-solid fa-angles-left"></i>',
        last: '<i class="fa-solid fa-angles-right"></i>',
        next: '<i class="fa-solid fa-angle-right"></i>',
        previous: '<i class="fa-solid fa-angle-left"></i>'
    };
    const language = {
        search: '',
        searchPlaceholder: USER_MESSAGES.search[lang],
        lengthMenu: USER_MESSAGES.lengthMenu[lang],
        info: USER_MESSAGES.info[lang],
        paginate: paginateIcons
    };

    // ── Filter params ─────────────────────────────────────────────
    const filterParams = () => ({
        omniSearch: $("#omniSearch").val().trim(),
        divisionId: $('#division').val(),
        districtId: $("#district").val(),
        upazilaId: $("#upazila").val(),
        status: $("#status").val(),
    });

// ── Initialize ────────────────────────────────────────────────
    const table = globalDataTableInitialization('usersTable', usersApi, columns, {
        pageLength: 10,
        filterParams,
        language,
        ordering: true,
        scrollToTop: true,
        order: [[0, 'asc']],
    });

// ── Filter form submit — redraw table instead of page reload ──
    $("#filterForm").on("submit", function (e) {
        e.preventDefault();
        table.ajax.reload();
    });

    // ── Reset filter ──────────────────────────────────────────────
    $("#resetFilter").on("click", function () {
        $("#filterForm")[0].reset();
        $('#district').prop('disabled', true).find('option:not(:first)').remove();
        $('#upazila').prop('disabled', true).find('option:not(:first)').remove();
        table.ajax.reload();
    });

    GeoFilter.init();
});