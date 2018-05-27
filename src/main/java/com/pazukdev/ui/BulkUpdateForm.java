package com.pazukdev.ui;

import com.pazukdev.entities.Category;
import com.pazukdev.entities.Hotel;
import com.vaadin.ui.*;

public class BulkUpdateForm extends AbstractForm {

    private static PopupView instance;


    public BulkUpdateForm(HotelForm hotelForm) {
        super(hotelForm);

        selectField();

        Label label = new Label(hotelForm.bulkUpdate.getCaption());
        addComponents(label, fieldSelect, categorySelect, name, address, rating, url,
                description, operatesFromDay, buttonBar);
    }


    public static PopupView getInstance(HotelForm hotelForm) {
        BulkUpdateForm bulkUpdateForm = new BulkUpdateForm(hotelForm);
        instance =  new PopupView(null, bulkUpdateForm);
        instance.setHideOnMouseOut(false);
        instance.addPopupVisibilityListener(event -> {
            bulkUpdateForm.resetForm();
        });
        return instance;
    }


    @Override
    protected NativeSelect<Category> initCategorySelect() {
        categorySelect = super.initCategorySelect();

        categorySelect.setCaption("Select hotel category");
        categorySelect.setVisible(false);
        return categorySelect;
    }


    @Override
    protected DateField initDateField() {
        operatesFromDay = super.initDateField();

        operatesFromDay.setCaption(null);
        operatesFromDay.setVisible(false);
        return operatesFromDay;
    }


    @Override
    protected void setFields(String entityClassName) {
        super.setFields(Hotel.class.getName());

        name.setCaption(null);
        name.setVisible(false);

        address.setCaption(null);
        address.setVisible(false);

        rating.setCaption(null);
        rating.setVisible(false);

        description.setCaption(null);
        description.setVisible(false);

        url.setCaption(null);
        url.setVisible(false);
    }


    @Override
    protected void setButtons() {
        super.setButtons();

        // Update button
        updateButton.addClickListener(event -> {
            saveAllHotels();
            instance.setPopupVisible(false);
            hotelForm.updateHotelList();
        });

        // Cancel button
        cancelButton.addClickListener(event -> instance.setPopupVisible(false));
    }


    @Override
    protected void setComponentsSizes() {
        // NativeSelects sizes
        fieldSelect.setSizeFull();
        categorySelect.setSizeFull();

        // TextFields size
        name.setSizeFull();
        address.setSizeFull();
        rating.setSizeFull();
        url.setSizeFull();
        description.setSizeFull();

        // Buttons size
        String buttonsWidth = "100px";
        updateButton.setWidth(buttonsWidth);
        cancelButton.setWidth(buttonsWidth);
    }


    private void refreshForm() {
        clearAllFields();
        setAllFieldsInvisible();
        setPlaceholders();
        categorySelect.setSelectedItem(null);
        categorySelect.setVisible(false);
        operatesFromDay.clear();
        operatesFromDay.setVisible(false);
        description.clear();
        description.setVisible(false);
        updateButton.setEnabled(false);
    }


    @Override
    protected void selectField() {
        fieldSelect.addSelectionListener(event -> {
            refreshForm();

            if(fieldSelect.getValue() != null) {
                if(fieldSelect.getValue().equals(super.nameKey)) {
                    setBinderForField(Hotel.class.getName(), super.nameKey, true);

                    if(allFieldValuesAreEquals(nameKey)) {
                        binder.readBean(hotelForm.getSelected().get(0));
                        name.selectAll();
                    }
                    name.setVisible(true);
                }

                if(fieldSelect.getValue().equals(addressKey)) {
                    setBinderForField(Hotel.class.getName(), super.addressKey, true);

                    if(allFieldValuesAreEquals(fieldSelect.getValue())) {
                        binder.readBean(hotelForm.getSelected().get(0));
                        address.selectAll();
                    }
                    address.setVisible(true);
                }

                if(fieldSelect.getValue().equals(ratingKey)) {
                    setBinderForField(Hotel.class.getName(), ratingKey, true);

                    if(allFieldValuesAreEquals(fieldSelect.getValue())) {
                        binder.readBean(hotelForm.getSelected().get(0));
                        rating.selectAll();
                    }
                    rating.setVisible(true);
                }

                if(fieldSelect.getValue().equals(operatesFromDayKey)) {
                    setBinderForField(Hotel.class.getName(), operatesFromDayKey, true);

                    if(allFieldValuesAreEquals(fieldSelect.getValue())) {
                        binder.readBean(hotelForm.getSelected().get(0));
                    }
                    operatesFromDay.setVisible(true);
                }

                if(fieldSelect.getValue().equals(categoryIdKey)) {
                    setBinderForField(Hotel.class.getName(), categoryIdKey, true);

                    if(allFieldValuesAreEquals(fieldSelect.getValue())) {
                        binder.readBean(hotelForm.getSelected().get(0));
                    }
                    categorySelect.setVisible(true);
                }

                if(fieldSelect.getValue().equals(urlKey)) {
                    setBinderForField(Hotel.class.getName(), urlKey, true);

                    if(allFieldValuesAreEquals(fieldSelect.getValue())) {
                        binder.readBean(hotelForm.getSelected().get(0));
                        url.selectAll();
                    } else {
                        url.setValue("http://");
                    }
                    url.setVisible(true);
                }

                if(fieldSelect.getValue().equals(descriptionKey)) {
                    setBinderForField(Hotel.class.getName(), descriptionKey, true);

                    if(allFieldValuesAreEquals(fieldSelect.getValue())) {
                        binder.readBean(hotelForm.getSelected().get(0));
                        description.selectAll();
                    } else {
                        updateButton.setEnabled(true);
                    }
                    description.setVisible(true);
                }

            }
        });
    }


}
