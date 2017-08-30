# Kernel (v2.0.0)

A simple Grails plugin which provide some core functionality and utility classes for a Grails application.

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
