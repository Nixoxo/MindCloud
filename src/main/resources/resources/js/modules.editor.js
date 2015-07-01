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
            var mindmap = mindcloud.cache.getMindmapClone();
            mindcloud.client.invokeAction('saveMindmap', mindmap);
        });
        $('.mindmap-name').click(function (event) {
            event.preventDefault();
            renameMindmap();
        });
        registerMenuAction('editor-rename', function () {
            renameMindmap();
        });
        registerMenuAction('editor-delete', function () {
            deleteMindmap();
        });
        registerMenuAction('editor-export', function () {
            exportMindmapAsJson();
        });
        registerMenuAction('editor-export-image', function () {
            exportMindmapAsImage();
        });
        editor.setMindmap();
        mindcloud.client.registerAction('setMindmap', editor.setMindmap);
        mindcloud.client.registerAction('deleteMindmapSuccess', function () {
            editor.setMindmap(undefined);
        });
    };

    function registerMenuAction(id, callback) {
        var item = $('#' + id);
        item.click(function (event) {
            event.preventDefault();
            if (isMenuActionEnabled(id)) {
                callback.call(this);
            }
        });
        var shortcut = item.find('.text-muted').html();
        if (shortcut != undefined) {
            shortcut = shortcut.toLowerCase().replace(/ /g, '').replace(/strg/g, 'ctrl');
            $(document).bind('keydown', shortcut, function (event) {
                event.preventDefault();
                event.stopPropagation();
                if (editor.isEnabled() && isMenuActionEnabled(id)) {
                    callback.call(this);
                }
            });
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

    editor.importMindmap = function (mindmap) {
        mindmap.id = undefined;
        if (validateMindmap(mindmap)) {
            editor.setMindmap(mindmap);
            return true;
        }
        return false;
    };

    function validateMindmap(mindmap) {
        console.log(mindmap);
        if (mindmap.id != undefined) {
            return false;
        }
        if (mindmap.name == undefined || typeof mindmap.name != 'string' || mindmap.name.length == 0) {
            return false;
        }
        if (mindmap.nodes.length == 0) {
            return false;
        }
        for (var i = 0; i < mindmap.nodes.length; i++) {
            var node = mindmap.nodes[i];
            if (node.data.id == undefined || typeof node.data.id != 'string' || node.data.id.length == 0) {
                return false;
            }
            if (node.data.title == undefined || typeof node.data.title != 'string' || node.data.title.length == 0) {
                return false;
            }
            if (node.data.level == undefined || node.data.level != parseInt(node.data.level)) {
                return false;
            }
        }
        for (var i = 0; i < mindmap.edges.length; i++) {
            var edge = mindmap.edges[i];
            if (edge.data.source == undefined || typeof edge.data.source != 'string' || edge.data.source.length == 0) {
                return false;
            }
            if (edge.data.target == undefined || typeof edge.data.target != 'string' || edge.data.target.length == 0) {
                return false;
            }
        }
        return true;
    }

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

    function renameMindmap() {
        mindcloud.ui.showInputDialog('Mindmap umbenennen', 'Name eingeben...', mindcloud.cache.getMindmap().name, function (event) {
            if (event.action == 'ok') {
                mindcloud.cache.setMindmapName(event.input);
                editor.refreshMindmap();
            }
        });
    }

    function exportMindmapAsJson() {
        var mindmap = mindcloud.cache.getMindmap();
        var content = 'data:text/json,' + JSON.stringify(mindmap);
        var name = mindmap.name + '.mc';
        saveFile(name, content);
    }

    function exportMindmapAsImage() {
        mindcloud.ui.showImageExportDialog('Mindmap als Bild exportieren', function (event) {
            if (event.action == 'ok') {
                var image = mindcloud.modules.mindmap.getImage(event.options);
                var name = mindcloud.cache.getMindmap().name + '.png';
                if (event.options.format == 'jpg') {
                    name = name.replace(/png/g, 'jpg');
                }
                saveFile(name, image);
            }
        });
    }

    function saveFile(name, content) {
        var link = $('<a>').attr({
            'href': content,
            'download': name
        }).css({
            'display': 'node'
        }).appendTo('body');
        link.get(0).click();
        link.remove();
    }

    function deleteMindmap() {
        mindcloud.ui.showConfirmDialog('Mindmap löschen', 'Sind Sie sicher, dass Sie diese Mindmap löschen wollen?', function (event) {
            if (event.action == 'yes') {
                mindcloud.client.invokeAction('deleteMindmap', {
                    id: mindcloud.cache.getMindmap().id
                });
            }
        });
    }
})(mindcloud.modules.editor);