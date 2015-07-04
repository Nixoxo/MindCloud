mindcloud.modules.menu = {};
(function (menu) {
    menu.init = function () {
        $('#profile_menu a').click(function (event) {
            $('.profile-content').children().hide();
            index = $('#profile_menu a').index(this);
            $('.profile-content').children().eq(index).show();
            $('#profile_menu').children().removeClass('active');
            $('#profile_menu').children().eq(index).addClass('active');
        });
        $('#profil_container').click(function (event) {
            event.preventDefault();
            event.stopPropagation();
        });
        $('.nav-side-menu li').click(function (event) {
            /*
            $('.nav-side-menu li').removeClass('active');
            $(this).addClass('active');
            */
            event.preventDefault();
        });
    };

    menu.run = function () {
        $('#update-user').find('.btn-default').click(function (event) {
            $('#update-user').find('[name="image"]').click();
        });
        $('#update-user').find('[name="image"]').change(function () {
            var file = this.files[0];
            $('#update-user').find('.btn-default').html(file.name);
        });
        $('#create-mindmap').click(function (event) {
            mindcloud.modules.editor.createMindmap();
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
        $('#import-mindmap').click(function (event) {
            $('#import-mindmap-file').click();
        });
        $('#import-mindmap-file').change(function () {
            var file = this.files[0];
            var reader = new FileReader();
            reader.onload = function (progressEvent) {
                try {
                    var mindmap = JSON.parse(this.result);
                    if (!mindcloud.modules.editor.importMindmap(mindmap)) {
                        throw "error";
                    }
                } catch (e) {
                    mindcloud.notify.error('Fehler beim Importieren!');
                }
            };
            reader.readAsText(file);
        });
        mindcloud.client.registerAction('setMindmapList', menu.setMindmapList);
        mindcloud.client.registerAction('setSharedMindmapList', menu.setSharedMindmapList);
        mindcloud.client.registerAction('setSearchResult', menu.setSearchList)
        mindcloud.client.registerAction('setUserMindmapData', setUserMindmapData)
        mindcloud.client.registerAction('setMindmap', function (mindmap) {
            mindcloud.client.invokeAction('getMindmapList');
            mindcloud.client.invokeAction('getUserMindmapData');
        });
        mindcloud.client.registerAction('deleteMindmapSuccess', function (mindmap) {
            mindcloud.client.invokeAction('getMindmapList');
            mindcloud.client.invokeAction('getUserMindmapData');
        });
    };

    function setUserMindmapData(data) {
        $('#home-mindmaps-count').html(data.mindmapsCount);
        $('#home-nodes-count').html(data.nodesCount);
    }

    menu.setMindmapList = function (list) {
        if (list.length == 0) {
            $('li[data-target="#my-mindmaps"]').addClass('empty');
        } else {
            $('li[data-target="#my-mindmaps"]').removeClass('empty');
        }
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

    menu.setSharedMindmapList = function (list) {
        if (list.length == 0) {
            $('li[data-target="#shared-mindmaps"]').addClass('empty');
        } else {
            $('li[data-target="#shared-mindmaps"]').removeClass('empty');
        }
        var listPanel = $('#shared-mindmaps');
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