# Kernel

[ ![Download](https://api.bintray.com/packages/wizpanda/grails-plugins/kernel/images/download.svg) ](https://bintray.com/wizpanda/grails-plugins/kernel/_latestVersion)

A simple Grails plugin which provide some core functionality and utility classes for a Grails application.

## Usage

### Logging

`KernelLogging` class provides various value to your Grails application in development as well as in production. Following are the benefits:

1. In development, Hibernate queries are logging with faded color so that you can keep watch on your SQL performance and it doesn't 
affect your normal logging.
2. Provides daily rotating logging for production with configurable directory and file name prefix.

### Slack Server Notifier

Configure the following config in your `application.groovy` file:

```groovy
grails.plugin.kernel.server.slack.notify.enabled = true
grails.plugin.kernel.server.slack.notify.webhookURL = "https://hooks.slack.com/services/xxxxxx/xxxxxxx/jJ5dbjbCnfB8EN5dBugFi3d"
```

Add `ServerSlackNotifier.notifyServerStartup()` in `Bootstrap.groovy` file in the last line of `def init` closure. This will notify the 
configured Slack channel on app startup with following info:

![image](https://user-images.githubusercontent.com/1804514/56596307-f73ba500-660d-11e9-93e2-fcb8f582b056.png)

Add `ServerSlackNotifier.notifyServerShutdown()` in `Bootstrap.groovy` file in the last line of `def destroy` closure. This will notify 
the configured Slack channel on app shutdown with following info:

![image](https://user-images.githubusercontent.com/1804514/56596481-4681d580-660e-11e9-8f0b-4556f2c84b38.png)


## Compatibility

Grails Version | Supported
--------- | ---------
3         |  >= v1.0.0
3.3.x     |  >= v2.0.0

## Change Log

See [Releases](https://github.com/wizpanda/kernel/releases) for the changes.

## Installation

### Grails 3

Add the following to `build.gradle` file of your Grails 3 application

**Under `repositories` section**

```groovy
maven { url "http://dl.bintray.com/wizpanda/grails-plugins" }
```

**Under `dependencies` section**

```groovy
compile "com.wizpanda.plugins:kernel:2.1.6"
```

## Installing locally for development

To develop in kernel plugin, you can install this plugin locally and run your app directly. For this, add the following in 
`settings.gradle` of your Grails application:

```groovy
// For inline Kernel plugin development
include ":kernel"
project(":kernel").projectDir = new File("../../kernel")
```

Then comment the line `compile "com.wizpanda.plugins:kernel:` from your `build.gradle` and add the line `compile project (':kernel')`. That's it.

https://medium.com/wizpanda/another-way-of-adding-local-grails-plugin-to-a-grails-app-using-the-gradle-build-tool-d60ddaf326cb

## Releasing new version

1. Change the version in the `build.gradle`.
2. Make sure Bintray configuration are configured properly as given [here](https://github.com/grails/grails-core/blob/639d7039d24031dbc1353f95b6d2c88a100da850/grails-gradle-plugin/src/main/groovy/org/grails/gradle/plugin/publishing/GrailsCentralPublishGradlePlugin.groovy).
2. Run `gradle bintrayUpload`
