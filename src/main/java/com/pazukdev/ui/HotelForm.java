package com.pazukdev.ui;

import com.pazukdev.entities.Category;
import com.pazukdev.entities.Hotel;
import com.pazukdev.services.CategoryService;
import com.pazukdev.services.HotelService;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.time.LocalDate;
import java.util.*;


@Theme("mytheme")
public class HotelForm extends FormLayout implements View {
    private HotelService hotelService = HotelService.getInstance();
    private CategoryService categoryService = CategoryService.getInstance();

    private Grid<Hotel> hotelGrid =new Grid<>(Hotel.class);

    private HorizontalLayout hotelMainLayout;
    private HorizontalLayout hotelToolbar;

    private TextField filterByName =new TextField();
    private TextField filterByAddress=new TextField();

    private Button addHotel= new Button("Add hotel");
    private Button deleteHotel = new Button("Delete hotel");
    private Button editHotel = new Button("Edit hotel");
    Button bulkUpdate = new Button("Bulk Update");

    private HotelEditForm hotelEditForm;
    private PopupView popupView;


    public HotelForm() {
        //Components init and settings
        setGrid();
        setFilters();
        setButtons();
        setLayoutsAndForms();
        setComponentsSize();

        updateHotelList();

        addComponents(hotelToolbar, hotelMainLayout);
    }


    private void setGrid() {
        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        hotelGrid.setWidth("1000px");
        hotelGrid.setHeight("524px");
        hotelGrid.setBodyRowHeight(30);
        hotelGrid.getColumn("name").setMaximumWidth(260);
        hotelGrid.sort(hotelGrid.getColumn("name"), SortDirection.ASCENDING);
        hotelGrid.getColumn("address").setMaximumWidth(260);
        hotelGrid.getColumn("description").setExpandRatio(1);
        hotelGrid.addItemClickListener(event -> Notification.show(event.getItem().toString()));
        hotelGrid.addSelectionListener(e -> selectionCheck());

        hotelGrid.setColumns(
                //"id",
                "name",
                "address",
                "rating"
                //"operatesFrom"
                //"categoryID",
                //"description"
                //"url"
        );

        Grid.Column<Hotel, LocalDate> operatesFromColumn = hotelGrid.addColumn(hotel -> {
            return LocalDate.ofEpochDay(hotel.getOperatesFromDay());
        }).setCaption("Operates from");

        Grid.Column<Hotel, String> categoryColumn = hotelGrid.addColumn(hotel -> {
            Category category = categoryService.findById(hotel.getCategoryId());
            if (category != null) {
                return category.getName();
            }
            return "No category";
        }).setCaption("Category").setMaximumWidth(260);

        Grid.Column<Hotel, String> urlColumn = hotelGrid.addColumn(hotel ->
                "<a target=\"_blank\" href='" + hotel.getUrl() + "'>" + hotel.getUrl() + "</a>", new HtmlRenderer())
                .setCaption("URL").setMaximumWidth(260);

        Grid.Column<Hotel, String> descriptionColumn = hotelGrid.addColumn(Hotel::getDescription)
                .setCaption("Description").setMaximumWidth(260);

    }


    private void setFilters() {
        // Filter by name
        filterByName.setPlaceholder("filter by name");
        filterByName.addValueChangeListener(e -> updateHotelList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        // Filter by address
        filterByAddress.setPlaceholder("filter by address");
        filterByAddress.addValueChangeListener(e -> updateHotelList());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
    }


    private void setLayoutsAndForms() {
        // Toolbar
        hotelToolbar = new HorizontalLayout(filterByName, filterByAddress, addHotel,
                deleteHotel, editHotel, bulkUpdate);

        // Hotel edit form
        hotelEditForm = new HotelEditForm(this);
        hotelEditForm.setVisible(false);

        // Popup
        popupView = BulkUpdateForm.getInstance(this);
        popupView.addPopupVisibilityListener(event -> {
            deleteHotel.setEnabled(!popupView.isPopupVisible());
            bulkUpdate.setEnabled(!popupView.isPopupVisible());
        });

        // Main layout
        hotelMainLayout = new HorizontalLayout();
        hotelMainLayout.addComponents(hotelGrid, hotelEditForm, popupView);
    }


    private void setButtons() {
        //Add button
        addHotel.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        addHotel.addClickListener(event -> {
            //hotelGrid.asMultiSelect().clear();
            if(hotelEditForm.isVisible()) hotelEditForm.setVisible(false);
            //LocalDate yesterday=LocalDate.now().minusDays(1L);
            hotelEditForm.editHotel(new Hotel());
        });

        // Delete button
        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(event -> deleteSelected());

        // Edit button
        editHotel.setEnabled(false);
        editHotel.addClickListener(event -> hotelEditForm.editHotel(hotelGrid.getSelectedItems().iterator().next()));

        // Bulk Update button
        bulkUpdate.setEnabled(false);
        bulkUpdate.addClickListener(event -> popupView.setPopupVisible(true));
    }

    private void setComponentsSize() {
        String toolBarButtonsSize = "142px";

        // Toolbar buttons
        addHotel.setWidth(toolBarButtonsSize);
        deleteHotel.setWidth(toolBarButtonsSize);
        editHotel.setWidth(toolBarButtonsSize);
        bulkUpdate.setWidth(toolBarButtonsSize);
    }


    private void deleteSelected() {
        for (Hotel hotel : hotelGrid.getSelectedItems()) {
            hotelService.delete(hotel);
        }
        updateHotelList();
    }


    public List<Hotel> getSelected() {
        return new ArrayList<>(hotelGrid.getSelectedItems());
    }


    private void selectionCheck() {
        int selectedRowsNumber = hotelGrid.getSelectedItems().size();
        if(hotelEditForm.isVisible() || popupView.isPopupVisible()) {
            hotelEditForm.setVisible(false);
            popupView.setVisible(false);
        }
        addHotel.setEnabled(selectedRowsNumber == 0);
        editHotel.setEnabled(selectedRowsNumber == 1);
        deleteHotel.setEnabled(selectedRowsNumber > 0);
        bulkUpdate.setEnabled(selectedRowsNumber > 1);
    }


    public void updateHotelList() {
        List<Hotel> hotels = hotelService.findByNameAndAddress(filterByName.getValue(), filterByAddress.getValue());
        hotelGrid.setItems(hotels);
    }


}
