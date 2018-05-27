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

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;


public abstract class AbstractForm extends VerticalLayout {

    protected Hotel hotel;
    protected Category category;

    protected HotelForm hotelForm;
    protected CategoryForm categoryForm;

    protected HotelService hotelService = HotelService.getInstance();
    protected CategoryService categoryService =CategoryService.getInstance();

    protected NativeSelect<String> fieldSelect;
    protected NativeSelect<Category> categorySelect;

    // Fields for Hotel class
    protected TextField name;
    protected TextField address;
    protected TextField rating;
    protected TextField url;

    protected DateField operatesFromDay;

    protected TextArea description;

    protected PaymentField paymentField;

    // Fields for Category class
    protected TextField categoryName;

    protected Button updateButton;
    protected Button cancelButton;
    protected HorizontalLayout buttonBar;

    protected Binder<Hotel> binder;
    protected Binder<Category> categoryBinder;

    protected List<TextField> textFieldsList;

    private int inputLengthLimit = 255;

    protected String nameKey = "Name";
    protected String addressKey = "Address";
    protected String ratingKey = "Rating";
    protected String operatesFromDayKey = "Operates from";
    protected String categoryIdKey ="Category";
    protected String urlKey = "URL";
    protected String descriptionKey = "Description";
    protected String paymentKey = "Payment";

    protected String categoryNameKey = "Category name";


    protected AbstractForm() {}

    protected AbstractForm(HotelForm hotelForm) {
        this.hotelForm = hotelForm;

        // Components init and settings
        setFields(Hotel.class.getName());
        setDescriptions();
        setPlaceholders();
        setButtons();
        setTextFieldsList();
        buttonBar = new HorizontalLayout(updateButton, cancelButton);
        setComponentsSizes();
        setMargin(false);
    }


    protected AbstractForm(CategoryForm categoryForm) {
        this.categoryForm = categoryForm;

        // Components init and settings
        setFields(Category.class.getName());
        setDescriptions();
        setPlaceholders();
        setButtons();
        buttonBar = new HorizontalLayout(updateButton, cancelButton);
        setComponentsSizes();
        setMargin(false);
    }


    private void setTextFieldsList() {
        textFieldsList = new ArrayList<>();
        textFieldsList.add(name);
        textFieldsList.add(address);
        textFieldsList.add(rating);
        textFieldsList.add(url);
    }


    protected void setAllFieldsInvisible() {
        textFieldsList.forEach(textField -> textField.setVisible(false));
    }


    protected void clearAllFields() {
        textFieldsList.forEach(textField -> textField.clear());
    }


    protected void resetForm() {
        fieldSelect.setSelectedItem(null);
    }


    protected Map<String, Field> getClassFieldsMap(String entityClassName) {
        if(entityClassName.equals(Hotel.class.getName())) {
            Hotel hotel = new Hotel();
            Class c = hotel.getClass();

            Field name = null;
            Field address = null;
            Field rating = null;
            Field operatesFromDay = null;
            Field categoryId = null;
            Field url = null;
            Field description = null;
            Field payment = null;
            try {
                name = c.getDeclaredField("name");
                address = c.getDeclaredField("address");
                rating = c.getDeclaredField("rating");
                operatesFromDay = c.getDeclaredField("operatesFromDay");
                categoryId = c.getDeclaredField("categoryId");
                url = c.getDeclaredField("url");
                description = c.getDeclaredField("description");
                payment = c.getDeclaredField("payment");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            Map<String, Field> classFieldsMap = new TreeMap<>();
            classFieldsMap.put(nameKey, name);
            classFieldsMap.put(addressKey, address);
            classFieldsMap.put(ratingKey, rating);
            classFieldsMap.put(operatesFromDayKey, operatesFromDay);
            classFieldsMap.put(categoryIdKey, categoryId);
            classFieldsMap.put(urlKey, url);
            classFieldsMap.put(descriptionKey, description);

            return classFieldsMap;
        }

        if(entityClassName.equals(Category.class.getName())) {
            Category category = new Category();
            Class c = category.getClass();

            Field categoryName = null;
            try {
                categoryName = c.getDeclaredField("name");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            Map<String, Field> classFieldsMap = new TreeMap<>();
            classFieldsMap.put(categoryNameKey, categoryName);

            return classFieldsMap;
        }

        return null;

    }


    protected NativeSelect<String> initFieldSelect(String entityClassName) {
        Map<String, Field> classFieldsMap = getClassFieldsMap(entityClassName);
        NativeSelect<String> fieldSelect = new NativeSelect<>("Select field to edit");
        fieldSelect.setItems(classFieldsMap.keySet());
        return fieldSelect;
    }


    protected NativeSelect<Category> initCategorySelect() {
        NativeSelect<Category> categorySelect = new NativeSelect<>(categoryIdKey);
        categorySelect.setId("hotelCategoryNativeSelect");
        categorySelect.setItems(categoryService.findAll());
        return categorySelect;
    }

    protected DateField initDateField() {
        DateField operatesFromDay = new DateField(operatesFromDayKey);
        operatesFromDay.setId("hotelOperatesFromDateField");
        operatesFromDay.setDateFormat("dd.MM.yyyy");
        operatesFromDay.setRangeEnd(LocalDate.now().minusDays(1L));
        operatesFromDay.setRangeStart(null);
        return operatesFromDay;
    }


    protected void setFields(String entityClassName) {
        fieldSelect = initFieldSelect(entityClassName);
        categorySelect = initCategorySelect();
        operatesFromDay = initDateField();

        name = new TextField(nameKey);
        name.setId("hotelNameTextField");
        address = new TextField(addressKey);
        address.setId("hotelAddressTextField");
        rating = new TextField(ratingKey);
        rating.setId("hotelRatingTextField");
        description = new TextArea(descriptionKey);
        description.setId("hotelDescriptionTextArea");
        url = new TextField(urlKey);
        url.setId("hotelUrlTextField");
        paymentField = new PaymentField(paymentKey);
        paymentField.setId("hotelPaymentCustomField");

        categoryName = new TextField(categoryNameKey);
        categoryName.setId("categoryNameTextField");
    }


    protected void setPlaceholders() {
        // For Hotel class forms
        name.setPlaceholder(nameKey);
        address.setPlaceholder(addressKey);
        rating.setPlaceholder(ratingKey);
        description.setPlaceholder(descriptionKey);

        // For Category class forms
        categoryName.setPlaceholder(categoryNameKey);
    }


    private void setDescriptions() {
        String maxLength = ". Max length: " + inputLengthLimit + " characters";

        // For Hotel class forms
        fieldSelect.setDescription("Select field to edit");
        categorySelect.setDescription("Select hotel category");

        name.setDescription("Hotel name" + maxLength);
        address.setDescription("Hotel address" + maxLength);
        rating.setDescription("Hotel star rating. Numbers: 0, 1, 2, 3, 4, 5");
        url.setDescription("Link to hotel page on booking.com" + maxLength);
        description.setDescription("Any additional info about hotel");

        operatesFromDay.setDescription("Hotel operates since");

        // For Category class forms
        categoryName.setDescription("Category name" + maxLength);
    }


    protected void setButtons() {
        // Update button
        updateButton = new Button("Update");
        updateButton.setId("updateButton");
        updateButton.setEnabled(false);
        updateButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        updateButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // Cancel button
        cancelButton = new Button("Cancel");
        cancelButton.setId("cancelButton");
        cancelButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
    }


    protected void saveHotel(Hotel hotel) {
        try {
            binder.writeBean(hotel);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        hotelService.save(hotel);
    }


    protected void saveAllHotels() {
        for (Hotel h : hotelForm.getSelected()) {
            try {
                binder.writeBean(h);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            hotelService.save(h);
        }
    }


    protected void saveCategory(Category category) {
        try {
            categoryBinder.writeBean(category);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        categoryService.save(category);
    }


    protected boolean allFieldValuesAreEquals(String fieldDescription) {
        Set<String> set = new HashSet<>();
        switch (fieldDescription) {
            case "Name":
                for(Hotel hotel : hotelForm.getSelected()) {
                    set.add(hotel.getName());
                }
                break;

            case "Address":
                for(Hotel hotel : hotelForm.getSelected()) {
                    set.add(hotel.getAddress());
                }
                break;

            case "Rating":
                for(Hotel hotel : hotelForm.getSelected()) {
                    set.add(hotel.getRating().toString());
                }
                break;

            case "Operates from":
                for(Hotel hotel : hotelForm.getSelected()) {
                    set.add(hotel.getOperatesFromDay().toString());
                }
                break;

            case "Category":
                for(Hotel hotel : hotelForm.getSelected()) {
                    if(hotel.getCategoryId() != null) set.add(hotel.getCategoryId().toString());
                    else set.add("No category");
                }
                break;

            case "URL":
                for(Hotel hotel : hotelForm.getSelected()) {
                    set.add(hotel.getUrl());
                }
                break;

            case "Description":
                for(Hotel hotel : hotelForm.getSelected()) {
                    set.add(hotel.getDescription());
                }
                break;

        }

        return set.size() == 1;
    }


    private void setNewBinder(String entityClassName) {
        if(entityClassName.equals(Hotel.class.getName())) {
            binder = new Binder<>(Hotel.class);

            binder.addStatusChangeListener(e -> {
                boolean isValid = e.getBinder().isValid();
                boolean hasChanges = e.getBinder().hasChanges();
                updateButton.setEnabled(isValid && hasChanges);
            });
        }

        if(entityClassName.equals(Category.class.getName())) {
            categoryBinder = new Binder<>(Category.class);

            categoryBinder.addStatusChangeListener(e -> {
                boolean isValid = e.getBinder().isValid();
                boolean hasChanges = e.getBinder().hasChanges();
                updateButton.setEnabled(isValid && hasChanges);
            });
        }

    }


    protected void setBinderForField(String entityClassName, String fieldKey, boolean withNewBinder) {
        String wrongLengthMessage = "Length limit is exceeded";

        if(withNewBinder) {
            setNewBinder(entityClassName);
        }

        if(fieldKey.equals(nameKey)) {
            binder.forField(name)
                    .asRequired("The field shouldn't be empty")
                    .withNullRepresentation("")
                    .withValidator(value -> value.length() <= inputLengthLimit, wrongLengthMessage)
                    .bind(Hotel:: getName, Hotel:: setName);
        }

        if(fieldKey.equals(addressKey)) {
            binder.forField(address)
                    .asRequired("The field shouldn't be empty")
                    .withNullRepresentation("")
                    .withValidator(value -> value.length() <= inputLengthLimit, wrongLengthMessage)
                    .bind(Hotel:: getAddress, Hotel:: setAddress);
        }

        if(fieldKey.equals(ratingKey)) {
            binder.forField(rating)
                    .asRequired("The field shouldn't be empty")
                    .withNullRepresentation("")
                    .withConverter(new StringToIntegerConverter("Wrong input. Integer numbers only"))
                    .withValidator(value -> value >= 0 && value <= 5, "Wrong value. Valid values from 0 to 5")
                    .bind(Hotel:: getRating, Hotel:: setRating);
        }

        if(fieldKey.equals(operatesFromDayKey)) {
            binder.forField(operatesFromDay)
                    .asRequired("The field shouldn't be empty")
                    .withConverter(new LocalDateToDaysConverter())
                    .bind(Hotel::getOperatesFromDay, Hotel::setOperatesFromDay);
        }

        if(fieldKey.equals(categoryIdKey)) {
            binder.forField(categorySelect)
                    .asRequired("The field shouldn't be empty")
                    .withConverter(new CategoryToCategoryIDConverter())
                    .bind(Hotel::getCategoryId, Hotel::setCategoryId);
        }

        if(fieldKey.equals(urlKey)) {
            String urlRegex = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
            binder.forField(url)
                    .asRequired("The field shouldn't be empty")
                    .withNullRepresentation("")
                    .withValidator(new RegexpValidator("This is incorrect URL", urlRegex))
                    .withValidator(value -> value.length() <= inputLengthLimit, wrongLengthMessage)
                    .bind(Hotel:: getUrl, Hotel:: setUrl);
        }

        if(fieldKey.equals(descriptionKey)) {
            binder.forField(description)
                    .withNullRepresentation("")
                    .bind(Hotel::getDescription, Hotel::setDescription);
        }

        if(fieldKey.equals(paymentKey)) {
            binder.forField(paymentField)
                    .asRequired("")
                    .withValidator(payment -> (payment.getPaymentValue() != null && payment.getPaymentValue() != -1)
                                    || payment.getPaymentValue() == null, "Wrong input: incorrect payment value")
                    .bind(Hotel::getPayment, Hotel::setPayment);
        }

        if(fieldKey.equals(categoryNameKey)) {
            categoryBinder.forField(categoryName)
                    .asRequired("The field shouldn't be empty")
                    .withNullRepresentation("")
                    .withValidator(value -> value.length() <= inputLengthLimit, wrongLengthMessage)
                    .bind(Category:: getName, Category:: setName);
        }

    }


    protected abstract void setComponentsSizes();
    protected abstract void selectField();

}


