mindcloud.notify = {};
(function (notify) {
    var notification = 'notification';
    var content = 'notification-content';
    var timeout = {};
    var state = {
        success: 'success',
        info: 'info',
        warn: 'warning',
        error: 'danger'
    };

    notify.init = function () {
        $('<div>').attr({
            'class': 'notification alert alert-dismissable',
            'id': notification
        }).appendTo('body');
        $('<span>').attr('class', 'close').appendTo('#' + notification).html('&times;').click(hide);
        $('<span>').attr('id', content).appendTo('#' + notification);
    };

    notify.run = function () {
        if (!Notification) {
            return;
        }
        if (Notification.permission !== "granted") {
            Notification.requestPermission();
        }
    }

    notify.desktop = function (title, message, icon, callback) {
        if (!Notification) {
            return;
        }
        var notification = new Notification(title, {
            icon: icon,
            body: message
        });
        if (callback != undefined) {
            notification.onclick = callback;
        }
    };

    notify.success = function (message) {
        show(message, state.success);
    };

    notify.info = function (message) {
        show(message, state.info);
    };

    notify.warn = function (message) {
        show(message, state.warn);
    };

    notify.error = function (message) {
        show(message, state.error);
    };

    function show(message, type) {
        $('#' + content).html(message);
        var box = $('#' + notification);
        for (key in state) {
            box.removeClass("alert-" + state[key]);
        }
        box.addClass('alert-' + type);
        box.fadeIn();
        box.css('margin-left', -box.outerWidth() / 2);
        timeout = setTimeout(hide, 7000);
    }

    function hide() {
        clearTimeout(timeout);
        $('#' + notification).fadeOut();
    }
})(mindcloud.notify);