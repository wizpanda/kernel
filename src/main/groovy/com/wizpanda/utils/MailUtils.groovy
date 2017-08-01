package com.wizpanda.utils

import grails.gsp.PageRenderer
import grails.transaction.Transactional
import grails.util.Environment
import grails.util.Holders
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.grails.datastore.gorm.GormEnhancer
import org.grails.datastore.gorm.GormEntity

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Shashank Agrawal
 * @since 0.0.1
 */
class MailUtils {

    private static Log log = LogFactory.getLog(this)

    static private PageRenderer getGroovyPageRenderer() {
        return KernelUtils.getBean("groovyPageRenderer")
    }

    // def should be AsynchronousMailService
    static private def getMailService() {
        return KernelUtils.getBean("asynchronousMailService")
    }

    static String getDevelopersEmail() {
        Holders.getFlatConfig()["app.developers.email"]
    }

    // def should be AsynchronousMailMessage
    static def sendMail(String email, String subject, Map<String, Object> template, Map args) {
        return sendMail([email], subject, template, args)
    }

    static def sendMail(List<String> emails, String emailSubject, Map templateData, Map args) {
        // Use any of the available domain class to create a new transaction
        Holders.getGrailsApplication().getDomainClasses()[0].clazz.withNewTransaction {
            _sendMail(emails, emailSubject, templateData, args)
        }
    }

    /**
     * A helper method to send email via Grails asynchronous mail plugin. This plugin provide simpler wrapper to send
     * a Grails view as the body. The Grails mail plugin also provide the option to send a Grails template as email
     * body but that has a limitation of the current thread to be inside a request scope that means that option can
     * only be used when a user is interacting.
     *
     * This method overcomes that by using Groovy page renderer.
     *
     * @param emails List of email addresses to send email to (to need to call ".toArray")
     * @param emailSubject Subject of the email
     * @param templateData The template options as described in http://docs.grails.org/3.2.8/ref/Tags/render.html
     * @param args Additional data to pass to "sendMail" closure
     *
     * @return Instance of AsynchronousMailMessage domain recently created
     */
    // def should be AsynchronousMailMessage
    static def _sendMail(List<String> emails, String emailSubject, Map templateData, Map args) {
        log.debug "Sending email to $emails subject [$emailSubject] args $args"

        args = args ?: [:]
        String htmlContent = args.html, eventCode
        String appName = KernelUtils.getAppName()

        if (args.developerEmail) {
            eventCode = RandomUtils.randomUniqueCode(10)
            log.debug "Email event code: $eventCode"

            emailSubject = "[$appName][${Environment.current.name}] $emailSubject "
        }

        if (templateData) {
            templateData.model = templateData.model ?: [:]
            templateData.model.appName = appName

            if (args.developerEmail) {
                templateData.model.eventCode = eventCode
                setRequestRelatedModel(templateData.model.request ?: RequestUtils.currentRequest, templateData.model)
            }

            htmlContent = getGroovyPageRenderer().render(templateData)
        }

        def messageInstance = getMailService().sendMail {
            if (args.attachments) {
                multipart(true)
            }
            if (args.from) {
                from(args.from)
            }
            if (args.scheduledOn) {
                beginDate(args.scheduledOn)
            }

            to(emails.toArray())
            subject(emailSubject)
            html(htmlContent)

            if (args.cc) {
                if (args.cc instanceof String) {
                    cc(args.cc)
                } else if (args.cc instanceof List) {
                    cc(args.cc.toArray())
                }
            }

            if (args.bcc) {
                if (args.bcc instanceof String) {
                    bcc(args.bcc)
                } else if (args.bcc instanceof List) {
                    bcc(args.bcc.toArray())
                }
            }

            if (args.attachments) {
                args.attachments.each { File fileInstance ->
                    attach(fileInstance)
                }
            }

            // Checking for null value to allow "false"
            if (args.immediate != null) {
                immediate(args.immediate)
            }
        }

        log.debug "Email scheduled $messageInstance"
        return messageInstance
    }

    static void sendDevelopersEmail(String subject, Map<String, Object> model, Map args = null) {
        args = args ?: [:]
        args.immediate = true
        args.developerEmail = true

        Map templateData = [
                template: "/email-templates/developers",
                model: model,
                plugin: "kernel"
        ]

        sendMail(developersEmail, subject, templateData, args)
    }

    static void sendValidationFailedEmail(GormEntity gormInstance, Map templateModel) {
        sendValidationFailedEmail([gormInstance], templateModel)
    }

    /**
     *
     * @param gormInstances
     * @param model
     * @since 1.0.5
     */
    static void sendValidationFailedEmail(List<GormEntity> gormInstances, Map model) {
        log.debug "Sending exception email"

        model = model ?: [:]
        model.gormInstances = gormInstances
        
        Map templateModel = [
                template: "/email-templates/validation-failed",
                model: model,
                plugin: "kernel"
        ]

        sendMail(developersEmail, "Domain validation failed", templateModel, [immediate: true, developerEmail: true])
    }

    static private void setRequestRelatedModel(HttpServletRequest request, Map model) {
        if (!model || !request) {
            return
        }

        String requestURL = request.forwardURI
        if (request.queryString) {
            requestURL += "?" + request.queryString
        }

        model.requestURL = requestURL
        model.currentRequest = request
        model.headers = RequestUtils.getHeaders(request)
        model.frontendURL = request.getHeader("angular-url") ?: request.getHeader("frontend-url")
    }

    /**
     * Method used to send email on exception to developers with detailed stacktrace.
     *
     * @param exceptions A list of exceptions
     * @param model OPTIONAL A map containing all parameters to send email.
     * @param model.requestURL OPTIONAL Grails server URL where exception occurred
     * @param model.angularURL OPTIONAL Client side Angular app URL
     * @param model.codeExecutionAt OPTIONAL Any human readable extra information where exception occurred
     *
     * @since 1.0.5
     */
    static private void _sendExceptionEmail(List<Throwable> exceptions, Map model) {
        log.debug "Sending exception email"

        model = model ?: [:]
        model.exceptions = exceptions

        Map templateModel = [
                template: "/email-templates/exception",
                model: model,
                plugin: "kernel"
        ]

        String developersEmail = Holders.getFlatConfig()["app.developers.email"]
        sendMail(developersEmail, "Internal Server Error", templateModel, [immediate: true, developerEmail: true])
    }

    static void sendExceptionEmail(List<Throwable> exceptions, Map model) {
        try {
            _sendExceptionEmail(exceptions, model)
        } catch (Exception e) {
            // Do not repeatedly call this method to avoid StackOverflow.
            log.error "Couldn't send email.", e
        }
    }

    static void sendExceptionEmail(Throwable exception, Map model) {
        try {
            _sendExceptionEmail([exception], model)
        } catch (Exception e) {
            // Do not repeatedly call this method to avoid StackOverflow.
            log.error "Couldn't send email.", e
        }
    }
}
