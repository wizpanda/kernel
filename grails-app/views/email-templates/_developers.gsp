<g:applyLayout name="emailMain">
    <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    </head>
    <body>
        <div>
            Hello,
        </div>
        <br>
        <div>
            Developer notification generated.
            <g:render template="/email-templates/developers-common" plugin="kernel" />
        </div>
        <br>
        From,<br>
        ${appName } Server
    </body>
    </html>
</g:applyLayout>