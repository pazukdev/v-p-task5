package com.pazukdev.converters;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 StringToIntegerPayment.java,
 converts String TextField value to Integer Payment value
 */

@SuppressWarnings("serial")
public class StringToIntegerPaymentConverter implements Converter<String, Integer> {

    @Override
    public Result<Integer> convertToModel(String value, ValueContext context) {
        if(value == null || value.length() == 0) return Result.ok(null);
        else {
            try {
                return Result.ok(Integer.valueOf(value));
            } catch (NumberFormatException e) {
                return Result.ok(null);
            }
        }
    }

    @Override
    public String convertToPresentation(Integer value, ValueContext context) {
        if(value != null) return value.toString();
        else return "";
    }

}