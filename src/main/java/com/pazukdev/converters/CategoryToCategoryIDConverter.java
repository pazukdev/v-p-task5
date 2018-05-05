package com.pazukdev.converters;

import com.pazukdev.services.CategoryService;
import com.pazukdev.entities.Category;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

/**
 CategoryToCategoryIDConverter.java,
 convert Category to Long categoryID
 */

@SuppressWarnings("serial")
public class CategoryToCategoryIDConverter implements Converter<Category, Long> {

    @Override
    public Result<Long> convertToModel(Category value, ValueContext context) {
        if (value == null) {
            Result.ok(null);
        }
        return Result.ok(value.getId());
    }

    @Override
    public Category convertToPresentation(Long value, ValueContext context) {
        if (value == null) {
            return null;
        }
        return CategoryService.getInstance().findById(value);
    }

}
