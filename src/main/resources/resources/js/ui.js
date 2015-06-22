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
        dialog.find('.cancel').html('Abbrechen').off("click").click(function (event) {
            callback.call(this, {
                action: 'cancel'
            });
            dialog.modal('hide');
        });
        dialog.modal('show');
        input.focus();
        input.val(defaultValue);
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
        dialog.find('.cancel').html('Nein').off("click").click(function (event) {
            callback.call(this, {
                action: 'no'
            });
            dialog.modal('hide');
        });
        dialog.modal('show');
    };
})(mindcloud.ui);