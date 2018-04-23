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


public class HotelEditForm extends FormLayout {

    private TextField name = new TextField("Name");
    private TextField address = new TextField("Address");
    private TextField rating = new TextField("Rating");
    private DateField operatesFrom = new DateField("Date");
    private NativeSelect<Category> category = new NativeSelect<>("Category");
    private TextArea description = new TextArea("Description");
    private TextField url = new TextField("URL");



    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private HotelService hotelService = HotelService.getInstance();
    private CategoryService categoryService = CategoryService.getInstance();
    private Hotel hotel;
    private MyUI myUI;
    private Binder<Hotel> binder = new Binder<>(Hotel.class);

    public HotelEditForm(MyUI myUI) {
        this.myUI = myUI;

        name.setValueChangeMode(ValueChangeMode.EAGER);
        name.setDescription("Hotel name");

        address.setValueChangeMode(ValueChangeMode.EAGER);
        address.setDescription("Hotel address");

        rating.setValueChangeMode(ValueChangeMode.EAGER);
        rating.setDescription("Hotel star rating. Numbers: 0, 1, 2, 3, 4, 5");

        category.setItems(categoryService.findAll());
        category.setWidth("186px");
        category.setDescription("Hotel category");

        operatesFrom.setDescription("Hotel operates since");
        operatesFrom.setRangeEnd(LocalDate.now().minusDays(1L));

        url.setValueChangeMode(ValueChangeMode.EAGER);
        url.setDescription("Link to hotel page on booking.com");

        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.setDescription("Any additional info about hotel");

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
                .withValidator(dateOperatesFrom -> dateOperatesFrom.isBefore(LocalDate.now()),
                        "Wrong date. Date should be before present day")
                .bind(Hotel:: getOperatesFrom, Hotel:: setOperatesFrom);

        binder.forField(category)
                .asRequired("The field shouldn't be empty")
                .bind(Hotel:: getCategory, Hotel:: setCategory);

        String urlRegex = "^http(s{0,1})://[a-zA-Z0-9_/\\-\\.]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        //String urlRegex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        binder.forField(url)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withValidator(new RegexpValidator("This is incorrect URL", urlRegex))
                .bind(Hotel:: getUrl, Hotel:: setUrl);

        binder.bindInstanceFields(this);

        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        save.addClickListener(e -> {
            if(binder.validate().isOk()) this.save();
        });
        save.setWidth("86px");
        save.setEnabled(false);

        close.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
        close.addClickListener(e -> this.close());
        close.setWidth("86px");

        HorizontalLayout buttons = new HorizontalLayout(save, close);
        buttons.setWidth("186px");
        buttons.setComponentAlignment(close, Alignment.MIDDLE_RIGHT);

        addComponents(name, address, rating, operatesFrom, category, description, url, buttons);



    }

    public void editHotel(Hotel hotel) {

        category.setItems(categoryService.findAll());

        this.hotel=hotel;
        binder.readBean(hotel);

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

    private void delete() {
        hotelService.delete(hotel);
        myUI.updateHotelList();
        setVisible(false);
    }

    private void save() {
        try {
            binder.writeBean(hotel);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        hotelService.save(hotel);
        myUI.updateHotelList();
        setVisible(false);
    }


}
