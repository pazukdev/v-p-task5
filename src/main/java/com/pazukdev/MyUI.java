package com.pazukdev;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private VerticalLayout layout = new VerticalLayout();
    private HotelService service=HotelService.getInstance();
    private Grid<Hotel> hotelGrid =new Grid<>(Hotel.class);

    private TextField filterByName =new TextField();
    private TextField filterByAddress=new TextField();

    private Button addHotel= new Button("Add hotel");
    private Button deleteHotel = new Button("Delete hotel");
    private Button editHotel = new Button("Edit hotel");

    private hotelEditForm form = new hotelEditForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        hotelGrid.setWidth("1000px");
        hotelGrid.setHeight("516px");
        hotelGrid.setBodyRowHeight(30);
        hotelGrid.getColumn("name").setWidth(260);
        hotelGrid.sort(hotelGrid.getColumn("name"), SortDirection.ASCENDING);
        hotelGrid.getColumn("address").setWidth(260);
        hotelGrid.getColumn("description").setExpandRatio(1);

        filterByName.setPlaceholder("filter by name");
        filterByName.addValueChangeListener(e -> updateList("by name"));
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        filterByAddress.setPlaceholder("filter by address");
        filterByAddress.addValueChangeListener(e -> updateList("by address"));
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);



        addHotel.addClickListener(event -> {
            hotelGrid.asSingleSelect().clear();
            form.editHotel(new Hotel());
        });

        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(event -> {

            /*Hotel delCandidate = hotelGrid.getSelectedItems().iterator().next();
            service.delete(delCandidate);
            deleteHotel.setEnabled(false);
            hotelGrid.asSingleSelect().clear();*/
            updateList();
            form.setVisible(false);
        });

        editHotel.setEnabled(false);
        editHotel.addClickListener(event -> {
            form.editHotel(hotelGrid.getSelectedItems().iterator().next());
        });


        HorizontalLayout toolbar = new HorizontalLayout(filterByName, filterByAddress, addHotel,
                deleteHotel, editHotel);

        hotelGrid.setColumns(
                //"id",
                "name",
                "address",
                "rating",
                "operatesFrom",
                "category",
                "description"
                //"url"
        );

        Grid.Column<Hotel, String> urlColumn = hotelGrid.addColumn(hotel ->
                "<a target=\"_blank\" href='" + hotel.getUrl() + "'>" + hotel.getUrl() + "</a>", new HtmlRenderer())
                .setCaption("URL").setWidth(260);

        HorizontalLayout main = new HorizontalLayout(hotelGrid, form);

        /*hotelGrid.asSingleSelect().addValueChangeListener(event -> {
            deleteHotel.setEnabled(true);
            editHotel.setEnabled(true);
        });*/

        hotelGrid.asMultiSelect().addSelectionListener(event -> {
            if(event.getValue().isEmpty()) {
                deleteHotel.setEnabled(false);
                editHotel.setEnabled(false);
                form.setVisible(false);
            }
            else if(event.getValue().size()==1) {
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(true);
            }
            else if(event.getValue().size()>1){
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(false);
                form.setVisible(false);
            }
        });

        form.setVisible(false);

        layout.addComponents(toolbar, main);

        updateList();
        setContent(layout);
    }

    public void updateList() {
        List<Hotel> hotels = service.findAll(filterByName.getValue());
        hotelGrid.setItems(hotels);
    }

    public void updateList(String filter) {
        List<Hotel> hotels=null;
        switch(filter) {
            case "by name":
                hotels = service.findAll(filterByName.getValue(), "by name");
                break;
            case "by address":
                hotels = service.findAll(filterByAddress.getValue(), "by address");
                break;

        }
        if(hotels!=null) hotelGrid.setItems(hotels);
    }



    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
