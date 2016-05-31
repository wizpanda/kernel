package com.wizpanda.utils

import grails.gsp.PageRenderer
import grails.plugin.asyncmail.AsynchronousMailMessage
import grails.plugin.asyncmail.AsynchronousMailService
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

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

    static private AsynchronousMailService getMailService() {
        return KernelUtils.getBean("asynchronousMailService")
    }

    static AsynchronousMailMessage sendMail(String email, String subject, Map<String, Object> template, Map args) {
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
     * @param template The template options as described in http://docs.grails.org/2.5.4/ref/Tags/render.html
     * @param args Additional data to pass to "sendMail" closure
     *
     * @return Instance of AsynchronousMailMessage domain recently created
     */
    static AsynchronousMailMessage sendMail(List<String> emails, String emailSubject, Map template, Map args) {
        log.debug "Sending email to $emails subject [$emailSubject] args $args"

        args = args ?: [:]
        String htmlContent = args.htmlContent

        if (template) {
            htmlContent = getGroovyPageRenderer().render(template)
        }

        AsynchronousMailMessage messageInstance = getMailService().sendMail {
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