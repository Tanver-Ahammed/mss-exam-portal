/* ================================================================
   user/index.js
   ================================================================ */
$(function () {
    if (!$('#usersTable').length) return;

    // ── Column definitions ────────────────────────────────────────
    const columns = [
        {
            sTitle: '',
            data: "userId",
            width: '10%',
            render: function (data) {
                return data || '';
            }
        },
        {
            sTitle: name,
            data: null,
            width: '20%',
            render: function (data) {
                return `
            <div class="row py-1">
                <span class="col-3 col-3 fw-bold">${name}</span> 
                <span class="col-7 ml-n4 text-break">${localizeEntityName(data)}</span>
            </div> 
            <div class="row py-1">
                <span class="col-3 col-3 fw-bold">${username}</span> 
                <span class="col-7 text-break ml-n4 text-break">${data.username}</span>
            </div>`;
            }
        },
        {
            sTitle: contact,
            data: null,
            width: '25%',
            render: function (data) {
                return `
            <div class="row py-1">
                <span class="col-3 col-3 fw-bold">${email}</span> 
                <span class="col-7 ml-n4 text-break">${data.email}</span>
            </div> 
            <div class="row py-1">
                <span class="col-3 col-3 fw-bold">${phone}</span> 
                <span class="col-7 text-break ml-n4 text-break">${data.phone}</span>
            </div>`;
            }
        },
        {
            sTitle: role,
            data: "role",
            width: '10%',
            render: function (data) {
                return data || '';
            }
        },
        {
            sTitle: status,
            data: 'status',
            width: '10%',
            render: function (data) {
                if (!USER_STATUS[data]) {
                    return '<span class="badge badge-secondary">—</span>';
                }
                return `<span class="badge ${USER_STATUS[data].displayCSS} fw-bold">
                ${localizeEnum(data, USER_STATUS)}
            </span>`;
            }
        },
        {
            sTitle: action,
            data: null,
            width: '10%',
            className: 'text-center',
            orderable: false,
            render: () => `
            <div class="action-buttons">
                <button class="btn-hover-blue btn-icon" title="Edit">
                    <i class="fa-solid fa-pen-to-square"></i>
                </button>
                <button class="btn-hover-red btn-icon" title="Delete">
                    <i class="fa-solid fa-trash"></i>
                </button>
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