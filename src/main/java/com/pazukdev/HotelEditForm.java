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


public class HotelEditForm extends FormLayout {

    private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Operates from");
    private NativeSelect<Integer> category = new NativeSelect<>("HotelCategory");
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

    public HotelEditForm(HotelForm hotelForm) {
        this.hotelForm = hotelForm;

        // Form elements settings
        setDescriptions();
        setFieldsSettings();
        setButtonsSettings();
        setFieldsValueChangeMode();
        setLayoutsSettings();
        setComponentsSize();

        bindFields();
        addComponents(name, address, rating, operatesFrom, category, description, url, buttons);

    }


    private void setDescriptions() {
        name.setDescription("Hotel name");
        address.setDescription("Hotel address");
        rating.setDescription("Hotel star rating. Numbers: 0, 1, 2, 3, 4, 5");
        category.setDescription("Hotel category");
        operatesFrom.setDescription("Hotel operates since");
        url.setDescription("Link to hotel page on booking.com");
        description.setDescription("Any additional info about hotel");
    }

    private void setFieldsSettings() {
        // Category
        List<Integer> categories = new ArrayList<>();
        for (HotelCategory category : CategoryService.getInstance().findAll()) {
            categories.add(category.getId());
        }
        category.setItemCaptionGenerator(i -> categoryService.findById(i).getName());
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

    private void setFieldsValueChangeMode() {
        name.setValueChangeMode(ValueChangeMode.EAGER);
        address.setValueChangeMode(ValueChangeMode.EAGER);
        rating.setValueChangeMode(ValueChangeMode.EAGER);
        url.setValueChangeMode(ValueChangeMode.EAGER);
        description.setValueChangeMode(ValueChangeMode.EAGER);
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
        binder.forField(name)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .bind(Hotel:: getName, Hotel:: setName);

        binder.forField(address)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
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
                .bind(Hotel:: getCategoryID, Hotel:: setCategoryID);

        String urlRegex = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        binder.forField(url)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withValidator(new RegexpValidator("This is incorrect URL", urlRegex))
                .bind(Hotel:: getUrl, Hotel:: setUrl);

        binder.forField(description)
                .withNullRepresentation("")
                .bind(Hotel::getDescription, Hotel::setDescription);

        //binder.bindInstanceFields(this);

    }


    public void editHotel(Hotel hotel) {
        //category.setItems(categoryService.findAll());
        this.hotel = hotel;
        binder.readBean(hotel);
        if (CategoryService.getInstance().findById(hotel.getCategoryID()) == null) {
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
