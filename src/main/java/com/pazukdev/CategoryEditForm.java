package com.pazukdev;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;


public class CategoryEditForm extends FormLayout {

    private TextField name = new TextField("Name");

    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private CategoryService categoryService = CategoryService.getInstance();
    private HotelService hotelService = HotelService.getInstance();
    private Category category;
    private MyUI myUI;
    private Binder<Category> binder = new Binder<>(Category.class);

    private String categoryOldName;

    public CategoryEditForm(MyUI myUI) {
        this.myUI = myUI;

        name.setValueChangeMode(ValueChangeMode.EAGER);
        name.setDescription("Category name");

        binder.forField(name)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .bind(Category:: getName, Category:: setName);

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

        addComponents(name, buttons);



    }

    public void editCategory(Category category) {
        this.category=category;
        categoryOldName=category.toString();
        binder.readBean(category);
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
        categoryService.delete(category);
        myUI.updateCategoryList();
        setVisible(false);
    }

    private void save() {

        List<Hotel> hotelsList = hotelService.findAll(categoryOldName, "by category");
        for(Hotel hotel : hotelsList) {
            hotel.setCategory(category);
            hotelService.save(hotel);
        }

        try {
            binder.writeBean(category);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        categoryService.save(category);
        myUI.updateCategoryList();
        myUI.updateHotelList();
        setVisible(false);
    }


}
