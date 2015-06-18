mindcloud.modules.menu = {};
(function (menu) {
    menu.init = function () {
        $('[data-toggle="dropdown"]').hover(function (event) {
            if (!$(this).parent().hasClass('open')) {
                console.log('toggle');
                $(this).dropdown('toggle');
            }
        });
        $('.nav-side-menu li').click(function (event) {
            $('.nav-side-menu li').removeClass('active');
            $(this).addClass('active');
        });
    };
})(mindcloud.modules.menu);