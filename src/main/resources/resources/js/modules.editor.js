mindcloud.modules.editor = {};
(function (editor) {
    var editorPanel;
    var menuPanel;
    var editingMindmap;

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
        editor.setMindmap();
        mindcloud.client.registerAction('getMindmap', editor.setMindmap)
    };

    editor.createMindmap = function (name) {
        var mindmap = {
            name: name,
            nodes: [
                {
                    data: {
                        id: mindcloud.generateUUID(),
                        title: name,
                        level: 0
                    }
                }
            ],
            edges: []
        };
        editor.setMindmap(mindmap);
    };

    editor.setMindmap = function (mindmap) {
        if (mindmap == undefined) {
            editorPanel.addClass('disabled');
            editorPanel.find('.empty-message').show();
            menuPanel.hide();
        } else {
            editorPanel.removeClass('disabled');
            editorPanel.find('.empty-message').hide();
            menuPanel.show();
            menuPanel.find('.mindmap-name').html(mindmap.name);
            editingMindmap = mindmap;
        }
        mindcloud.modules.mindmap.set(mindmap);
    };

    editor.addNode = function (node) {
        mindcloud.ui.showInputDialog('Node hinzufügen', 'Text eingeben...', undefined, function (event) {
            if (event.action == 'ok') {
                if (event.input.length == 0) {
                    mindcloud.notify.error('Der Nodetext darf nicht leer sein!');
                } else {
                    createNode(node, event.input);
                }
            }
        });
    };

    function createNode(parent, text) {
        var node = {
            data: {
                id: mindcloud.generateUUID(),
                title: text,
                level: parent.level + 1
            }
        };
        var edge = {
            data: {
                source: parent.data.id,
                target: node.data.id
            }
        };
        editingMindmap.nodes.push(node);
        editingMindmap.edges.push(edge);
        editor.setMindmap(editingMindmap);
    }

    editor.removeNode = function (node) {
        if (node.data.level == 0) {
            mindcloud.notify.info('Der Basisknoten kann nicht gelöscht werden.');
            return;
        }
        mindcloud.ui.showConfirmDialog('Knoten löschen?',
            'Sind Sie sicher, dass Sie den Knoten "' + node.data.title + '" und alle Unterknoten löschen wollen?',
            function (event) {
                if (event.action == 'yes') {
                    removeNodeAndChilds(mode);
                }
            });
    };

    function removeNodeAndCHilds(node) {
        var idsToRemove = getDeleteIdsForId(node.data.id);
        for (var i = 0; i < editingMindmap.nodes.length; i++) {
            var n = editingMindmap.nodes[i];
            if (idsToRemove.contains(n.data.id)) {
                editingMindmap.nodes.splice(i, 1);
                i--;
            }
        }
        for (var i = 0; i < editingMindmap.edges.length; i++) {
            var e = editingMindmap.edges[i];
            if (idsToRemove.contains(e.data.target)) {
                editingMindmap.edges.splice(i, 1);
                i--;
            }
        }
        editor.setMindmap(editingMindmap);
    }

    function getDeleteIdsForId(id) {
        var ids = [];
        for (var i = 0; i < editingMindmap.edges.length; i++) {
            var edge = editingMindmap.edges[i].data;
            if (edge.source == id) {
                var childIds = getDeleteIdsForId(edge.target);
                for (var j = 0; j < childIds.length; j++) {
                    if (!ids.contains(childIds[j])) {
                        ids.push(childIds[j]);
                    }
                }
            }
            if (edge.target == id && !ids.contains(edge.target)) {
                ids.push(edge.target);
            }
        }
        return ids;
    }

    editor.editNode = function (node) {
        mindcloud.ui.showInputDialog('Node ändern', 'Text eingeben...', node.data.title, function (event) {
            if (event.action == 'ok') {
                if (event.input.length == 0) {
                    mindcloud.notify.error('Der Nodetext darf nicht leer sein!');
                } else {
                    for (var i = 0; i < editingMindmap.nodes.length; i++) {
                        var n = editingMindmap.nodes[i];
                        if (n.data.id == node.data.id) {
                            n.data.title = event.input;
                        }
                    }
                    editor.setMindmap(editingMindmap);
                }
            }
        });
    };
})(mindcloud.modules.editor);