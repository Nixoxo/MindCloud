mindcloud.client = {};
(function (client) {
    client.config = {
        host: window.location.hostname,
        port: window.location.port,
        path: 'mindcloud'
    };

    var socket;
    var socketInitEvents = [];
    var actions = {};

    client.run = function () {
        var url = client.config.host + ':' + client.config.port + '/' + client.config.path;
        socket = new WebSocket('ws://' + url);
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
        for (var i = 0; i < socketInitEvents.length; i++) {
            socket.send(JSON.stringify(socketInitEvents[i]));
        }
    }

    function onClose() {

    }

    function onError(error) {

    }

    function onMessage(event) {
        var message = JSON.parse(event);
        console.log(message);
        var action = message.action;
        var data = message.response;
        var callbacks = actions[action];
        if (callbacks != undefined && $.isArray(callbacks)) {
            for (var i = 0; i < callbacks.length; i++) {
                callbacks[i].call(this, data);
            }
        }
    }

    client.invokeAction = function (action, data) {
        var event = {
            action: action,
            data: data
        };
        console.log(event);
        if (socket.readyState == WebSocket.OPEN) {
            socket.send(JSON.stringify(event));
        } else {
            socketInitEvents.push(event);
        }
        // simulate result
        if (action == 'getMindmapList') {
            var response = [
                {
                    id: 1,
                    name: 'Mindmap 1'
                }, {
                    id: 2,
                    name: 'Mindmap 2'
                }, {
                    id: 3,
                    name: 'Mindmap 3'
                }, {
                    id: 4,
                    name: 'Mindmap 4'
                }, {
                    id: 5,
                    name: 'Mindmap 5'
                }
            ];
        } else if (action == 'getMindmap' || action == 'saveMindmap') {
            var response = {
                name: 'Test Mindmap',
                nodes: [
                    {
                        data: {
                            id: '1',
                            title: 'Wissenschaftliches Schreiben',
                            level: 0
                        }
                    },
                    {
                        data: {
                            id: '2',
                            title: 'Text Aufbau',
                            level: 1
                        }
                    },
                    {
                        data: {
                            id: '3',
                            title: 'Satzkonstruktion / Wortwahl',
                            level: 2
                        }
                    },
                    {
                        data: {
                            id: '4',
                            title: 'Literatur',
                            level: 1
                        }
                    },
                    {
                        data: {
                            id: '5',
                            title: 'Quellenangabe/ Literaturverzeichnis',
                            level: 2
                        }
                    },
                    {
                        data: {
                            id: '6',
                            title: 'Zitation',
                            level: 1
                        }
                    },
                    {
                        data: {
                            id: '7',
                            title: 'direkt/ wörtlich',
                            level: 2
                        }
                    },
                    {
                        data: {
                            id: '8',
                            title: 'indirekt / sinngemäß',
                            level: 2
                        }
                    }
                ],
                edges: [
                    {
                        data: {
                            source: '1',
                            target: '2'
                        }
                    },
                    {
                        data: {
                            source: '1',
                            target: '4'
                        }
                    },
                    {
                        data: {
                            source: '1',
                            target: '6'
                        }
                    },
                    {
                        data: {
                            source: '2',
                            target: '3'
                        }
                    },
                    {
                        data: {
                            source: '4',
                            target: '5'
                        }
                    },
                    {
                        data: {
                            source: '6',
                            target: '7'
                        }
                    },
                    {
                        data: {
                            source: '6',
                            target: '8'
                        }
                    },
                ]
            };
        }
        var result = {
            action: action,
            response: response
        };
        onMessage(JSON.stringify(result));
    };
})(mindcloud.client);