mindcloud.cache = {};
(function (cache) {
    var timeIndex;
    var history;

    cache.createMindmap = function (name) {
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
        cache.setMindmap(mindmap);
    };

    cache.setMindmap = function (mindmap) {
        timeIndex = 0;
        history = [mindmap];
    };

    cache.getMindmap = function () {
        return history[timeIndex];
    };

    cache.isStepBackwardsAvailable = function () {
        return timeIndex > 0;
    };

    cache.stepBackwards = function () {
        if (cache.isStepBackwardsAvailable()) {
            timeIndex--;
        }
    };

    cache.isStepForwardAvailable = function () {
        return timeIndex < history.length - 1;
    };

    cache.stepForward = function () {
        if (cache.isStepForwardAvailable()) {
            timeIndex++;
        }
    };

    function pushNewVersion(mindmap) {
        timeIndex++;
        if (timeIndex < history.length) {
            history.splice(timeIndex, history.length - timeIndex);
        }
        history.push(mindmap);
    }

    function getCurrentMindmapClone() {
        return JSON.parse(JSON.stringify(history[timeIndex]));
    }

    cache.addNode = function (parent, text) {
        var mindmap = getCurrentMindmapClone();
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
        mindmap.nodes.push(node);
        mindmap.edges.push(edge);
        pushNewVersion(mindmap);
    };

    cache.editNode = function (node, title) {
        var mindmap = getCurrentMindmapClone();
        for (var i = 0; i < mindmap.nodes.length; i++) {
            var n = mindmap.nodes[i];
            if (n.data.id == node.data.id) {
                n.data.title = title;
            }
        }
        pushNewVersion(mindmap);
    };

    cache.removeNode = function (node) {
        var mindmap = getCurrentMindmapClone();
        var idsToRemove = getDeleteIdsForId(mindmap, node.data.id);
        for (var i = 0; i < mindmap.nodes.length; i++) {
            var n = mindmap.nodes[i];
            if (idsToRemove.contains(n.data.id)) {
                mindmap.nodes.splice(i, 1);
                i--;
            }
        }
        for (var i = 0; i < mindmap.edges.length; i++) {
            var e = mindmap.edges[i];
            if (idsToRemove.contains(e.data.target)) {
                mindmap.edges.splice(i, 1);
                i--;
            }
        }
        pushNewVersion(mindmap);
    };

    function getDeleteIdsForId(mindmap, id) {
        var ids = [];
        for (var i = 0; i < mindmap.edges.length; i++) {
            var edge = mindmap.edges[i].data;
            if (edge.source == id) {
                var childIds = getDeleteIdsForId(mindmap, edge.target);
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

    cache.setMindmapName = function (name) {
        var mindmap = getCurrentMindmapClone();
        mindmap.name = name;
        pushNewVersion(mindmap);
    };
})(mindcloud.cache);