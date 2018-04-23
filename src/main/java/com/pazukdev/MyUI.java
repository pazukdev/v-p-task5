package com.pazukdev;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.themes.ValoTheme;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    private VerticalLayout mainLayout = new VerticalLayout();
    private VerticalLayout hotelLayout = new VerticalLayout();
    private VerticalLayout categoryLayout = new VerticalLayout();

    private HotelService hotelService =HotelService.getInstance();
    private CategoryService categoryService=CategoryService.getInstance();

    private Grid<Hotel> hotelGrid =new Grid<>(Hotel.class);
    private Grid<Category> categoryGrid =new Grid<>(Category.class);

    private TextField filterByName =new TextField();
    private TextField filterByAddress=new TextField();

    private Button addHotel= new Button("Add hotel");
    private Button deleteHotel = new Button("Delete hotel");
    private Button editHotel = new Button("Edit hotel");

    private Button addCategory= new Button("Add category");
    private Button deleteCategory = new Button("Delete category");
    private Button editCategory = new Button("Edit category");

    private MenuBar menu = new MenuBar();

    private HotelEditForm hotelForm = new HotelEditForm(this);
    private CategoryEditForm categoryForm = new CategoryEditForm(this);

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        MenuBar.Command command1 = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                categoryLayout.setVisible(false);
                hotelLayout.setVisible(true);
                categoryForm.setVisible(false);
                updateHotelList();
            }
        };

        MenuBar.Command command2 = new MenuBar.Command() {

            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {
                hotelLayout.setVisible(false);
                categoryLayout.setVisible(true);
                hotelForm.setVisible(false);
                updateCategoryList();
            }
        };

        MenuBar.MenuItem hotelItem = menu.addItem("Hotel", VaadinIcons.BUILDING, command1);
        MenuBar.MenuItem categoryItem = menu.addItem("Category", VaadinIcons.ACADEMY_CAP, command2);

        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);




        hotelGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        hotelGrid.setWidth("1000px");
        hotelGrid.setHeight("516px");
        hotelGrid.setBodyRowHeight(30);
        hotelGrid.getColumn("name").setWidth(260);
        hotelGrid.sort(hotelGrid.getColumn("name"), SortDirection.ASCENDING);
        hotelGrid.getColumn("address").setWidth(260);
        hotelGrid.getColumn("description").setExpandRatio(1);
        hotelGrid.addItemClickListener(event -> Notification.show(event.getItem().toString()));

        filterByName.setPlaceholder("filter by name");
        filterByName.addValueChangeListener(e -> updateList("by name"));
        filterByName.setValueChangeMode(ValueChangeMode.LAZY);

        filterByAddress.setPlaceholder("filter by address");
        filterByAddress.addValueChangeListener(e -> updateList("by address"));
        filterByAddress.setValueChangeMode(ValueChangeMode.LAZY);



        addHotel.addClickListener(event -> {
            hotelGrid.asMultiSelect().clear();
            LocalDate yesterday=LocalDate.now().minusDays(1L);
            hotelForm.editHotel(new Hotel(yesterday));
        });

        deleteHotel.setEnabled(false);
        deleteHotel.addClickListener(event -> {
            Set<Hotel> deleteCandidates=hotelGrid.getSelectedItems();
            for(Hotel hotel : deleteCandidates) {
                hotelService.delete(hotel);
            }
            updateHotelList();
        });

        editHotel.setEnabled(false);
        editHotel.addClickListener(event -> {
            hotelForm.editHotel(hotelGrid.getSelectedItems().iterator().next());
        });

        HorizontalLayout hotelToolbar = new HorizontalLayout(filterByName, filterByAddress, addHotel,
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

        HorizontalLayout mainHotel = new HorizontalLayout(hotelGrid, hotelForm);

        hotelGrid.asMultiSelect().addSelectionListener(event -> {
            if(event.getValue().isEmpty()) {
                deleteHotel.setEnabled(false);
                editHotel.setEnabled(false);
                hotelForm.setVisible(false);
            }
            else if(event.getValue().size()==1) {
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(true);
            }
            else if(event.getValue().size()>1){
                deleteHotel.setEnabled(true);
                editHotel.setEnabled(false);
                hotelForm.setVisible(false);
            }
        });

        hotelForm.setVisible(false);







        categoryGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        categoryGrid.sort(categoryGrid.getColumn("name"), SortDirection.ASCENDING);

        addCategory.addClickListener(event -> {
            categoryGrid.asMultiSelect().clear();
            categoryForm.editCategory(new Category());
        });

        deleteCategory.setEnabled(false);
        deleteCategory.addClickListener(event -> {
            Set<Category> deleteCandidates=categoryGrid.getSelectedItems();
            for(Category category : deleteCandidates) {
                List<Hotel> hotelsList = hotelService.findAll(category.toString(), "by category");
                for(Hotel hotel : hotelsList) {
                    hotel.setCategory(CategoryService.getNullCategory());
                    hotelService.save(hotel);
                }
                categoryService.delete(category);
            }
            updateCategoryList();
            updateHotelList();
        });

        editCategory.setEnabled(false);
        editCategory.addClickListener(event -> {
            categoryForm.editCategory(categoryGrid.getSelectedItems().iterator().next());
        });

        HorizontalLayout categoryToolbar = new HorizontalLayout(addCategory, deleteCategory, editCategory);

        categoryGrid.setColumns(
                //"id",
                "name"
        );



        HorizontalLayout mainCategory = new HorizontalLayout(categoryGrid, categoryForm);

        categoryGrid.asMultiSelect().addSelectionListener(event -> {
            if(event.getValue().isEmpty()) {
                deleteCategory.setEnabled(false);
                editCategory.setEnabled(false);
                categoryForm.setVisible(false);
            }
            else if(event.getValue().size()==1) {
                deleteCategory.setEnabled(true);
                editCategory.setEnabled(true);
            }
            else if(event.getValue().size()>1){
                deleteCategory.setEnabled(true);
                editCategory.setEnabled(false);
                categoryForm.setVisible(false);
            }
        });

        categoryForm.setVisible(false);


        mainLayout.addComponents(menu, hotelLayout, categoryLayout);
        hotelLayout.addComponents(hotelToolbar, mainHotel);
        categoryLayout.addComponents(categoryToolbar, mainCategory);
        categoryLayout.setVisible(false);

        updateHotelList();
        updateCategoryList();

        setContent(mainLayout);

    }

    public void updateHotelList() {
        List<Hotel> hotels = hotelService.findAll(filterByName.getValue());
        hotelGrid.setItems(hotels);
    }

    public void updateList(String filter) {
        List<Hotel> hotels=null;
        switch(filter) {
            case "by name":
                hotels = hotelService.findAll(filterByName.getValue(), filter);
                break;
            case "by address":
                hotels = hotelService.findAll(filterByAddress.getValue(), filter);
                break;

        }
        if(hotels!=null) hotelGrid.setItems(hotels);
    }

    public void updateCategoryList() {
        List<Category> categories = categoryService.findAll();
        categoryGrid.setItems(categories);
    }



    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
