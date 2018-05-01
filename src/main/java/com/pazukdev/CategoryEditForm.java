package com.pazukdev;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;


public class CategoryEditForm extends FormLayout {

    private TextField name = new TextField("Name");

    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private CategoryService categoryService = CategoryService.getInstance();
    private HotelService hotelService = HotelService.getInstance();
    private HotelCategory category;

    private CategoryForm categoryForm;
    HotelForm hotelForm = new HotelForm();

    private Binder<HotelCategory> binder = new Binder<>(HotelCategory.class);

    private String categoryOldName;

    public CategoryEditForm(CategoryForm categoryForm) {
        this.categoryForm=categoryForm;

        name.setValueChangeMode(ValueChangeMode.EAGER);
        name.setDescription("HotelCategory name");

        binder.forField(name)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .bind(HotelCategory:: getName, HotelCategory:: setName);

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

    public void editCategory(HotelCategory category) {
        /*this.category=category;
        categoryOldName=category.toString();
        binder.readBean(category);
        binder.addStatusChangeListener(event -> {
            boolean isValid = event.getBinder().isValid();
            boolean hasChanges = event.getBinder().hasChanges();
            save.setEnabled(hasChanges && isValid);
        });

        setVisible(true);
        name.selectAll();*/

        this.category = category;
        //binder.setBean(category);
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

    /*private void delete() {
        categoryService.delete(category);
        categoryForm.updateCategoryList();
        setVisible(false);
    }*/

    private void save() {

        /*if(categoryOldName!=null) {
            List<Hotel> hotelsList = hotelService.findAllByCategory(categoryOldName);
            for(Hotel hotel : hotelsList) {
                hotel.setCategory(category.toString());
                hotelService.save(hotel);
            }
        }

        try {
            binder.writeBean(category);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        categoryService.save(category);
        categoryForm.updateCategoryList();
        hotelForm.updateHotelList();
        setVisible(false);*/

        if (binder.isValid()) {
            try {
                binder.writeBean(category);
            } catch (ValidationException e) {
                e.printStackTrace();
            }
            categoryService.save(category);
            categoryForm.updateCategoryList();

            setVisible(false);
        } else {
            binder.validate();
        }

    }


}
