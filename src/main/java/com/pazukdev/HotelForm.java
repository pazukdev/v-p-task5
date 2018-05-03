package com.pazukdev;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;


import java.time.LocalDate;
import java.util.List;


@Theme("mytheme")
public class HotelForm extends FormLayout implements View {
    private HotelService hotelService =HotelService.getInstance();

    private Grid<Hotel> hotelGrid =new Grid<>(Hotel.class);

    private HorizontalLayout hotelMainLayout;
    private HorizontalLayout hotelToolbar;

    private TextField filterByName =new TextField();
    private TextField filterByAddress=new TextField();

    private Button addHotel= new Button("Add hotel");
    private Button deleteHotel = new Button("Delete hotel");
    private Button editHotel = new Button("Edit hotel");

    private HotelEditForm hotelEditForm = new HotelEditForm(this);

    private String hotelToolbarElementsWidth;


    public HotelForm() {
        //Components init and settings
        setGrid();
        setFilters();
        setButtons();
        setLayouts();

        updateHotelList();

        addComponents(hotelToolbar, hotelMainLayout);
    }


    private void setGrid() {
        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        hotelGrid.setWidth("1000px");
        hotelGrid.setHeight("516px");
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
            HotelCategory category = CategoryService.getInstance().findById(hotel.getCategoryID());
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
        filterByName.setWidth(hotelToolbarElementsWidth);

        // Filter by address
        filterByAddress.setPlaceholder("filter by address");
        filterByAddress.addValueChangeListener(e -> updateHotelList());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);
        filterByAddress.setWidth(hotelToolbarElementsWidth);

    }


    private void setLayouts() {
        // Toolbar
        hotelToolbar = new HorizontalLayout(filterByName, filterByAddress, addHotel,
                deleteHotel, editHotel);
        hotelToolbarElementsWidth = "190px";

        // Hotel edit form
        hotelEditForm.setVisible(false);

        // Main layout
        hotelMainLayout = new HorizontalLayout();
        hotelMainLayout.addComponents(hotelGrid, hotelEditForm);
    }


    private void setButtons() {
        //Add button
        addHotel.setWidth(hotelToolbarElementsWidth);
        addHotel.addClickListener(event -> {
            hotelGrid.asMultiSelect().clear();
            if(hotelEditForm.isVisible()) hotelEditForm.setVisible(false);
            LocalDate yesterday=LocalDate.now().minusDays(1L);
            hotelEditForm.editHotel(new Hotel());
        });

        // Delete button
        editHotel.setWidth(hotelToolbarElementsWidth);
        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(event -> deleteSelected());

        // Edit button
        deleteHotel.setWidth(hotelToolbarElementsWidth);
        editHotel.setEnabled(false);
        editHotel.addClickListener(event -> {
            hotelEditForm.editHotel(hotelGrid.getSelectedItems().iterator().next());
        });
    }


    private void deleteSelected() {
        for (Hotel hotel : hotelGrid.getSelectedItems()) {
            hotelService.delete(hotel);
        }
        updateHotelList();
    }


    private void selectionCheck() {
        int selectedRowsNumber = hotelGrid.getSelectedItems().size();
        if(hotelEditForm.isVisible()) {
            hotelEditForm.setVisible(false);
        }
        editHotel.setEnabled(selectedRowsNumber == 1);
        deleteHotel.setEnabled(selectedRowsNumber > 0);
    }


    public void updateHotelList() {
        List<Hotel> hotels = hotelService.findByNameAndAddress(filterByName.getValue(), filterByAddress.getValue());
        hotelGrid.setItems(hotels);
    }


}
