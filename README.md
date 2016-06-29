# Kernel (v0.0.1)

A simple Grails plugin which provide some core functionality and utility classes for a Grails 2 application.

## Compatibility

Grails Version | Supported
--------- | ---------
2         |  Yes
3         |  No (Coming soon)

## Installation

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

1. Change the version in the `KernalGrailsPlugin.groovy`
2. Run `grails maven-deploy`

You should have following in your `~/.grails/settings.groovy`

```groovy
grails.project.repos.default = "wizpandaRepo"
grails.project.repos.wizpandaRepo.username = "my-username"
grails.project.repos.wizpandaRepo.password = "my-password"
```