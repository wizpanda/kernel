package com.wizpanda.converts

import grails.databinding.converters.FormattedValueConverter
import groovy.transform.CompileStatic

/**
 * Custom String formatter that might convert the case of a String based on the value assigned to the
 * BindingFormat annotation.
 *
 * http://docs.grails.org/3.2.0/guide/theWebLayer.html#_custom_formatted_converters
 *
 * @author Shashank Agrawal
 * @since 1.0.4
 */
@CompileStatic
class FormattedStringValueConverter implements FormattedValueConverter {

    def convert(value, String format) {
        if ("UPPERCASE" == format) {
            value = value.toString().toUpperCase()
        } else if ("LOWERCASE" == format) {
            value = value.toString().toLowerCase()
        }

        value
    }

    Class getTargetType() {
        // Specifies the type to which this converter may be applied
        String
    }
}
