var mindcloud = {};
(function (core) {
    core.modules = {};

    core.init = function () {
        core.notify.init();
        core.ui.init();
    };

    core.run = function () {
        core.notify.run();
        core.client.run();
    };

    core.started = function () {
        core.client.invokeAction('getMindmapList');
        core.client.invokeAction('getUserMindmapData');
    };

    core.generateUUID = function () {
        var d = new Date().getTime();
        var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
        });
        return uuid;
    };
})(mindcloud);