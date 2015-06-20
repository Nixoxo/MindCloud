mindcloud.client = {};
(function (client) {
    client.config = {
        host: window.location.hostname,
        port: window.location.port,
        path: 'mindcloud'
    };

    var actions = {};

    client.run = function () {
        var url = client.config.host + ':' + client.config.port + '/' + client.config.path;
        var socket = new WebSocket('ws://' + url);
        if (socket == undefined) {
            necon.notify.warn('WebSockets werden in Ihrem Browser nicht unterstützt. Deshalb ist die Anwendung nicht vollständig benutzbar.');
            return;
        }
        socket.onopen = onOpen;
        socket.onclose = onClose;
        socket.onerror = onError;
        socket.onmessage = onMessage;
    };

    client.registerAction = function (action, callback) {
        if (actions[action] == undefined) {
            actions[action] = [];
        }
        actions[action].push(callback);
    };

    client.unregisterAction = function (action, callback) {
        var index = actions[action].indexOf(callback);
        actions[action].splice(index, 1);
        if (actions[action].length == 0) {
            actions[action] = undefined;
        }
    };

    function onOpen() {

    }

    function onClose() {

    }

    function onError(error) {

    }

    function onMessage(event) {
        var message = JSON.parse(event.data);
        console.log(message);
        var action = message.requestAction;
        var data = message.response;
        var callbacks = actions[action];
        if (callbacks != undefined && $.isArray(callbacks)) {
            for (var i = 0; i < callbacks.length; i++) {
                callbacks[i].call(this, data);
            }
        }
    }
})(mindcloud.client);