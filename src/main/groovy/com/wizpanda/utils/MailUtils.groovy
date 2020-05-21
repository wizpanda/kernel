package com.wizpanda.utils

import grails.gsp.PageRenderer
import grails.util.Environment
import grails.util.Holders
import groovy.transform.CompileStatic
import groovy.transform.TypeCheckingMode
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * A wrapper around grails-async-mail plugin to send the email in simpler way.
 *
 * @author Shashank Agrawal
 * @since 0.0.1
 */
@CompileStatic
class MailUtils {

    private static Log log = LogFactory.getLog(this)
    private static PageRenderer groovyPageRenderer

    private MailUtils() {
    }

    static private PageRenderer getGroovyPageRenderer() {
        if (groovyPageRenderer) {
            return groovyPageRenderer
        }

        groovyPageRenderer = KernelUtils.getBean("groovyPageRenderer") as PageRenderer
        return groovyPageRenderer
    }

    // def should be AsynchronousMailService
    static private def getMailService() {
        return KernelUtils.getBean("asynchronousMailService")
    }

    // def should be AsynchronousMailMessage
    static def sendMail(String email, String subject, Map<String, Object> template, Map args) {
        return sendMail([email], subject, template, args)
    }

    @CompileStatic(TypeCheckingMode.SKIP)
    static def sendMail(List<String> emails, String emailSubject, Map templateData, Map args) {
        // Use any of the available domain class to create a new transaction
        Holders.getGrailsApplication().getDomainClasses()[0].clazz.withNewTransaction {
            _sendMail(emails, emailSubject, templateData, args)
        }
    }

    private static String prependEnvironmentInSubject(String subject) {
        if (Environment.current == Environment.PRODUCTION) {
            return subject
        }

        "[${Environment.current.name}] $subject"
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
    @CompileStatic(TypeCheckingMode.SKIP)
    static def _sendMail(List<String> emails, String emailSubject, Map templateData, Map args) {
        log.debug "Sending email to $emails subject [$emailSubject] args $args"

        args = args ?: [:]
        String htmlContent = args.html
        String appName = KernelUtils.getAppName()

        emailSubject = prependEnvironmentInSubject(emailSubject)

        if (templateData) {
            templateData.model = templateData.model ?: new HashMap<String, Object>()
            templateData.model.appName = appName

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
}
