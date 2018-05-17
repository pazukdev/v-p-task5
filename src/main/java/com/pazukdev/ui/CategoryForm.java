package com.pazukdev.ui;

import com.pazukdev.entities.Category;
import com.pazukdev.services.CategoryService;
import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

public class CategoryForm extends FormLayout implements View {
    private CategoryService categoryService=CategoryService.getInstance();

    private Grid<Category> categoryGrid =new Grid<>(Category.class);

    private HorizontalLayout categoryMainLayout;
    private HorizontalLayout categoryToolbar;

    private Button addCategory= new Button("Add category");
    private Button deleteCategory = new Button("Delete category");
    private Button editCategory = new Button("Edit category");

    private CategoryEditForm categoryEditForm = new CategoryEditForm(this);



    public CategoryForm() {
        //Components init and settings
        setGrid();
        setButtons();
        setLayouts();

        updateCategoryList();

        addComponents(categoryToolbar, categoryMainLayout);
        setMargin(false);
    }


    private void setGrid() {
        categoryGrid.setColumns(
                //"id",
                "name"
        );
        categoryGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        categoryGrid.sort(categoryGrid.getColumn("name"), SortDirection.ASCENDING);
        categoryGrid.setWidth("444px");
        categoryGrid.addSelectionListener(e -> selectionCheck());
    }


    private void setLayouts() {
        // Toolbar
        categoryToolbar = new HorizontalLayout(addCategory, deleteCategory, editCategory);

        // Category edit form
        categoryEditForm.setVisible(false);

        // Main layout
        categoryMainLayout = new HorizontalLayout(categoryGrid, categoryEditForm);
    }


    private void setButtons() {
        //Add button
        addCategory.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        addCategory.addClickListener(event -> {
            categoryGrid.asMultiSelect().clear();
            categoryEditForm.editCategory(new Category());
        });

        // Delete button
        deleteCategory.setEnabled(false);
        deleteCategory.addClickListener(e -> deleteSelected());

        // Edit button
        editCategory.setEnabled(false);
        editCategory.addClickListener(event -> {
            categoryEditForm.editCategory(categoryGrid.getSelectedItems().iterator().next());
        });
    }


    private void deleteSelected() {
        for (Category category : categoryGrid.getSelectedItems()) {
            categoryService.delete(category);
        }
        updateCategoryList();
    }


    private void selectionCheck() {
        int selectedRowsNumber = categoryGrid.getSelectedItems().size();
        if(categoryEditForm.isVisible()) {
            categoryEditForm.setVisible(false);
        }
        addCategory.setEnabled(selectedRowsNumber == 0);
        editCategory.setEnabled(selectedRowsNumber == 1);
        deleteCategory.setEnabled(selectedRowsNumber > 0);
    }


    public void updateCategoryList() {
        List<Category> categories = categoryService.findAll();
        categoryGrid.setItems(categories);
    }

}
