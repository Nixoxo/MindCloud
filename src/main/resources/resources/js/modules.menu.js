mindcloud.modules.menu = {};
(function (menu) {
    menu.init = function () {
        $('.navbar-inverse').find('[data-toggle="dropdown"]').hover(function (event) {
            if (!$(this).parent().hasClass('open')) {
                $(this).dropdown('toggle');
            }
        });
        $('.navbar-inverse').find('.navbar-nav').hover(undefined, function (event) {
            $.each($(this).find('[data-toggle="dropdown"]'), function (index, dropdown) {
                if ($(dropdown).parent().hasClass('open')) {
                    $(dropdown).dropdown('toggle');
                }
            });
        });
        $('.nav-side-menu li').click(function (event) {
            $('.nav-side-menu li').removeClass('active');
            $(this).addClass('active');
        });
    };
})(mindcloud.modules.menu);