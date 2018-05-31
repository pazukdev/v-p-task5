package com.pazukdev.ui;

import com.pazukdev.entities.Category;
import com.pazukdev.entities.Hotel;
import com.pazukdev.services.CategoryService;
import com.pazukdev.services.HotelService;
import com.pazukdev.services.UIComponentsService;
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
    public Button bulkUpdate = new Button("Bulk Update");

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
        setSizeFull();
        setMargin(false);
    }


    private void setGrid() {
        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        hotelGrid.setWidth(UIComponentsService.hotelGridWidth + "px");
        hotelGrid.setHeight("526px");
        hotelGrid.setBodyRowHeight(34);
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

        Grid.Column<Hotel, String> paymentColumn = hotelGrid.addColumn(hotel -> {
            if(hotel.getPayment() != null) return hotel.getPayment().toString();
            else return "Without prepayment";
        }).setCaption("Payment").setWidth(190);

    }


    private void setFilters() {
        // Filter by name
        filterByName.setId("filterHotelsByNameTextField");
        filterByName.setPlaceholder("Filter by name");
        filterByName.addValueChangeListener(e -> updateHotelList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        // Filter by address
        filterByAddress.setId("filterHotelsByAddressTextField");
        filterByAddress.setPlaceholder("Filter by address");
        filterByAddress.addValueChangeListener(e -> updateHotelList());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
    }


    private void setLayoutsAndForms() {
        // Tool bar
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
        addHotel.setId("addHotelButton");
        addHotel.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        addHotel.setDescription("Create a new hotel");
        addHotel.addClickListener(event -> {
            if(hotelEditForm.isVisible()) hotelEditForm.setVisible(false);
            hotelEditForm.editHotel(new Hotel());
        });

        // Delete button
        deleteHotel.setId("deleteHotelButton");
        deleteHotel.setEnabled(false);
        deleteHotel.setDescription("Delete selected hotels");
        deleteHotel.addClickListener(event -> deleteSelected());

        // Edit button
        editHotel.setId("editHotelButton");
        editHotel.setEnabled(false);
        editHotel.setDescription("Edit selected hotel");
        editHotel.addClickListener(event -> hotelEditForm.editHotel(getSelected().get(0)));

        // Bulk Update button
        bulkUpdate.setId("bulkUpdateHotelsButton");
        bulkUpdate.setEnabled(false);
        bulkUpdate.setDescription("Update parameters of 2 or more selected hotels");
        bulkUpdate.addClickListener(event -> popupView.setPopupVisible(true));
    }

    private void setComponentsSize() {
        //String toolBarButtonsSize = "142px";
        String toolBarButtonsSize = UIComponentsService.hotelFormButtonsWidth + "px";

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
        addHotel.setEnabled(selectedRowsNumber == 0);
        editHotel.setEnabled(selectedRowsNumber == 1);
        deleteHotel.setEnabled(selectedRowsNumber > 0);
        bulkUpdate.setEnabled(selectedRowsNumber > 1);

        hotelEditForm.setVisible(false);
    }


    public void updateHotelList() {
        List<Hotel> hotels = hotelService.findByNameAndAddress(filterByName.getValue(), filterByAddress.getValue());
        hotelGrid.setItems(hotels);
    }


}
