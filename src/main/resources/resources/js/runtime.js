var runtime = {};
(function (runtime) {
    runtime.start = function () {
        mindcloud.init();
        $.each(mindcloud.modules, function (index, module) {
            if ($.isFunction(module.init)) {
                module.init();
            }
        });
        mindcloud.run();
        $.each(mindcloud.modules, function (index, module) {
            if ($.isFunction(module.run)) {
                module.run();
            }
        });
    };
})(runtime);
runtime.start();