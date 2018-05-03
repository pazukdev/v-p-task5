package com.pazukdev;

import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;


public class CategoryEditForm extends FormLayout {

    private TextField name = new TextField("Name");

    private HorizontalLayout buttons;
    private Button save = new Button("Save");
    private Button close = new Button("Close");

    private CategoryService categoryService = CategoryService.getInstance();

    private HotelCategory category;
    private CategoryForm categoryForm;


    private Binder<HotelCategory> binder = new Binder<>(HotelCategory.class);



    public CategoryEditForm(CategoryForm categoryForm) {
        this.categoryForm=categoryForm;

        // Form elements settings
        setButtonsSettings();
        setLayoutsSettings();
        setComponentsSize();

        name.setValueChangeMode(ValueChangeMode.EAGER);
        name.setDescription("HotelCategory name");

        binder.forField(name)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .bind(HotelCategory:: getName, HotelCategory:: setName);

        addComponents(name, buttons);
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
        save.setWidth("86px");
        close.setWidth("86px");
        buttons.setWidth("186px");
    }


    public void editCategory(HotelCategory category) {
        this.category = category;
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


    private void save() {
        try {
            binder.writeBean(category);
        } catch (ValidationException e) {
            e.printStackTrace();
        }
        categoryService.save(category);
        categoryForm.updateCategoryList();
        setVisible(false);
    }

}
