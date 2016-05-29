class KernelGrailsPlugin {

    // The plugin version
    String version = "0.1"
    String groupId = "com.wizpanda.plugins"
    // The version or versions of Grails the plugin is designed for
    String grailsVersion = "2.5 > *"
    // Resources that are excluded from plugin packaging
    List pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    String title = "Kernel Grails Plugin" // Headline display name of the plugin
    String author = "Shashank Agrawal"
    String authorEmail = ""
    String description = "A Grails plugin to provide some core functionality and utility classes"

    // URL to the plugin's documentation
    //String documentation = "http://grails.org/plugin/kernel"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    //String license = "APACHE"

    Map organization = [name: "Wiz Panda", url: "http://www.my-company.com/"]

    Map issueManagement = [system: "GITHUB", url: "https://github.com/wizpanda/kernel/issues"]

    Map scm = [ url: "https://github.com/wizpanda/kernel" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}