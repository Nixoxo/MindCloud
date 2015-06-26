mindcloud.client = {};
(function (client) {
    client.config = {
        host: window.location.hostname,
        port: window.location.port,
        path: 'mindcloud'
    };

    var socket;
    var socketInitEvents = [];
    var socketInitActions = [];

    client.run = function () {
        var websocket = new SockJS('/' + client.config.path);
        socket = Stomp.over(websocket);
        if (socket == undefined) {
            necon.notify.warn('WebSockets werden in Ihrem Browser nicht unterstützt. Deshalb ist die Anwendung nicht vollständig benutzbar.');
            return;
        }
        socket.connect({}, onOpen);
    };

    client.registerAction = function (action, callback) {
        if (socket != undefined && socket.ws.readyState == SockJS.OPEN) {
            socket.subscribe('/' + action, function (message) {
                if (message.body.length > 0) {
                    callback.call(this, JSON.parse(message.body));
                }
            });
        } else {
            socketInitActions.push({
                action: action,
                callback: callback
            });
        }
    };

    function onOpen(frame) {
        console.log('Connected: ' + frame);
        for (var i = 0; i < socketInitActions.length; i++) {
            var action = socketInitActions[i];
            client.registerAction(action.action, action.callback);
        }
        for (var i = 0; i < socketInitEvents.length; i++) {
            var event = socketInitEvents[i];
            client.invokeAction(event.action, event.data);
        }
    }

    function onClose() {

    }

    function onError(error) {

    }

    client.invokeAction = function (action, data) {
        console.log(action);
        if (socket != undefined && socket.ws.readyState == SockJS.OPEN) {
            socket.send('/' + action, {}, JSON.stringify(data));
        } else {
            socketInitEvents.push({
                action: action,
                data: data
            });
        }
    };
})(mindcloud.client);