package com.pazukdev;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Theme("mytheme")
public class HotelForm extends FormLayout implements View {

    private HotelService hotelService =HotelService.getInstance();
    //private CategoryService categoryService = CategoryService.getInstance();

    private Grid<Hotel> hotelGrid =new Grid<>(Hotel.class);

    private TextField filterByName =new TextField();
    private TextField filterByAddress=new TextField();

    private Button addHotel= new Button("Add hotel");
    private Button deleteHotel = new Button("Delete hotel");
    private Button editHotel = new Button("Edit hotel");

    private HotelEditForm hotelEditForm = new HotelEditForm(this);


    public HotelForm() {

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


        filterByName.setPlaceholder("filter by name");
        filterByName.addValueChangeListener(e -> updateHotelList());
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        filterByAddress.setPlaceholder("filter by address");
        filterByAddress.addValueChangeListener(e -> updateHotelList());
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);



        addHotel.addClickListener(event -> {
            hotelGrid.asMultiSelect().clear();
            if(hotelEditForm.isVisible()) hotelEditForm.setVisible(false);
            LocalDate yesterday=LocalDate.now().minusDays(1L);
            hotelEditForm.editHotel(new Hotel(yesterday));
        });

        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(event -> deleteSelected());

        editHotel.setEnabled(false);
        editHotel.addClickListener(event -> {
            hotelEditForm.editHotel(hotelGrid.getSelectedItems().iterator().next());
        });

        HorizontalLayout hotelToolbar = new HorizontalLayout(filterByName, filterByAddress, addHotel,
                deleteHotel, editHotel);
        String hotelToolbarElementsWidth = "190px";
        filterByName.setWidth(hotelToolbarElementsWidth);
        filterByAddress.setWidth(hotelToolbarElementsWidth);
        addHotel.setWidth(hotelToolbarElementsWidth);
        deleteHotel.setWidth(hotelToolbarElementsWidth);
        editHotel.setWidth(hotelToolbarElementsWidth);

        hotelGrid.setColumns(
                //"id",
                "name",
                "address",
                "rating",
                "operatesFrom"
                //"categoryID",
                //"description"
                //"url"
        );


        Grid.Column<Hotel, String> categoryColumn = hotelGrid.addColumn(hotel -> {
            HotelCategory category = CategoryService.getInstance().findById(hotel.getCategoryID());
            if (category != null) {
                return category.getName();
            }
            return "[not found]";
        }).setCaption("Category").setMaximumWidth(260);

        Grid.Column<Hotel, String> urlColumn = hotelGrid.addColumn(hotel ->
                "<a target=\"_blank\" href='" + hotel.getUrl() + "'>" + hotel.getUrl() + "</a>", new HtmlRenderer())
                .setCaption("URL").setMaximumWidth(260);

        Grid.Column<Hotel, String> descriptionColumn = hotelGrid.addColumn(Hotel::getDescription)
                .setCaption("Description").setMaximumWidth(260);


        hotelEditForm.setVisible(false);

        updateHotelList();

        HorizontalLayout mainHotel = new HorizontalLayout();
        mainHotel.addComponents(hotelGrid, hotelEditForm);

        addComponents(hotelToolbar, mainHotel);

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
            hotelEditForm.setVisible(selectedRowsNumber == 1);
        }
        editHotel.setEnabled(selectedRowsNumber == 1);
        deleteHotel.setEnabled(selectedRowsNumber > 0);
    }

    public void updateHotelList() {
        List<Hotel> hotels = hotelService.findAllByNameAndAddress(filterByName.getValue(), filterByAddress.getValue());
        hotelGrid.setItems(hotels);
    }


}
