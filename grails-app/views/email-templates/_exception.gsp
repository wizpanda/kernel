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
            Internal server error occurred.
            <g:render template="/email-templates/developers-common" plugin="kernel" />
        </div>
        <br>
        <g:each in="${exceptions }" var="exception">
            <kernel:renderException exception="${exception }" noCodeSnippet="${noCodeSnippet }" />
        </g:each>
        <br>
        From,<br>
        ${appName } Server
    </body>
    </html>
</g:applyLayout>