package com.wizpanda.server

import com.wizpanda.utils.KernelUtils
import grails.util.Environment
import grails.util.Holders
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
        sendSlackMessage(message)
    }

    /**
     * Call this method in Bootstrap.groovy "destroy" method
     */
    static void notifyServerShutdown() {
        String message = getCommonMessage("shutting down")
        sendSlackMessage(message)
    }

    private static void sendSlackMessage(String message) {
        if (Environment.isDevelopmentMode()) {
            return
        }

        Map config = Holders.getFlatConfig()["grails.plugin.kernel.server.slack.notify"]
        if (!config || !config.enabled) {
            return
        }

        log.debug "Sending [$message] to Slack"

        Map body = [text: message]
        if (config.channel) {
            body.channel = config.channel
        }

        String webhookURL = config.webhookURL
        Map headers = ["Accept": ContentType.ANY]
        Map args = [body: body, headers: headers, requestContentType: MediaType.APPLICATION_JSON]

        try {
            RESTClient restClient = new RESTClient(webhookURL)
            HttpResponseDecorator response = restClient.post(args)

            log.debug "Slack response: " + response.responseData
        } catch (Exception e) {
            log.error "Error notifying to Slack", e
        }
    }
}
