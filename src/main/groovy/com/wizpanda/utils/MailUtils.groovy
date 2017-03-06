package com.wizpanda.utils

import grails.gsp.PageRenderer
import grails.util.Environment
import grails.util.Holders
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.grails.datastore.gorm.GormEntity

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
     * @param templateModel The template options as described in http://docs.grails.org/2.5.4/ref/Tags/render.html
     * @param args Additional data to pass to "sendMail" closure
     *
     * @return Instance of AsynchronousMailMessage domain recently created
     */
    // def should be AsynchronousMailMessage
    static def sendMail(List<String> emails, String emailSubject, Map templateModel, Map args) {
        log.debug "Sending email to $emails subject [$emailSubject] args $args"

        args = args ?: [:]
        String htmlContent = args.html

        String appName = KernelUtils.getAppName()
        if (args.appendAppInfo) {
            emailSubject = "[$appName][${Environment.current.name}] $emailSubject"
        }

        if (templateModel) {
            templateModel.model = templateModel.model ?: [:]
            templateModel.model.appName = appName
            htmlContent = getGroovyPageRenderer().render(templateModel)
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

    static void sendDevelopersEmail(String subject, Map<String, Object> template, Map args) {
        args.immediate = true
        args.appendAppInfo = true

        sendMail(developersEmail, subject, template, args)
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
        String appName = KernelUtils.getAppName()

        model = model ?: [:]
        model.gormInstances = gormInstances
        Map templateModel = [
                template: "/email-templates/validation-failed",
                model: model,
                plugin: "kernel"
        ]

        sendMail(developersEmail, "Domain validation failed", templateModel, [immediate: true, appendAppInfo: true])
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
    static void sendExceptionEmail(List<Throwable> exceptions, Map model) {
        log.debug "Sending exception email"

        model = model ?: [:]
        model.exceptions = exceptions
        Map templateModel = [
                template: "/email-templates/exception",
                model: model,
                plugin: "kernel"
        ]

        String developersEmail = Holders.getFlatConfig()["app.developers.email"]
        sendMail(developersEmail, "Internal Server Error", templateModel, [immediate: true, appendAppInfo: true])
    }

    static void sendExceptionEmail(Throwable exception, Map model) {
        sendExceptionEmail([exception], model)
    }
}