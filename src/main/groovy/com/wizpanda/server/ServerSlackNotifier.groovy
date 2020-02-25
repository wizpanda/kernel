package com.wizpanda.server

import com.wizpanda.utils.KernelUtils
import grails.util.Environment
import grails.util.Holders
import grails.util.Metadata
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.springframework.http.MediaType

/**
 * Simple utility class to notify a Slack channel on server boot or shutdown.
 *
 * @author Shashank Agrawal
 */
@Slf4j
@CompileStatic
class ServerSlackNotifier {

    static String getCommonMessage(String message) {
        StringBuilder messageBuilder = new StringBuilder("*Attention*: ")
                .append(KernelUtils.getAppName())
                .append(" ")
                .append("(")
                .append(Environment.current.name)
                .append(")")
                .append(" server ")
                .append(message)

        messageBuilder.toString()
    }

    /**
     * Call this method in Bootstrap.groovy "init" method
     */
    static void notifyServerStartup() {
        String message = getCommonMessage("started")
        sendSlackMessage(message, "good")
    }

    /**
     * Call this method in Bootstrap.groovy "destroy" method
     */
    static void notifyServerShutdown() {
        String message = getCommonMessage("shutting down")
        sendSlackMessage(message, "danger")
    }

    /**
     * Get list of attachments (information) to be posted along with the message
     * @return
     */
    private static List<Map> getInfoAttachments(String color) {
        Metadata metadata = Holders.getGrailsApplication().metadata
        List<Map> fields = []

        Map<String, String> os = metadata["os"] as Map
        fields << [
                short: true,
                title: "Build Version",
                value: metadata["info.app.version"]
        ]

        fields << [
                short: true,
                title: "Operating System",
                value: os.name + " " + os.version + " (${os.arch})"
        ]

        fields << [
                short: true,
                title: "User",
                value: metadata["user.name"]
        ]

        fields << [
                short: true,
                title: "PID",
                value: System.getProperty("PID")
        ]

        Map fieldAttachment = [
                fields: fields,
                color : color
        ]

        [fieldAttachment] as List<Map>
    }

    private static void sendSlackMessage(String message, String color) {
        if (Environment.isDevelopmentMode()) {
            return
        }

        Map config = Holders.getFlatConfig().get("grails.plugin.kernel.server.slack.notify") as Map
        if (!config || !config.enabled) {
            return
        }

        log.debug "Sending [$message] to Slack"

        Map body = [text: message, attachments: getInfoAttachments(color)]
        if (config.channel) {
            body.channel = config.channel
        }

        String webhookURL = config.webhookURL
        Map headers = ["Accept": ContentType.ANY]
        Map args = [body: body, headers: headers, requestContentType: MediaType.APPLICATION_JSON]

        try {
            RESTClient restClient = new RESTClient(webhookURL)
            HttpResponseDecorator response = restClient.post(args) as HttpResponseDecorator

            log.debug "Slack response: " + response.getData()
        } catch (Exception e) {
            log.error "Error notifying to Slack", e
        }
    }
}
