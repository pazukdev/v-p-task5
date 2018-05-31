package com.pazukdev.auxiliary_services;

// To run test run class
// By default test adds 3 new hotel categories and 4 new hotels
// To change number of new items edit invocation of addCategory(int quantityOfNewCategories)
// and addHotel(int quantityOfNewHotels) methods (change arguments)

public class TestService {

    public static void main(String[] args) throws InterruptedException {
        DemoService demoService = new DemoService();
        demoService.runDemo("Chrome");
    }

}