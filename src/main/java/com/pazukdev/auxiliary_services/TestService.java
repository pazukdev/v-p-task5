package com.pazukdev.auxiliary_services;

// to run test run class

// to select browser for run test use runDemo() method with arguments "Chrome", "FireFox", "IE"

// to change using of downloading from remote repository webdriver to webdriver on local machine
// change runDemo() method argument to "local". By default it is chromedriver at toot of disk C ("C:\\chromedriver.exe")



public class TestService {

    public static void main(String[] args) throws InterruptedException {
        DemoService demoService = new DemoService();
        demoService.runDemo("Chrome");
    }

}