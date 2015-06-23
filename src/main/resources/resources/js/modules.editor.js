mindcloud.modules.editor = {};
(function (editor) {
    var editorPanel;
    var menuPanel;

    editor.init = function () {
        editorPanel = $('.mindmap-editor');
        menuPanel = editorPanel.find('.navbar');
        menuPanel.find('.navbar-nav > li > [data-toggle="dropdown"]').hover(function (event) {
            var dropdowns = menuPanel.find('.navbar-nav > li > [data-toggle="dropdown"]');
            var open = false;
            for (var i = 0; i < dropdowns.length; i++) {
                var dropdown = $(dropdowns[i]);
                if (dropdown.parent().hasClass('open')) {
                    dropdown.dropdown('toggle');
                    open = true;
                }
            }
            if (open) {
                $(this).dropdown('toggle');
            }
        });
    };

    editor.run = function () {
        // TODO init menu items
        registerMenuAction('editor-step-backwards', function () {
            if (mindcloud.cache.isStepBackwardsAvailable()) {
                mindcloud.cache.stepBackwards();
                editor.refreshMindmap();
                refreshMenuState();
            }
        });
        registerMenuAction('editor-step-forward', function () {
            if (mindcloud.cache.isStepForwardAvailable()) {
                mindcloud.cache.stepForward();
                editor.refreshMindmap();
                refreshMenuState();
            }
        });
        registerMenuAction('editor-save', function () {
            mindcloud.client.invokeAction('saveMindmap', mindcloud.cache.getMindmap());
        });
        editor.setMindmap();
        mindcloud.client.registerAction('getMindmap', editor.setMindmap);
        mindcloud.client.registerAction('saveMindmap', editor.setMindmap);
    };

    function registerMenuAction(id, callback, enabled) {
        var item = $('#' + id);
        item.click(function (event) {
            event.preventDefault();
            if (isMenuActionEnabled(id)) {
                callback.call(this);
            }
        });
        var shortcut = item.find('.text-muted').html();
        shortcut = shortcut.toLowerCase().replace(/ /g, '').replace(/strg/g, 'ctrl');
        $(document).bind('keydown', shortcut, function (event) {
            event.preventDefault();
            event.stopPropagation();
            if (editor.isEnabled() && isMenuActionEnabled(id)) {
                callback.call(this);
            }
        });
        if (enabled != undefined) {
            setMenuActionEnabled(id, enabled);
        }
    }

    function setMenuActionEnabled(id, enabled) {
        if (enabled) {
            $('#' + id).parent().removeClass('disabled');
        } else {
            $('#' + id).parent().addClass('disabled');
        }
    }

    function isMenuActionEnabled(id) {
        return !$('#' + id).parent().hasClass('disabled');
    }

    editor.isEnabled = function () {
        return !editorPanel.hasClass('disabled');
    };

    editor.createMindmap = function () {
        mindcloud.ui.showInputDialog('Mindmap erstellen', 'Name eingeben...', undefined, function (event) {
            if (event.action == 'ok') {
                if (event.input.length == 0) {
                    mindcloud.notify.error('Der Name der Mindmap darf nicht leer sein!');
                } else {
                    mindcloud.cache.createMindmap(event.input);
                    editor.setMindmap(mindcloud.cache.getMindmap());
                }
            }
        });
    };

    editor.setMindmap = function (mindmap) {
        mindcloud.cache.setMindmap(mindmap);
        if (mindmap == undefined) {
            editorPanel.addClass('disabled');
            editorPanel.find('.empty-message').show();
            menuPanel.hide();
        } else {
            editorPanel.removeClass('disabled');
            editorPanel.find('.empty-message').hide();
            menuPanel.show();
            menuPanel.find('.mindmap-name').html(mindmap.name);
            refreshMenuState();
        }
        mindcloud.modules.mindmap.set(mindmap);
    };

    function refreshMenuState() {
        setMenuActionEnabled('editor-step-backwards', mindcloud.cache.isStepBackwardsAvailable());
        setMenuActionEnabled('editor-step-forward', mindcloud.cache.isStepForwardAvailable());
    }

    editor.refreshMindmap = function () {
        var mindmap = mindcloud.cache.getMindmap();
        menuPanel.find('.mindmap-name').html(mindmap.name);
        mindcloud.modules.mindmap.set(mindmap);
        refreshMenuState();
    };

    editor.addNode = function (node) {
        mindcloud.ui.showInputDialog('Node hinzufügen', 'Text eingeben...', undefined, function (event) {
            if (event.action == 'ok') {
                if (event.input.length == 0) {
                    mindcloud.notify.error('Der Nodetext darf nicht leer sein!');
                } else {
                    mindcloud.cache.addNode(node, event.input);
                    editor.refreshMindmap();
                }
            }
        });
    };

    editor.removeNode = function (node) {
        if (node.data.level == 0) {
            mindcloud.notify.info('Der Basisknoten kann nicht gelöscht werden.');
            return;
        }
        mindcloud.ui.showConfirmDialog('Knoten löschen?',
            'Sind Sie sicher, dass Sie den Knoten "' + node.data.title + '" und alle Unterknoten löschen wollen?',
            function (event) {
                if (event.action == 'yes') {
                    mindcloud.cache.removeNode(node);
                    editor.refreshMindmap();
                }
            });
    };

    editor.editNode = function (node) {
        mindcloud.ui.showInputDialog('Node ändern', 'Text eingeben...', node.data.title, function (event) {
            if (event.action == 'ok') {
                if (event.input.length == 0) {
                    mindcloud.notify.error('Der Nodetext darf nicht leer sein!');
                } else {
                    mindcloud.cache.editNode(node, event.input);
                    editor.refreshMindmap();
                }
            }
        });
    };
})(mindcloud.modules.editor);