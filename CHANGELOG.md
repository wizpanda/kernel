# Changelog

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
