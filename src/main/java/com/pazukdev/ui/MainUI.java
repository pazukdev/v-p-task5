package com.pazukdev.ui;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;
import com.pazukdev.auxiliary_services.DemoService;
import com.pazukdev.services.UIComponentsService;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.EnableVaadin;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ContextLoaderListener;

@Theme("mytheme")
@Title("Hotels")
@SuppressWarnings("serial")
@SpringUI
public class MainUI extends UI {

    @WebListener
    public static class MyContextLoaderListener extends ContextLoaderListener {
    }

    @Configuration
    @EnableVaadin
    public static class MyConfiguration {
    }


    private Navigator navigator;

    private VerticalLayout layout = new VerticalLayout();
    private HorizontalLayout menuBar = new HorizontalLayout();
    private HorizontalLayout buttonBar = new HorizontalLayout();
    private HorizontalLayout content = new HorizontalLayout();

    private NativeSelect<String> browserSelect = new NativeSelect<>();

    private Button demoPlay = new Button("Play demo");
    //private Button demoStop = new Button("Stop"); // - is not using at the present time

    MenuBar menu = new MenuBar();
    MenuBar.MenuItem hotelItem;
    MenuBar.MenuItem categoryItem;

    DemoService demoService;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout.setSizeFull();
        menuBar.setWidth(UIComponentsService.hotelGridWidth + 20 + "px");
        content.setSizeFull();

        setNativeSelect();
        setButtons();
        setMenu();

        buttonBar.addComponents(browserSelect, demoPlay);
        menuBar.addComponents(menu, buttonBar);
        menuBar.setComponentAlignment(buttonBar, Alignment.MIDDLE_RIGHT);
        layout.addComponents(menuBar, content);
        layout.setExpandRatio(content, 1f);
        setContent(layout);

        navigator = new Navigator(this, content);
        navigator.addView("Hotels", new HotelForm());
        navigator.navigateTo("Hotels");
    }


    private void setNativeSelect() {
        browserSelect.setItems("Chrome", "FireFox", "IE");
        browserSelect.setWidth(UIComponentsService.hotelFormButtonsWidth + "px");
        browserSelect.setDescription("Select browser in what demonstration of website functionality will run."
                + " The browser should be installed on your machine");
        browserSelect.addSelectionListener(event -> {
            demoPlay.setEnabled(true);
            if(browserSelect.getValue() == null) demoPlay.setEnabled(false);
        });
    }


    private void setButtons() {
        String buttonWidth = UIComponentsService.hotelFormButtonsWidth + "px";

        // Demo Play button
        demoPlay.setId("demoPlayButton");
        demoPlay.setStyleName(ValoTheme.BUTTON_FRIENDLY);
        demoPlay.setDescription("Runs demo. Demo runs in a new session of selected browser");
        demoPlay.addClickListener(event -> {
            demoService = new DemoService();
            demoService.runDemo(browserSelect.getValue());

        });
        demoPlay.setWidth(buttonWidth);
        demoPlay.setEnabled(false);

        // Demo Stop button - is not using at the present time
        /*demoStop.setId("demoStopButton");
        demoStop.setStyleName(ValoTheme.BUTTON_DANGER);
        demoStop.setDescription("Stops running demo");
        demoStop.addClickListener(event -> demoService.stopDemo());
        demoStop.setWidth(buttonWidth);
        demoStop.setVisible(false);*/
    }


    private void setMenu() {
        MenuBar.Command command1 = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                navigator.removeView("Hotels");
                navigator.addView("Hotels", new HotelForm());
                navigator.navigateTo("Hotels");

                Page.getCurrent().setTitle("Hotels");

                hotelItem.setEnabled(false);
                categoryItem.setEnabled(true);

                buttonBar.setVisible(true);
            }
        };

        MenuBar.Command command2 = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                navigator.removeView("Categories");
                navigator.addView("Categories", new CategoryForm());
                navigator.navigateTo("Categories");

                Page.getCurrent().setTitle("Categories");

                categoryItem.setEnabled(false);
                hotelItem.setEnabled(true);

                buttonBar.setVisible(false);
            }
        };

        hotelItem = menu.addItem("Hotels", VaadinIcons.BUILDING, command1);
        hotelItem.setDescription("Go to hotels page");
        hotelItem.setEnabled(false);
        categoryItem = menu.addItem("Categories", VaadinIcons.ACADEMY_CAP, command2);
        categoryItem.setDescription("Go to hotel categories page");

        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }

}