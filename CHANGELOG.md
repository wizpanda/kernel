# Changelog

## v3.0.1

1. Globally handle unhandled exceptions via Sentry.
2. Add request details interceptor for adding request details in Sentry events.

## v3.0.0

1. Rename `RequestUtils.groovy` to `KernelRequestUtils.groovy` to avoid conflict in app.
2. ServerSlackNotifier should not check for dev environment.
3. Upgrade sentry-java to 5.0.0

## v2.2.1

This release does not bring any code changes. It only changes the artifactory/maven location of publishing.
We have now moved from Bintray to JFrog because Bintray has been shutdown. Read more
[here](https://jfrog.com/blog/into-the-sunset-bintray-jcenter-gocenter-and-chartcenter/).

### Required Changes

**In your `build.gradle`**-

```diff
allprojects {
    repositories {
-       maven { url "https://dl.bintray.com/wizpanda/grails-plugins" }
+       maven { url "https://wizpanda.jfrog.io/artifactory/default-maven-local" }
    }
}
```

## v2.2.0

1. Deleted the unused `MailUtils` methods for developers as we switched to Sentry.
2. Added `httpResponseCode` in `ErrorCodeAwareException`.

## v2.1.8

1. Deleting Paytm related code & moved to https://github.com/wizpanda/kernel-paytm

## v2.1.7

1. Feature: Added taglib and commonly used classes for [Paytm Basic checkout](https://developer.paytm.com/docs/v1/payment-gateway/).
2. Feature: Added method `bind` in `DomainUtils` to avoid passing `null` when directly using `DataBindingUtils.bindObjectToInstance`.
3. Improvement: Using `@GrailsCompileStatic` or `@CompileStatic` where possible.

## v2.1.6

1. Using current request Locale while resolving message.

## v2.1.5

1. Added an option to change the environment for extra dull logs.
2. Added utility methods for request URLs.
3. Updating LocalDateUtils to get the correct 7 days range.

## v2.1.4

1. Added a new method `getDateRangeOfDay` in `DateRangeUtils`

## v2.1.3

1. Added a new method to track validation errors to Sentry.

## v2.1.2

1. Fix: Removed "protected" access identifier from declarative exception handling methods.

## v2.1.1

1. Improvement: Added an option to use the sentry in the local environment as well.

## v2.1.0

1. Feature: Added `SentryLogger` to log errors to Sentry
2. Breaking Change: Changing the package of a few classes.
3. Breaking Change: Changing traits to abstract classes.
4. Improvement: Responding first Validation error message directly.

## v2.0.18

1. Logging session events
2. Logging transaction events

## v2.0.17

1. Added `KernlLogging` utility class for logback configuration.

## v2.0.16

1. Fixed generating zip files using `ZipFileUtils` because of wrong separator.

## v2.0.15

1. Added a new `ZipFileUtils` class to zip a file or directory.
