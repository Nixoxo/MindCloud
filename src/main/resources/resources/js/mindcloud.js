var mindcloud = {};
(function (core) {
    core.modules = {};

    core.init = function () {
        core.notify.init();
    };

    core.run = function () {
        core.notify.run();
        core.client.run();
        core.notify.error('Haha es funktioniert...');
    };
})(mindcloud);