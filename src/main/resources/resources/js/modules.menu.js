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

        $('#profile_menu a').click(function (event) {
            $('.profile-content').children().hide();
            index = $('#profile_menu a').index(this);
            $('.profile-content').children().eq(index).show();
            $('#profile_menu').children().removeClass('active');
            $('#profile_menu').children().eq(index).addClass('active');
        });
        /*
                $('.nav-side-menu li').click(function (event) {
                    $('.nav-side-menu li').removeClass('active');
                    $(this).addClass('active');
                });
        */
    };

    menu.run = function () {
        $('#create-mindmap').click(function (event) {
            mindcloud.modules.editor.createMindmap();
        });
        $('#profil_container').hide();
        $('#profile_dropdown').mouseenter(function (event) {
            if ($('#profil_container').is(":visible"))
                $('#profil_container').hide();
            else
                $('#profil_container').show();
        });
        $('#searchInput').keyup(function (event) {
            if (event.keyCode == 13 || event.keyCode == 27) {
                $(this).val('');
            }
            var input = $(this).val();
            if (input.length > 0) {
                mindcloud.client.invokeAction('searchMindmapList', {
                    'search': input
                });
            } else {
                $('#search-results-header').addClass('hidden');
                $('#search-results').addClass('hidden');
            }
        });
        mindcloud.client.registerAction('setMindmapList', menu.setMindmapList);
        mindcloud.client.registerAction('searchMindmapList', menu.setSearchList)
    };

    menu.setMindmapList = function (list) {
        var listPanel = $('#my-mindmaps');
        listPanel.empty();
        $.each(list, function (index, item) {
            $('<li>').attr({
                'id': item.id
            }).click(function (event) {
                mindcloud.client.invokeAction('getMindmap', {
                    id: item.id
                });
            }).html(item.name).appendTo(listPanel);
        });
    };

    menu.setSearchList = function (list) {
        $('#search-results-header').removeClass('hidden');
        var listPanel = $('#search-results');
        listPanel.removeClass('hidden');
        listPanel.empty();
        $.each(list, function (index, item) {
            $('<li>').attr({
                'id': item.id
            }).click(function (event) {
                mindcloud.client.invokeAction('getMindmap', {
                    id: item.id
                });
            }).html(item.name).appendTo(listPanel);
        })
    }

})(mindcloud.modules.menu);