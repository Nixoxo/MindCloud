mindcloud.modules.mindmap = {};
(function (mindmap) {
    var container;
    var cy;
    var mindmapStyle = [
        {
            selector: 'node',
            css: {
                'content': 'data(title)',
                'font-size': 9,
                'text-valign': 'bottom',
                'text-outline-color': '#fff',
                'color': '#000',
                'padding': '10px',
                'width': 'mapData(source, 0, 1, 25, 50)',
                'height': 'mapData(source, 0, 1, 25, 50)',
                'background': '#666',
                'background-fit': 'cover'
            }
        }, {
            selector: 'edge',
            css: {
                'width': 2,
                'line-color': '#ccc'
            }
        }
    ];
    var mindmapLayout = {
        name: 'cola',

        animate: false, // whether to show the layout as it's running
        refresh: 1, // number of ticks per frame; higher is faster but more jerky
        maxSimulationTime: 4000, // max length in ms to run the layout
        ungrabifyWhileSimulating: false, // so you can't drag nodes during layout
        fit: true, // on every layout reposition of nodes, fit the viewport
        padding: 30, // padding around the simulation
        boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }

        // layout event callbacks
        ready: function () {}, // on layoutready
        stop: function () {}, // on layoutstop

        // positioning options
        randomize: false, // use random node positions at beginning of layout
        avoidOverlap: true, // if true, prevents overlap of node bounding boxes
        handleDisconnected: true, // if true, avoids disconnected components from overlapping
        nodeSpacing: function (node) {
            return 10;
        }, // extra spacing around nodes
        flow: undefined, // use DAG/tree flow layout if specified, e.g. { axis: 'y', minSeparation: 30 }
        alignment: undefined, // relative alignment constraints on nodes, e.g. function( node ){ return { x: 0, y: 1 } }

        // different methods of specifying edge length
        // each can be a constant numerical value or a function like `function( edge ){ return 2; }`
        edgeLength: undefined, // sets edge length directly in simulation
        edgeSymDiffLength: undefined, // symmetric diff edge length in simulation
        edgeJaccardLength: undefined, // jaccard edge length in simulation

        // iterations of cola algorithm; uses default values on undefined
        unconstrIter: undefined, // unconstrained initial layout iterations
        userConstIter: undefined, // initial layout iterations with user-specified constraints
        allConstIter: undefined, // initial layout iterations with all constraints including non-overlap

        // infinite layout options
        infinite: false // overrides all other options for a forces-all-the-time mode
    };

    mindmap.init = function () {
        container = $('.mindmap');
        mindmap.set();
        var resizeTimeout;
        $(window).resize(function () {
            clearTimeout(resizeTimeout);
            resizeTimeout = setTimeout(function () {
                cy.makeLayout(mindmapLayout).run();
            }, 200);
        });
    };

    mindmap.set = function (mindmap) {
        container.empty();
        if (mindmap == undefined) {
            mindmap = {
                nodes: [],
                edges: []
            }
        }
        cy = cytoscape({
            container: container.get(0),
            ready: function () {
                cy.menu = cy.cxtmenu({
                    selector: 'node',
                    commands: [
                        {
                            content: '<span class="glyphicon glyphicon-edit"/>',
                            select: function () {
                                var node = this.json();
                                mindcloud.modules.editor.editNode(node);
                            }
                        }, {
                            content: '<span class="glyphicon glyphicon-plus"/>',
                            select: function () {
                                var node = this.json();
                                mindcloud.modules.editor.addNode(node);
                            }
                        }, {
                            content: '<span class="glyphicon glyphicon-trash"/>',
                            select: function () {
                                var node = this.json();
                                mindcloud.modules.editor.removeNode(node);
                            }
                        }
                    ],
                    menuRadius: 70,
                    indicatorSize: 0,
                    activePadding: 0
                });
            },
            maxZoom: 2,
            userZoomingEnabled: true,
            userPanningEnabled: true,
            autolock: true,
            style: mindmapStyle,
            layout: mindmapLayout,
            elements: {
                nodes: mindmap.nodes,
                edges: mindmap.edges
            }
        });
    };
})(mindcloud.modules.mindmap);