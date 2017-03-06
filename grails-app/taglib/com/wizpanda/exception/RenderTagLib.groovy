package com.wizpanda.exception

import org.grails.encoder.CodecLookup
import org.grails.encoder.Encoder
import org.grails.exceptions.ExceptionUtils
import org.grails.web.errors.ErrorsViewStackTracePrinter
import org.springframework.util.StringUtils

/**
 * Created by shashank on 06/03/17.
 *
 * @since 1.0.5
 */
class RenderTagLib {

    static namespace = "kernel"
    static defaultEncodeAs = "raw"

    CodecLookup codecLookup
    ErrorsViewStackTracePrinter errorsViewStackTracePrinter

    /**
     * Renders an exception irrespective of a current web request for example can be used within a Job.
     *
     * <pre>
     *   <code>
     *      &lt;kernel:renderException exception="${exception}" /&gt;<br/>
     *      &lt;kernel:renderException exception="${exception}" noCodeSnippet="true" /&gt;<br/>
     *   </code>
     * </pre>
     *
     * @attr exception REQUIRED The exception to render
     * @attr noCodeSnippet OPTIONAL To avoid writing code snippet.
     */
    Closure renderException = { Map attrs ->
        if (!(attrs?.exception instanceof Throwable)) {
            return
        }

        Throwable exception = (Throwable) attrs.exception

        Encoder htmlEncoder = codecLookup.lookupEncoder("HTML")

        out << """<div style="background: #E8E8E8; padding: 10px; border-radius: 5px; margin-bottom: 10px;">"""

        def root = ExceptionUtils.getRootCause(exception)
        out << "<div><strong>Class</strong> &nbsp; ${root?.getClass()?.name ?: exception.getClass().name}</div>"
        out << "<div><strong>Message</strong> &nbsp; ${htmlEncoder.encode(exception.message)}</div>"

        if (root != null && root != exception && root.message != exception.message) {
            out << "<div><strong>Caused by</strong> &nbsp; ${htmlEncoder.encode(root.message)}</div>"
        }
        out << "<br>"

        if (!attrs.noCodeSnippet) {
            String codeSnippet = errorsViewStackTracePrinter.prettyPrintCodeSnippet(exception)

            // Browsers does not accepts CSS classes and its styles
            codeSnippet = codeSnippet.replaceAll("<code", "<code style=\"display: block\"")
                    .replaceAll("<h2", "<strong style=\"display: block\"")
                    .replaceAll("</h2", "</strong")

            out << codeSnippet
        }

        def trace = errorsViewStackTracePrinter.prettyPrint(exception.cause ?: exception)
        if (StringUtils.hasText(trace.trim())) {
            out << "<div><strong>Trace</strong></div>"
            out << '<pre>'
            out << htmlEncoder.encode(trace)
            out << '</pre>'
        }

        out << "</div>"
    }

    Closure renderErrors = { Map attrs ->
        out << """<div style="background: #E8E8E8; padding: 10px; border-radius: 5px; margin-bottom: 10px;">"""
        out << "<div><strong>Bean</strong> &nbsp; ${attrs.bean}</div>"
        out << "<code style=\"display: block\">"
        out << attrs.bean?.errors
        out << "</code>"
        out << "</div>"
    }
}
