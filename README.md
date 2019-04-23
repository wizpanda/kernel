# Kernel (v2.0.11)

A simple Grails plugin which provide some core functionality and utility classes for a Grails application.

## Usage

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
2         |  Only v0.0.1
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
compile "com.wizpanda.plugins:kernel:0.1"
```

### Grails 2

Add the following to your `BuildConfig.groovy` file:

**Under `respositories` block**

```groovy
mavenRepo "http://dl.bintray.com/wizpanda/grails-plugins"
```

**Under `plugins` block**

```groovy
compile "com.wizpanda.plugins:kernel:0.0.1"
```

## Releasing new version

1. Change the version in the `build.gradle`.
2. Make sure Bintray configuration are configured properly as given [here](https://github.com/grails/grails-core/blob/639d7039d24031dbc1353f95b6d2c88a100da850/grails-gradle-plugin/src/main/groovy/org/grails/gradle/plugin/publishing/GrailsCentralPublishGradlePlugin.groovy).
2. Run `gradle bintrayUpload`
