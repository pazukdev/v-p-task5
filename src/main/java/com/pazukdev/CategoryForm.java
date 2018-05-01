package com.pazukdev;

import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.*;

import java.util.List;
import java.util.Set;

public class CategoryForm extends FormLayout implements View {

    private HotelService hotelService =HotelService.getInstance();
    private CategoryService categoryService=CategoryService.getInstance();

    private Grid<HotelCategory> categoryGrid =new Grid<>(HotelCategory.class);


    private Button addCategory= new Button("Add category");
    private Button deleteCategory = new Button("Delete category");
    private Button editCategory = new Button("Edit category");

    private CategoryEditForm categoryEditForm = new CategoryEditForm(this);



    public CategoryForm() {

        addCategory.addClickListener(event -> {
            categoryGrid.asMultiSelect().clear();
            categoryEditForm.editCategory(new HotelCategory());
        });

        deleteCategory.setEnabled(false);
        /*deleteCategory.addClickListener(event -> {
            Set<HotelCategory> deleteCandidates=categoryGrid.getSelectedItems();
            for(HotelCategory category : deleteCandidates) {
                List<Hotel> hotelsList = hotelService.findAll(category.toString(), "by category");
                for(Hotel hotel : hotelsList) {
                    hotel.setCategory(CategoryService.getNullCategory().toString());
                    //hotel.setCategory(null);
                    hotelService.save(hotel);
                }
                categoryService.delete(category);
            }
            updateCategoryList();
            //updateHotelList();
        });*/
        deleteCategory.addClickListener(e -> deleteSelected());

        editCategory.setEnabled(false);
        editCategory.addClickListener(event -> {
            categoryEditForm.editCategory(categoryGrid.getSelectedItems().iterator().next());
        });

        /*String categoryToolbarElementsWidth="100px";
        addCategory.setWidth(categoryToolbarElementsWidth);
        deleteCategory.setWidth(categoryToolbarElementsWidth);
        editCategory.setWidth(categoryToolbarElementsWidth);*/
        HorizontalLayout categoryToolbar = new HorizontalLayout(addCategory, deleteCategory, editCategory);

        categoryGrid.setColumns(
                //"id",
                "name"
        );
        categoryGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        categoryGrid.sort(categoryGrid.getColumn("name"), SortDirection.ASCENDING);
        categoryGrid.setWidth("444px");
        categoryGrid.addSelectionListener(e -> selectionCheck());
        /*categoryGrid.asMultiSelect().addSelectionListener(event -> {
            if(event.getValue().isEmpty()) {
                deleteCategory.setEnabled(false);
                editCategory.setEnabled(false);
                categoryEditForm.setVisible(false);
            }
            else if(event.getValue().size()==1) {
                deleteCategory.setEnabled(true);
                editCategory.setEnabled(true);
            }
            else if(event.getValue().size()>1){
                deleteCategory.setEnabled(true);
                editCategory.setEnabled(false);
                categoryEditForm.setVisible(false);
            }
        });*/

        HorizontalLayout mainCategory = new HorizontalLayout(categoryGrid, categoryEditForm);

        categoryEditForm.setVisible(false);

        updateCategoryList();

        addComponents(categoryToolbar, mainCategory);


    }

    private void deleteSelected() {
        for (HotelCategory category : categoryGrid.getSelectedItems()) {
            categoryService.delete(category);
        }
        updateCategoryList();
    }

    private void selectionCheck() {
        editCategory.setEnabled(categoryGrid.getSelectedItems().size() == 1);
        deleteCategory.setEnabled(categoryGrid.getSelectedItems().size() > 0);
    }


    public void updateCategoryList() {
        List<HotelCategory> categories = categoryService.findAll();
        categoryGrid.setItems(categories);
    }


}
