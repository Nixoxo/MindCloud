mindcloud.ui = {};
(function (ui) {
    var dialogTemplate = '<div class="modal" tabindex="-1" role="dialog">' +
        '<div class="modal-dialog">' +
        '<div class="modal-content">' +
        '<div class="modal-header">' +
        '<h4 class="modal-title" id="myModalLabel"></h4></div>' +
        '<div class="modal-body"></div>' +
        '<div class="modal-footer">' +
        '<div class="btn-group"><button type="button" class="cancel btn btn-default"></button><button type="button" class="confirm btn btn-primary"></button></div>' +
        '</div></div></div></div>';

    var dialog;

    ui.init = function () {
        dialog = $(dialogTemplate).appendTo('body');
    };

    ui.showInputDialog = function (title, hint, defaultValue, callback) {
        dialog.find('.modal-dialog').addClass('modal-sm');
        dialog.find('.modal-title').html(title);
        var input = $('<input>').attr({
            'class': 'form-control',
            'placeholder': hint
        });
        input.keyup(function (event) {
            if (event.keyCode == 13) {
                callback.call(this, {
                    action: 'ok',
                    input: input.val()
                });
                dialog.modal('hide');
            }
        });
        dialog.find('.modal-body').html(input);
        dialog.find('.confirm').html('OK').off("click").click(function (event) {
            callback.call(this, {
                action: 'ok',
                input: input.val()
            });
            dialog.modal('hide');
        });
        dialog.find('.cancel').removeClass('hidden').html('Abbrechen').off("click").click(function (event) {
            callback.call(this, {
                action: 'cancel'
            });
            dialog.modal('hide');
        });
        dialog.modal('show');
        input.focus();
        input.val(defaultValue);
    };

    ui.showMessageDialog = function (title, message) {
        dialog.find('.modal-dialog').removeClass('modal-sm');
        dialog.find('.modal-title').html(title);
        dialog.find('.modal-body').html(message);
        dialog.find('.confirm').html('OK').off("click").click(function (event) {
            dialog.modal('hide');
        });
        dialog.find('.cancel').addClass('hidden').html('Abbrechen').off("click").click(function (event) {
            dialog.modal('hide');
        });
        dialog.modal('show');
    };

    ui.showConfirmDialog = function (title, message, callback) {
        dialog.find('.modal-dialog').removeClass('modal-sm');
        dialog.find('.modal-title').html(title);
        dialog.find('.modal-body').html(message);
        dialog.find('.confirm').html('Ja').off("click").click(function (event) {
            callback.call(this, {
                action: 'yes'
            });
            dialog.modal('hide');
        });
        dialog.find('.cancel').removeClass('hidden').html('Nein').off("click").click(function (event) {
            callback.call(this, {
                action: 'no'
            });
            dialog.modal('hide');
        });
        dialog.modal('show');
    };

    ui.showImageExportDialog = function (title, callback) {
        dialog.find('.modal-dialog').removeClass('modal-sm');
        dialog.find('.modal-title').html(title);
        var format = '<p class="bold">Dateiformat:</p><div class="radio"><label><input name="format" value="png" type="radio" checked> *.png</label></div><div class="radio"><label><input name="format" value="jpg" type="radio"> *.jpg</label></div>';
        var background = '<p class="bold">Hintergrundfarbe:</p><div id="export-image-background-color" class="input-group"><input type="text" value="transparent" class="form-control" /><span class="input-group-addon"><i></i></span></div>';
        var viewOnly = '<p class="bold">Größe:</p><div class="checkbox"><label><input name="full" value="full" type="checkbox"> Nur Anzeigtes verwenden</label></div><div class="input-group"><label>Skalierungsfaktor: </label><input type="number" value="1" min="1" step="any" class="form-control" /></div>';
        var body = format + background + viewOnly;
        dialog.find('.modal-body').html(body);
        dialog.find('#export-image-background-color').colorpicker({
            sliders: {
                saturation: {
                    maxLeft: 150,
                    maxTop: 150
                },
                hue: {
                    maxTop: 150
                },
                alpha: {
                    maxTop: 150
                }
            }
        });
        dialog.find('.confirm').html('OK').off("click").click(function (event) {
            callback.call(this, {
                action: 'ok',
                options: {
                    format: dialog.find('input[name="format"]:checked').val(),
                    bg: dialog.find('#export-image-background-color').data('colorpicker').color,
                    full: !dialog.find('input[name="full"]').is(':checked'),
                    scale: dialog.find('input[type="number"]').val()
                }
            });
            dialog.modal('hide');
        });
        dialog.find('.cancel').removeClass('hidden').html('Abbrechen').off("click").click(function (event) {
            callback.call(this, {
                action: 'cancel'
            });
            dialog.modal('hide');
        });
        dialog.modal('show');
    };
})(mindcloud.ui);