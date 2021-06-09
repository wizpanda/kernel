package com.wizpanda

import com.wizpanda.converts.FormattedStringValueConverter
import com.wizpanda.exception.KernelExceptionResolver
import grails.plugins.Plugin

class KernelGrailsPlugin extends Plugin {

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "3.1.10 > *"

    def profiles = ['web']

    def pluginExcludes = [
            '**/com/wizpanda/UrlMappings*/**'
    ]

    String title = "Kernel Grails Plugin" // Headline display name of the plugin
    String author = "Shashank Agrawal"
    String authorEmail = ""
    String description = "A Grails plugin to provide some core functionality and utility classes"

    // URL to the plugin's documentation
    //String documentation = "http://grails.org/plugin/kernel"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    //String license = "APACHE"

    Map organization = [name: "Wiz Panda", url: "http://www.wizpanda.com/"]

    Map issueManagement = [system: "GITHUB", url: "https://github.com/wizpanda/kernel/issues"]

    Map scm = [url: "https://github.com/wizpanda/kernel"]

    Closure doWithSpring() {
        { ->
            formattedStringConverter(FormattedStringValueConverter)

            // Copied from https://github.com/grails/grails-core/blob/77843bd857d3718d439e264b013eb566fc3afd32/grails-plugin-controllers/src/main/groovy/org/grails/plugins/web/controllers/ControllersGrailsPlugin.groovy#L118
            exceptionHandler(KernelExceptionResolver) {
                // TODO this is responding detailed error in API call. Fix this
                exceptionMappings = ["java.lang.Exception": "/error"]
            }
        }
    }
}
