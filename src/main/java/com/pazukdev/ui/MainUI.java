package com.pazukdev.ui;

import javax.servlet.annotation.WebListener;
import javax.servlet.annotation.WebServlet;

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

    MenuBar menu = new MenuBar();
    MenuBar.MenuItem hotelItem;
    MenuBar.MenuItem categoryItem;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();

        setContent(layout);

        HorizontalLayout content = new HorizontalLayout();
        content.setSizeFull();

        layout.addComponents(menu, content);
        layout.setExpandRatio(content, 1f);

        final Navigator navigator = new Navigator(this, content);

        MenuBar.Command command1 = new MenuBar.Command() {
            @Override
            public void menuSelected(MenuBar.MenuItem selectedItem) {

                navigator.removeView("Hotels");
                navigator.addView("Hotels", new HotelForm());
                navigator.navigateTo("Hotels");

                Page.getCurrent().setTitle("Hotels");

                hotelItem.setEnabled(false);
                categoryItem.setEnabled(true);

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

            }
        };

        hotelItem = menu.addItem("Hotels", VaadinIcons.BUILDING, command1);
        hotelItem.setEnabled(false);
        categoryItem = menu.addItem("Categories", VaadinIcons.ACADEMY_CAP, command2);
        menu.setStyleName(ValoTheme.MENUBAR_BORDERLESS);

        navigator.addView("Hotels", new HotelForm());
        navigator.navigateTo("Hotels");
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends SpringVaadinServlet {
    }
}