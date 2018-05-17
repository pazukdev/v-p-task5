package com.pazukdev.ui;

import com.pazukdev.converters.CategoryToCategoryIDConverter;
import com.pazukdev.converters.LocalDateToDaysConverter;
import com.pazukdev.entities.Category;
import com.pazukdev.entities.Hotel;
import com.pazukdev.services.CategoryService;
import com.pazukdev.services.HotelService;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CategoryEditForm extends AbstractForm {


    public CategoryEditForm(CategoryForm categoryForm) {
        super(categoryForm);
        addComponents(categoryName, buttonBar);
        setMargin(false);
    }


    @Override
    protected void setComponentsSizes() {
        updateButton.setWidth("86px");
        cancelButton.setWidth("86px");
        buttonBar.setWidth("186px");
    }


    @Override
    protected void setButtons() {
        super.setButtons();

        // Update button
        updateButton.setCaption("Save");
        updateButton.addClickListener(event -> {
            saveCategory(category);
            categoryForm.updateCategoryList();
            setVisible(false);
        });

        // Cancel button
        cancelButton.setCaption("Close");
        cancelButton.addClickListener(event -> setVisible(false));
    }


    @Override
    protected void selectField() {
        setBinderForField(Category.class.getName(), categoryNameKey, true);
    }


    public void editCategory(Category category) {
        this.category = category;
        selectField();
        categoryBinder.readBean(category);
        setVisible(true);
        categoryName.selectAll();
    }

}
