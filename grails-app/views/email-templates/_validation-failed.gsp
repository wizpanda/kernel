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
            Domain Validation Failed
            <g:if test="${userInstance }">for ${userInstance }</g:if>
            <g:if test="${requestURL }">while processing request at <strong>${requestURL }</strong></g:if>
            <g:if test="${angularURL }">at Angular app URL <strong>${angularURL }</strong></g:if>
            <g:if test="${codeExecutionAt }">while "${codeExecutionAt }"</g:if>
        </div>
        <br>
        <g:each in="${gormInstances }" var="instance">
            <kernel:renderErrors bean="${instance }" />
        </g:each>
        <br>
        From,<br>
        ${appName } Server
    </body>
    </html>
</g:applyLayout>