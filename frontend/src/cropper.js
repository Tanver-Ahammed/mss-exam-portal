// cropper.js — opt-in, loaded only on pages that use Cropper.js
// jQuery 4 (window.$) already available from main.js
import Cropper from 'cropperjs';

$(document).ready(() => {
  $('[data-cropper]').each(function () {
    const cropper = new Cropper(this, {
      aspectRatio: $(this).data('aspect-ratio') ?? 1,
      viewMode: 1,
      autoCropArea: 0.8,
      movable: true,
      zoomable: true,
      rotatable: true,
      scalable: true,
    });
    this._cropper = cropper;
  });

  $('[data-crop-target]').on('click', function () {
    const targetInput = $($(this).data('crop-target'));
    const imgEl = $('[data-cropper]')[0];
    if (!imgEl?._cropper) return;
    imgEl._cropper.getCroppedCanvas().toBlob(blob => {
      const dt = new DataTransfer();
      dt.items.add(new File([blob], 'cropped.png', { type: 'image/png' }));
      targetInput[0].files = dt.files;
    });
  });
});
