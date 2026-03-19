/* ================================================================
   common.js  —  Global DataTable initialization wrapper
   ================================================================ */

const BENGALI_LOCALE = 'bn-BN';
const ROW_DISPLAY_GLOBAL = 1;
const PAGE_LENGTHS = [10, 25, 50, 100];
paginationSizeSelectTitle = 'প্রতি পাতায় {0}'

/**
 * Initializes a server-side DataTable with shared project defaults.
 *
 * @param {string}          tableId       - ID of the <table> element (without #)
 * @param {string}          url           - AJAX endpoint URL
 * @param {Array}           columns       - DataTables column definitions
 * @param {Object}          [options]     - Optional overrides
 * @param {number}          [options.pageLength=PAGE_SIZE]
 * @param {Function|null}   [options.callBack=null]        - Runs after every draw
 * @param {Object}          [options.language={}]          - DataTables language config
 * @param {Object|Function} [options.filterParams={}]      - Extra AJAX query params
 * @param {boolean}         [options.ordering=true]
 * @param {boolean}         [options.scrollToTop=true]
 * @param {number}          [options.page=0]               - Initial page index
 * @param {Array}           [options.order=[[0,'asc']]]    - Default sort order
 * @param {Function|null}   [options.rowCallBack=null]     - createdRow callback
 *
 * @returns {DataTables.Api} The initialized DataTable instance
 */
function globalDataTableInitialization(
    tableId,
    url,
    columns,
    {
        pageLength = PAGE_SIZE,
        callBack = null,
        language = {},
        filterParams = {},
        ordering = true,
        scrollToTop = true,
        page = 0,
        order = [[0, 'asc']],
        rowCallBack = null,
    } = {}
) {
    // Bengali locale: convert pagination numbers to local script
    const lengthMenuLabels = lang === 'bn'
        ? [PAGE_LENGTHS, PAGE_LENGTHS.map(n => n.toLocaleString(BENGALI_LOCALE))]
        : [PAGE_LENGTHS, PAGE_LENGTHS];

    // Normalise filterParams to a function so AJAX data always calls it the same way
    const resolvedFilterParams = typeof filterParams === 'function'
        ? filterParams
        : () => filterParams;

    const config = {
        dom: '<"top">rt<"bottom"lip><"clear">',
        lengthMenu: lengthMenuLabels,
        ordering,
        searching: false,
        conditionalPaging: true,
        info: true,
        pageLength,
        displayStart: pageLength * page,
        iDisplayLength: ROW_DISPLAY_GLOBAL,
        language,
        oLanguage: {
            sLengthMenu: paginationSizeSelectTitle.replace('{0}', '_MENU_'),
        },
        ajax: {
            url,
            data: d => ({...d, ...resolvedFilterParams()}),
        },
        processing: true,
        serverSide: true,
        scrollX: true,
        columns,
        order,
        fnDrawCallback: callBack ?? undefined,
        createdRow: rowCallBack
            ? (row, data, index) => rowCallBack(row, data, index)
            : undefined,
    };

    const table = $(`#${tableId}`).DataTable(config);

    // Post-init DOM tweaks
    $(`#${tableId} thead tr`).addClass('head_row');
    $(`#${tableId}_length label select`)
        .removeClass('custom-select custom-select-sm')
        .addClass('ml-1');

    if (scrollToTop) {
        table.on('page.dt', () => {
            $('html, body').animate({scrollTop: 0}, 'slow');
            $('main').focus();
        });
    }

    return table;
}