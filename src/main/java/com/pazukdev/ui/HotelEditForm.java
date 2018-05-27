package com.pazukdev.ui;


import com.pazukdev.converters.StringToIntegerPaymentConverter;
import com.pazukdev.entities.Hotel;
import com.vaadin.ui.*;


public class HotelEditForm extends AbstractForm {

    FormLayout formLayout = new FormLayout();


    public HotelEditForm(HotelForm hotelForm) {
        super(hotelForm);
        formLayout.addComponents(name, address, rating, operatesFromDay, categorySelect, description,
                url, paymentField, buttonBar);
        formLayout.setMargin(false);
        addComponents(formLayout);
        setMargin(false);
    }


    @Override
    protected void setComponentsSizes() {
        categorySelect.setWidth("186px");
        updateButton.setWidth("86px");
        cancelButton.setWidth("86px");
        description.setHeight("70px");
    }


    @Override
    protected void setButtons() {
        super.setButtons();

        // Update button
        updateButton.setCaption("Save");
        updateButton.addClickListener(event -> {
            saveHotel(hotel);
            hotelForm.updateHotelList();
            setVisible(false);
        });

        // Cancel button
        cancelButton.setCaption("Close");
        cancelButton.addClickListener(event -> setVisible(false));
    }


    @Override
    protected void selectField() {
        setBinderForField(Hotel.class.getName(), nameKey, true);
        setBinderForField(Hotel.class.getName(), addressKey, false);
        setBinderForField(Hotel.class.getName(), ratingKey, false);
        setBinderForField(Hotel.class.getName(), categoryIdKey, false);
        setBinderForField(Hotel.class.getName(), operatesFromDayKey, false);
        setBinderForField(Hotel.class.getName(), urlKey, false);
        setBinderForField(Hotel.class.getName(), descriptionKey, false);
        setBinderForField(Hotel.class.getName(), paymentKey, false);
    }


    public void editHotel(Hotel hotel) {
        this.hotel = hotel;
        selectField();
        binder.readBean(hotel);
        setVisible(true);
        name.selectAll();
    }

}
