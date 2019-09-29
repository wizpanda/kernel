package com.wizpanda

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.gaffer.ConfigurationDelegate
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import com.wizpanda.logback.DevelopmentLoggingFilter
import grails.util.BuildSettings
import grails.util.Environment
import org.springframework.boot.logging.logback.ColorConverter
import org.springframework.boot.logging.logback.WhitespaceThrowableProxyConverter

import java.nio.charset.Charset

/**
 * A simple utility class to hold common logging properties.
 * See http://logback.qos.ch/manual/groovy.html for details on configuration
 *
 * @author Shashank Agrawal
 */
class KernelLogging {

    static private String appName
    static private String loggingDirectory = "/var/log"

    static final String PATTERN =
            "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} " + // Date
                    "%clr(%5p) " + // Log level
                    "%clr([%15.15t]){faint} " + // Thread
                    "%clr(%-40.40logger{39}){cyan} %clr(:){faint} " + // Logger
                    "%m%n%wex" // Message

    private static final String DULL_PATTERN =
            "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS} " + // Date
                    "%5p " + // Log level
                    "[%15.15t] " + // Thread
                    "%-40.40logger{39} : " + // Logger
                    "%m%n){faint}%wex"

    /**
     * File name pattern to be used for daily rotation of log files (majorly used in production & staging servers).
     * @return
     */
    private static String getFileNamePatternForGrailsApp() {
        String baseDirectory

        File targetDir = BuildSettings.TARGET_DIR
        if (isLocalEnvironment() && targetDir != null) {
            baseDirectory = targetDir.toString()
        } else {
            baseDirectory = loggingDirectory
        }

        String fileName = "${baseDirectory}/grails/${appName}-%d{yyyy-MM-dd}.log"
        println "File name pattern will be [$fileName]"

        fileName
    }

    private static void callClosure(Object delegate, @DelegatesTo(ConfigurationDelegate) Closure closure) {
        closure.delegate = delegate
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure.call()
    }

    static boolean isLocalEnvironment() {
        (Environment.current == Environment.DEVELOPMENT) || (Environment.current == Environment.TEST)
    }

    /**
     * Configure the rotating logs along with other development only logging.
     *
     * @appName A name of the app which will be used in the log file names.
     * @loggingDirectory Set the directory where the rotating logs will be stored. This is used for production only.
     * @param path
     */
    static void configure(Object delegate, String appName, String loggingDirectory) {
        this.appName = appName
        this.loggingDirectory = loggingDirectory

        callClosure(delegate) {
            String appLogFileNamePattern = getFileNamePatternForGrailsApp()

            conversionRule("clr", ColorConverter)
            conversionRule("wex", WhitespaceThrowableProxyConverter)

            appender("STDOUT", ConsoleAppender) {
                encoder(PatternLayoutEncoder) {
                    charset = Charset.forName("UTF-8")
                    pattern = PATTERN
                }
            }

            appender("ROLLING", RollingFileAppender) {
                encoder(PatternLayoutEncoder) {
                    charset = Charset.forName("UTF-8")
                    pattern = PATTERN
                }

                // http://grailsblog.objectcomputing.com/posts/2016/11/01/configure-rolling-log-files-with-logback.html
                rollingPolicy(TimeBasedRollingPolicy) {
                    fileNamePattern = appLogFileNamePattern
                    maxHistory = 30
                }
            }

            logger("com.wizpanda", Level.DEBUG)
            logger("grails.plugin.asyncmail.AsynchronousMailProcessService", Level.DEBUG)

            if (isLocalEnvironment()) {
                appender("DULL_STDOUT", ConsoleAppender) {
                    encoder(PatternLayoutEncoder) {
                        charset = Charset.forName("UTF-8")
                        pattern = DULL_PATTERN
                    }

                    filter(DevelopmentLoggingFilter)
                }

                List<String> commonAppenderNames = ["DULL_STDOUT", "ROLLING"]
                logger("org.hibernate.cache.internal", Level.DEBUG, commonAppenderNames, false)
                logger("org.hibernate.engine.internal", Level.INFO, commonAppenderNames, false)
                logger("org.hibernate.SQL", Level.TRACE, commonAppenderNames, false)
                //logger("org.hibernate", TRACE, ["DULL_STDOUT"], false)
                //logger("org.grails.orm.hibernate", DEBUG, ["DULL_STDOUT"], false)
                logger("org.hibernate.internal.SessionImpl", Level.TRACE, ["DULL_STDOUT"], false)
                logger("org.hibernate.event.internal.AbstractFlushingEventListener", Level.TRACE, ["DULL_STDOUT"], false)
                logger("org.hibernate.engine.transaction.internal.TransactionImpl", Level.DEBUG, ["DULL_STDOUT"], false)

                //logger("org.hibernate.type.descriptor.sql.BasicBinder", Level.TRACE, commonAppenderNames, false)

                // Uncomment to debug mail related issues, especially the first two lines
                /*logger("com.sun.mail.smtp", Level.ALL)
                logger("javax.mail", Level.ALL)
                logger("org.springframework.mail", Level.ALL)*/

                //logger("org.springframework.security", DEBUG, ['STDOUT'], false)
                //logger("grails.plugin.springsecurity", DEBUG, ['STDOUT'], false)
                //logger("org.pac4j", DEBUG, ['STDOUT'], false)
                //logger('grails.app.jobs.grails.plugin.asyncmail', TRACE, ['STDOUT'])
                //logger('grails.app.services.grails.plugin.asyncmail', TRACE, ['STDOUT'])
                //logger('grails.plugin.asyncmail', TRACE, ['STDOUT'])

                // Enable Quartz plugin logging
                //logger('grails.plugins.quartz', DEBUG, ['STDOUT'])

                root(Level.ERROR, ["STDOUT", "ROLLING"])
            } else {
                root(Level.ERROR, ["ROLLING"])
            }
        }
    }
}
