<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
    <xsl:output method="html" indent="yes" />
    <xsl:template match="/">
        <html lang="en">
            <head>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <meta name="description" content="Login page for MindCloud application" />
                <meta name="author" content="Samuel Philipp" />

                <title>MindCloud - Login</title>
                <link rel="icon" type="image/x-icon" href="/img/favicon.ico" />

                <link href="/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
                <link href="/css/bootstrap-theme.min.css" rel="stylesheet" type="text/css" />
                <link href="/css/login.css" rel="stylesheet" type="text/css" />
                <script src="/js/jquery-2.1.4.min.js"></script>
                <script src="/js/bootstrap.min.js"></script>
            </head>

            <body>
                <div class="container">
                    <form class="form-signin" action="/login" method="post">
                        <xsl:call-template name="error"/>
                        <h3 class="form-signin-heading">Bei MindCloud anmelden</h3>
                        <input type="text" name="username" class="form-control" placeholder="Benutzername" required="required" autofocus="autofocus" />
                        <input type="password" name="password" class="form-control" placeholder="Passwort" required="required" />
                        <button class="btn btn-lg btn-primary btn-block" type="submit">Anmelden <span class="glyphicon glyphicon-log-in"></span>
                        </button>

                        <a href="/register" class="btn btn-lg btn-success btn-block">Registrieren</a>
                    </form>
                </div>
            </body>
        </html>
    </xsl:template>
    <xsl:template name="error">
        <xsl:if test="/mindcloud/error">
            <div class="alert alert-danger" role="alert">
                <xsl:value-of select="/mindcloud/error"/>
            </div>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>