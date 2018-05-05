package com.pazukdev;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class HotelEditForm extends FormLayout {

    private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Operates from");
    private NativeSelect<HotelCategory> category = new NativeSelect<>("HotelCategory");
    private TextArea description = new TextArea("Description");
    private TextField url = new TextField("URL");

    private HorizontalLayout buttons;
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private HotelService hotelService = HotelService.getInstance();
    private CategoryService categoryService = CategoryService.getInstance();
    private Hotel hotel;
    private HotelForm hotelForm;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    private int inputLengthLimit;

    public HotelEditForm(HotelForm hotelForm) {
        this.hotelForm = hotelForm;

        // Form elements settings
        setFieldsSettings();
        setDescriptions();
        setButtonsSettings();
        setLayoutsSettings();
        setComponentsSize();

        bindFields();
        addComponents(name, address, rating, operatesFrom, category, description, url, buttons);

    }


    private void setDescriptions() {
        String maxLength = ". Max length: " + inputLengthLimit + " characters";

        name.setDescription("Hotel name" + maxLength);
        address.setDescription("Hotel address" + maxLength);
        rating.setDescription("Hotel star rating. Numbers: 0, 1, 2, 3, 4, 5");
        category.setDescription("Hotel category");
        operatesFrom.setDescription("Hotel operates since");
        url.setDescription("Link to hotel page on booking.com" + maxLength);
        description.setDescription("Any additional info about hotel");
    }

    private void setFieldsSettings() {
        inputLengthLimit =255;

        // Category
        List<HotelCategory> categories = new ArrayList<>();
        for (HotelCategory category : categoryService.findAll()) {
            categories.add(category);
        }
        //category.setItemCaptionGenerator(i -> categoryService.findById(i.longValue()).getName());
        category.setItems(categories);

        // Operates from
        operatesFrom.setRangeEnd(LocalDate.now().minusDays(1L));
    }

    private void setButtonsSettings() {
        // Button Save
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> {
            if(binder.validate().isOk()) this.save();
        });
        save.setEnabled(false);

        // Button Close
        close.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        close.addClickListener(e -> this.close());
    }

    private void setLayoutsSettings() {
        buttons = new HorizontalLayout(save, close);
        buttons.setComponentAlignment(close, Alignment.MIDDLE_RIGHT);
    }

    private void setComponentsSize() {
        category.setWidth("186px");
        save.setWidth("86px");
        close.setWidth("86px");
        buttons.setWidth("186px");
    }

    private void bindFields() {
        String wrongLengthMessage = "Length limit is exceeded";

        binder.forField(name)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withValidator(value -> value.length()<= inputLengthLimit, wrongLengthMessage)
                .bind(Hotel:: getName, Hotel:: setName);

        binder.forField(address)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withValidator(value -> value.length()<= inputLengthLimit, wrongLengthMessage)
                .bind(Hotel:: getAddress, Hotel:: setAddress);

        binder.forField(rating)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withConverter(new StringToIntegerConverter("Wrong input. Integer numbers only"))
                .withValidator(value -> value>=0 && value<=5, "Wrong value. Valid values from 0 to 5")
                .bind(Hotel:: getRating, Hotel:: setRating);

        binder.forField(operatesFrom)
                .asRequired("The field shouldn't be empty")
                .withConverter(new LocalDateToDaysConverter())
                .bind(Hotel::getOperatesFromDay, Hotel::setOperatesFromDay);

        binder.forField(category)
                .asRequired("The field shouldn't be empty")
                .withConverter(new CategoryToCategoryIDConverter())
                .bind(Hotel:: getCategory, Hotel:: setCategory);

        String urlRegex = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        binder.forField(url)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withValidator(new RegexpValidator("This is incorrect URL", urlRegex))
                .withValidator(value -> value.length()<= inputLengthLimit, wrongLengthMessage)
                .bind(Hotel:: getUrl, Hotel:: setUrl);

        binder.forField(description)
                .withNullRepresentation("")
                .bind(Hotel::getDescription, Hotel::setDescription);

    }


    public void editHotel(Hotel hotel) {
        //category.setItems(categoryService.findAll());
        this.hotel = hotel;
        binder.readBean(hotel);
        /*if (categoryService.findById(hotel.getCategoryID().longValue()) == null) {
            category.setSelectedItem(null);
        }*/
        if (hotel.getCategory() == null) {
            category.setSelectedItem(null);
        }
        binder.addStatusChangeListener(event -> {
            boolean isValid = event.getBinder().isValid();
            boolean hasChanges = event.getBinder().hasChanges();
            save.setEnabled(hasChanges && isValid);
        });

        setVisible(true);
        name.selectAll();
    }


    private void close() {
        setVisible(false);
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
