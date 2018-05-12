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


public class HotelEditForm extends AbstractForm {

    private Hotel hotel;
    //private FormLayout formLayout;


    public HotelEditForm(HotelForm hotelForm) {
        super(hotelForm);

        addComponents(name, address, rating, operatesFromDay, categorySelect, description, url, buttonBar);
    }


    @Override
    protected void setComponentsSizes() {
        categorySelect.setWidth("186px");
        updateButton.setWidth("86px");
        cancelButton.setWidth("86px");
    }


    @Override
    protected void setButtons() {
        super.setButtons();

        // Update button
        updateButton.setCaption("Save");
        updateButton.addClickListener(event -> {
            save();
            hotelForm.updateHotelList();
            binder.removeBean();
            setVisible(false);
        });

        // Cancel button
        cancelButton.setCaption("Close");
        cancelButton.addClickListener(event -> setVisible(false));
    }


    @Override
    protected void selectField() {
        setBinderForField(nameKey, true);
        setBinderForField(addressKey, false);
        setBinderForField(ratingKey, false);
        setBinderForField(categoryIdKey, false);
        setBinderForField(operatesFromDayKey, false);
        setBinderForField(urlKey, false);
        setBinderForField(descriptionKey, false);
    }


    public void editHotel(Hotel hotel) {
        this.hotel = hotel;
        selectField();
        binder.readBean(hotel);
        setVisible(true);
        name.selectAll();
    }


    private void save() {
        try {
            binder.writeBean(hotel);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        hotelService.save(hotel);

        hotelForm.updateHotelList();
        setVisible(false);
    }




}
