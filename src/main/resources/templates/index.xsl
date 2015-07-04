<?xml version="1.0" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" indent="yes" />
    <xsl:template match="/">
        <html lang="en">

        <head>
            <title>MindCloud</title>
            <link rel="icon" type="image/x-icon" href="/img/favicon.ico" />

            <link href="/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
            <link href="/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
            <link href="/css/bootstrap-colorpicker.min.css" rel="stylesheet" type="text/css" />
            <link href="/css/sidebar.css" rel="stylesheet" type="text/css" />
            <link href="/css/style.css" rel="stylesheet" type="text/css" />
            <script src="/js/sockjs-0.3.4.min.js"></script>
            <script src="/js/stomp.min.js"></script>
            <script src="/js/jquery-2.1.4.min.js"></script>
            <script src="/js/jquery.hotkeys.js"></script>
            <script src="/js/bootstrap.min.js"></script>
            <script src="/js/bootstrap-colorpicker.min.js"></script>
            <script src="/js/cytoscape.min.js"></script>
            <script src="/js/cytoscape-cxtmenu.js"></script>
            <script src="/js/engines/arbor.js"></script>
            <script src="/js/engines/cola.v3.min.js"></script>
            <script src="/js/engines/dagre.js"></script>
            <script src="/js/engines/foograph.js"></script>
            <script src="/js/engines/rhill-voronoi-core.js"></script>
            <script src="/js/engines/springy.js"></script>
        </head>

        <body>
            <nav class="navbar navbar-inverse navbar-fixed-top">
                <div class="container-fluid">
                    <div class="navbar-header">
                        <a class="navbar-brand" href="/">
                            <img alt="MindCloud" src="img/brand.png" />
                            <span>MindCloud</span>
                        </a>
                    </div>

                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul class="nav navbar-nav navbar-right">
                            <li class="dropdown" id="profile_dropdown">
                                <a id="menu-user" href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                    <img class="profile-thumbnail" src="/profileimage" />
                                    <span><xsl:value-of select="/mindcloud/session/user/displayedName"/></span> <span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <div id="profil_container" class="container-fluid">
                                            <div class="row profile">
                                                <div class="col-md-3">
                                                    <div class="profile-sidebar">
                                                        <div class="profile-userpic">
                                                            <img src="/profileimage" class="img-responsive" alt="" />
                                                        </div>
                                                        <div class="profile-usertitle">
                                                            <div class="profile-usertitle-name">
                                                                <xsl:value-of select="/mindcloud/session/user/displayedName" />
                                                            </div>
                                                            <div class="profile-usertitle-job">Mindmapper</div>
                                                        </div>
                                                        <div class="profile-usermenu">
                                                            <ul class="nav" id="profile_menu">
                                                                <li class="active">
                                                                    <a href="#">
                                                                        <span class="glyphicon glyphicon-home"></span> Übersicht
                                                                    </a>
                                                                </li>
                                                                <li>
                                                                    <a href="#">
                                                                        <span class="glyphicon glyphicon-user"></span> Profil
                                                                    </a>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-9">
                                                    <div class="profile-content">
                                                        <div>
                                                            <table class="table">
                                                                <tr>
                                                                    <th>Mindmaps:</th>
                                                                    <td id="home-mindmaps-count">Unbekannt</td>
                                                                </tr>
                                                                <tr>
                                                                    <th>Mindmap Knoten:</th>
                                                                    <td id="home-nodes-count">Unbekannt</td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                        <div style="display:none">
                                                            <form id="update-user-profile" action="/updateProfile" method="post" enctype="multipart/form-data">
                                                                <div class="form-group">
                                                                    <label>Angezeigter Name</label>
                                                                    <input type="text" name="displayedname" placeholder="Angezeigten Namen ändern..." class="form-control">
                                                                    <xsl:attribute name="value">
                                                                        <xsl:value-of select="/mindcloud/session/user/displayedName" />
                                                                    </xsl:attribute>
                                                                    </input>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label>Benutzername</label>
                                                                    <input type="text" name="username" placeholder="Benutzername ändern..." class="form-control">
                                                                    <xsl:attribute name="value">
                                                                        <xsl:value-of select="/mindcloud/session/user/name" />
                                                                    </xsl:attribute>
                                                                    </input>
                                                                </div>
                                                                <div class="form-group">
                                                                    <label>Passwort</label>
                                                                    <input type="password" name="password" placeholder="Passwort ändern..." class="form-control" />
                                                                </div>
                                                                <div class="form-group">
                                                                    <label>Passwort wiederholen</label>
                                                                    <input type="password" name="password2" placeholder="Passwort ändern..." class="form-control" />
                                                                </div>
                                                                <div class="form-group">
                                                                    <a class="btn btn-default form-control">Bild ändern</a>
                                                                    <input class="hidden" type="file" name="image" accept="image/png, image/jpeg, image/gif" />
                                                                </div>
                                                                <div class="checkbox pull-left">
                                                                    <label>
                                                                        <input type="checkbox" name="delete-image" /> Bild löschen
                                                                    </label>
                                                                </div>
                                                                <button type="submit" class="btn btn-primary pull-right">Speichern
                                                                </button>
                                                                <div class="clearfix" />
                                                            </form>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="/logout">
                                    <span class="glyphicon glyphicon-off"></span>
                                </a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>

            <div class="nav-side-menu">
                <div class="menu-search">
                    <div class="form-group has-feedback">
                        <input type="text" class="form-control" id="searchInput" placeholder="Suchen..." />
                        <span class="glyphicon glyphicon-search form-control-feedback" aria-hidden="true"></span>
                    </div>
                </div>

                <div class="menu-list">
                    <ul class="menu-content">
                        <li id="search-results-header" data-toggle="collapse" data-target="#search-results" aria-expanded="true" class="hidden top">
                            <a href="#">
                                <span class="glyphicon glyphicon-search"></span> Suchergebnisse
                                <span class="caret"></span>
                            </a>
                        </li>
                        <ul class="sub-menu collapse in hidden" id="search-results">
                        </ul>
                        <li data-toggle="collapse" data-target="#my-mindmaps" aria-expanded="true" class="top">
                            <a href="#">
                                <span class="glyphicon glyphicon-th-list"></span> Meine Mindmaps
                                <span class="caret"></span>
                            </a>
                        </li>
                        <ul class="sub-menu collapse in" id="my-mindmaps"></ul>
                        <li class="collapsed" data-toggle="collapse" data-target="#shared-mindmaps">
                            <a href="#">
                                <span class="glyphicon glyphicon-share"></span> Freigegebene Mindmaps
                                <span class="caret"></span>
                            </a>
                        </li>
                        <ul class="sub-menu collapse collapsed" id="shared-mindmaps"></ul>
                        <li id="create-mindmap" class="top">
                            <a href="#">
                                <span class="glyphicon glyphicon-plus-sign"></span> Neue Mindmap
                            </a>
                        </li>
                        <li id="import-mindmap">
                            <a href="#">
                                <span class="glyphicon glyphicon-cloud-upload"></span> Mindmap importieren
                            </a>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="mindmap-editor">
                <nav class="navbar navbar-default">
                    <ul class="nav navbar-nav">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Mindmap
                                    <span class="caret"></span>
                                </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a id="editor-share" href="#">
                                        <span class="glyphicon glyphicon-share"></span> Freigeben
                                    </a>
                                </li>
                                <li role="separator" class="divider"></li>
                                <li>
                                    <a id="editor-rename" href="#">
                                        <span class="glyphicon glyphicon-pencil"></span> Umbenennen
                                    </a>
                                </li>
                                <li>
                                    <a id="editor-save" href="#">
                                        <span class="glyphicon glyphicon-floppy-disk"></span> Speichern
                                        <span class="text-muted">Strg + S</span>
                                    </a>
                                </li>
                                <li>
                                    <a id="editor-delete" href="#">
                                        <span class="glyphicon glyphicon-trash"></span> Löschen
                                    </a>
                                </li>
                                <li role="separator" class="divider"></li>
                                <li>
                                    <a id="editor-export" href="#">
                                        <span class="glyphicon glyphicon-cloud-download"></span> Exportieren
                                    </a>
                                </li>
                                <li>
                                    <a id="editor-export-image" href="#">
                                        <span class="glyphicon glyphicon-picture"></span> Als Bild exportieren
                                    </a>
                                </li>
                            </ul>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Bearbeiten
                                    <span class="caret"></span>
                                </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a id="editor-step-backwards" href="#">
                                        <span class="glyphicon glyphicon-circle-arrow-left"></span> Rückgängig
                                        <span class="text-muted">Strg + Z</span>
                                    </a>
                                </li>
                                <li>
                                    <a id="editor-step-forward" href="#">
                                        <span class="glyphicon glyphicon-circle-arrow-right"></span> Wiederholen
                                        <span class="text-muted">Strg + Y</span>
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </ul>
                    <a href="#" class="mindmap-name navbar-text navbar-right"></a>
                </nav>
                <div class="mindmap">
                </div>
                <div class="empty-message">Bitte Mindmap zum Anzeigen links auswählen!</div>
            </div>

            <input class="hidden" type="file" name="file" id="import-mindmap-file" />

            <xsl:call-template name="message" />

            <script src="/js/mindcloud.js"></script>
            <script src="/js/notify.js"></script>
            <script src="/js/client.js"></script>
            <script src="/js/ui.js"></script>
            <script src="/js/mindmap.cache.js"></script>

            <script src="/js/modules.menu.js"></script>
            <script src="/js/modules.mindmap.js"></script>
            <script src="/js/modules.editor.js"></script>

            <script src="/js/runtime.js"></script>

        </body>

        </html>
    </xsl:template>
    <xsl:template name="message">
        <xsl:if test="/mindcloud/message">
            <div id="init-message" class="hidden">
                <div class="text">
                    <xsl:value-of select="/mindcloud/message/text" />
                </div>
                <div class="type">
                    <xsl:value-of select="/mindcloud/message/type" />
                </div>
            </div>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>