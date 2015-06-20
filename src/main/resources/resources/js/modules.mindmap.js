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

    mindmap.init = function () {

        container = $('.mindmap');
        cy = cytoscape({
            container: container.get(0),
            ready: function () {
                cy.menu = cy.cxtmenu({
                    selector: 'node',
                    commands: [
                        {
                            content: '<span class="glyphicon glyphicon-edit"/>',
                            select: function () {
                                var node = this.json().data;
                            }
                        },
                        {
                            content: '<span class="glyphicon glyphicon-plus"/>',
                            select: function () {
                                var node = this.json().data;
                            }
                        },
                        {
                            content: '<span class="glyphicon glyphicon-trash"/>',
                            select: function () {
                                var node = this.json().data;
                            }
                        }
                    ],
                    menuRadius: 70,
                    indicatorSize: 0,
                    activePadding: 0
                });
            },
            userPanningEnabled: false,
            autolock: true,
            style: mindmapStyle,
            layout: {
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
            },
            elements: {
                nodes: [
                    {
                        data: {
                            id: '1',
                            title: 'Wissenschaftliches Schreiben',
                            range: 1
                        }
                    },
                    {
                        data: {
                            id: '2',
                            title: 'Text Aufbau',
                            range: 2
                        }
                    },
                    {
                        data: {
                            id: '3',
                            title: 'Satzkonstruktion / Wortwahl',
                            range: 3
                        }
                    },
                    {
                        data: {
                            id: '4',
                            title: 'Literatur',
                            range: 2
                        }
                    },
                    {
                        data: {
                            id: '5',
                            title: 'Quellenangabe/ Literaturverzeichnis',
                            range: 3
                        }
                    },
                    {
                        data: {
                            id: '6',
                            title: 'Zitation',
                            range: 2
                        }
                    },
                    {
                        data: {
                            id: '7',
                            title: 'direkt/ wörtlich',
                            range: 3
                        }
                    },
                    {
                        data: {
                            id: '8',
                            title: 'indirekt / sinngemäß',
                            range: 3
                        }
                    }
                ],
                edges: [
                    {
                        data: {
                            source: '1',
                            target: '2'
                        }
                    },
                    {
                        data: {
                            source: '1',
                            target: '4'
                        }
                    },
                    {
                        data: {
                            source: '1',
                            target: '6'
                        }
                    },
                    {
                        data: {
                            source: '2',
                            target: '3'
                        }
                    },
                    {
                        data: {
                            source: '4',
                            target: '5'
                        }
                    },
                    {
                        data: {
                            source: '6',
                            target: '7'
                        }
                    },
                    {
                        data: {
                            source: '6',
                            target: '8'
                        }
                    },
                ]
            }

        });
    };
})(mindcloud.modules.mindmap);