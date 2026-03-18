/* ================================================================
   layout.js
   Place at:  src/main/resources/static/js/layout.js
   Requires:  jQuery 4 (loaded before this script)
   ================================================================ */
$(function () {

    const $sidebar = $('#sidebar');
    const $wrapper = $('#mainWrapper');
    const KEY = 'ep_sidebar_collapsed';
    const isMobile = () => window.innerWidth <= 768;

    /* ── Restore collapsed state on load ───────────────────────── */
    if (!isMobile() && localStorage.getItem(KEY) === '1') {
        $sidebar.addClass('collapsed');
        $wrapper.addClass('collapsed');
    }

    /* ── Mobile backdrop ────────────────────────────────────────── */
    const $backdrop = $('<div id="sidebarBackdrop" class="sidebar-backdrop"></div>');
    $('body').append($backdrop);

    /* ── Toggle: sidebar ─────────────────────────────────────────── */
    function toggleSidebar() {
        if (isMobile()) {
            $sidebar.toggleClass('mobile-open');
            $backdrop.toggleClass('active');
        } else {
            toggleCollapse();
        }
    }

    $('#topbarToggle, #collapseBtn').on('click', toggleSidebar);

    function toggleCollapse() {
        $sidebar.toggleClass('collapsed');
        $wrapper.toggleClass('collapsed');
        localStorage.setItem(KEY, $sidebar.hasClass('collapsed') ? '1' : '0');
    }

    /* ── Close on backdrop click ────────────────────────────────── */
    $backdrop.on('click', function () {
        $sidebar.removeClass('mobile-open');
        $backdrop.removeClass('active');
    });

    /* ── User dropdown ──────────────────────────────────────────── */
    $('#userBtn').on('click', function (e) {
        e.stopPropagation();
        const open = $('#userPanel').toggleClass('open').hasClass('open');
        $('#userChevron').toggleClass('open', open);
    });

    $(document).on('click', function () {
        $('#userPanel').removeClass('open');
        $('#userChevron').removeClass('open');
    });

    /* ── ESC closes everything ──────────────────────────────────── */
    $(document).on('keydown', function (e) {
        if (e.key !== 'Escape') return;
        $sidebar.removeClass('mobile-open');
        $backdrop.removeClass('active');
        $('#userPanel').removeClass('open');
        $('#userChevron').removeClass('open');
    });

    /* ── Responsive: clean up on resize ────────────────────────── */
    $(window).on('resize', $.debounce ? $.debounce(100, handleResize) : handleResize);

    function handleResize() {
        if (!isMobile()) {
            $sidebar.removeClass('mobile-open');
            $backdrop.removeClass('active');
            // Re-apply persisted state
            if (localStorage.getItem(KEY) === '1') {
                $sidebar.addClass('collapsed');
                $wrapper.addClass('collapsed');
            } else {
                $sidebar.removeClass('collapsed');
                $wrapper.removeClass('collapsed');
            }
        } else {
            $sidebar.removeClass('collapsed');
            $wrapper.removeClass('collapsed');
        }
    }

    /* ── Theme toggle ────────────────────────────────────────────── */
    const THEME_KEY = 'ep_theme';
    const $themeIcon = $('#themeIcon');

    function applyTheme(theme) {
        if (theme === 'light') {
            $('html').attr('data-theme', 'light');
            $themeIcon.removeClass('fa-moon').addClass('fa-sun');
        } else {
            $('html').removeAttr('data-theme');
            $themeIcon.removeClass('fa-sun').addClass('fa-moon');
        }
        localStorage.setItem(THEME_KEY, theme);
    }

    const savedTheme = localStorage.getItem(THEME_KEY) || 'dark';
    applyTheme(savedTheme);

    $('#themeBtn').on('click', function () {
        const current = $('html').attr('data-theme') === 'light' ? 'light' : 'dark';
        applyTheme(current === 'light' ? 'dark' : 'light');
    });

    /* ── AJAX default headers ───────────────────────────────────── */
    $.ajaxSetup({contentType: 'application/json'});

    /* ── Language dropdown ───────────────────────────────────────── */
    const $langBtn = $('#langBtn');
    const $langPanel = $('#langPanel');

    if ($langBtn.length && $langPanel.length) {
        $langBtn.on('click', function (e) {
            e.stopPropagation();
            const open = $langPanel.toggleClass('show').hasClass('show');
            $langBtn.toggleClass('active', open);
        });

        $(document).on('click', function (e) {
            if (!$langBtn[0].contains(e.target) && !$langPanel[0].contains(e.target)) {
                $langPanel.removeClass('show');
                $langBtn.removeClass('active');
            }
        });
    }

});