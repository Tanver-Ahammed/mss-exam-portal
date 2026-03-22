/* ================================================================
   geo.js - Geo Location Cascade Filter Helper
   ================================================================ */

const GeoFilter = (function () {

    const API = {
        districts: '/api/geo/division/{divisionId}/districts',
        upazilas: '/api/geo/district/{districtId}/upazilas',
    };

    // ── Helpers ────────────────────────────────────────────────

    function isEmpty(value) {
        return !value || value === '' || Number(value) <= 0;
    }

    function getLocalizedText(obj) {
        return (typeof lang !== 'undefined' && lang === 'bn')
            ? obj.nameLocal
            : obj.name;
    }

    function resetSelect($select, disable = true) {
        $select.find('option:not(:first)').remove();
        $select.prop({disabled: disable, selectedIndex: 0});
    }

    function setLoading($select, isLoading) {
        $select.toggleClass('loading-inside-input', isLoading);
    }

    function setWarning($select) {
        $select.removeClass('loading-inside-input').addClass('warning-inside-input');
    }

    function populateSelect($select, items, valueKey, labelFn) {
        const fragment = document.createDocumentFragment();
        items.forEach(item => {
            const opt = document.createElement('option');
            opt.value = item[valueKey];
            opt.textContent = labelFn(item);
            fragment.appendChild(opt);
        });
        $select[0].appendChild(fragment);
        $select.prop('disabled', false);
    }

    // ── Event Handlers ─────────────────────────────────────────

    function onDivisionChange() {
        $('#divisionSelect').on('change', async function () {
            const divisionId = $(this).val();
            const $district = $('#districtSelect');
            const $upazila = $('#upazilaSelect');

            resetSelect($upazila, true);
            resetSelect($district, true);

            if (isEmpty(divisionId)) return;

            setLoading($district, true);

            try {
                const districts = await $.ajax({
                    url: API.districts.replace('{divisionId}', divisionId),
                    type: 'GET',
                });
                populateSelect($district, districts, 'districtId', getLocalizedText);
            } catch (err) {
                console.error('[GeoFilter] Failed to load districts:', err);
                setWarning($district);
            } finally {
                setLoading($district, false);
            }
        });
    }

    function onDistrictChange() {
        $('#districtSelect').on('change', async function () {
            const districtId = $(this).val();
            const $upazila = $('#upazilaSelect');

            resetSelect($upazila, true);

            if (isEmpty(districtId)) return;

            setLoading($upazila, true);

            try {
                const upazilas = await $.ajax({
                    url: API.upazilas.replace('{districtId}', districtId),
                    type: 'GET',
                });
                populateSelect($upazila, upazilas, 'upazilaId', getLocalizedText);
            } catch (err) {
                console.error('[GeoFilter] Failed to load upazilas:', err);
                setWarning($upazila);
            } finally {
                setLoading($upazila, false);
            }
        });
    }

    // ── Init ───────────────────────────────────────────────────

    /**
     * Call this explicitly after the fragment is injected into the DOM.
     * Safe to call multiple times — unbinds previous handlers first.
     */
    function init() {
        // Unbind first to prevent duplicate handlers if fragment is re-loaded
        $('#divisionSelect').off('change');
        $('#districtSelect').off('change');

        onDivisionChange();
        onDistrictChange();
    }

    return {init};

})();