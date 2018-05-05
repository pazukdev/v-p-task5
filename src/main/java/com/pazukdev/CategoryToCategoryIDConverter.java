package com.pazukdev;

import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import java.time.LocalDate;

/**
 CategoryToCategoryIDConverter.java,
 convert HotelCategory to Long categoryID
 */

@SuppressWarnings("serial")
public class CategoryToCategoryIDConverter implements Converter<HotelCategory, Long> {

    @Override
    public Result<Long> convertToModel(HotelCategory value, ValueContext context) {
        if (value == null) {
            Result.ok(null);
        }
        return Result.ok(value.getId());
    }

    @Override
    public HotelCategory convertToPresentation(Long value, ValueContext context) {
        if (value == null) {
            return null;
        }
        return CategoryService.getInstance().findById(value);
    }

}
