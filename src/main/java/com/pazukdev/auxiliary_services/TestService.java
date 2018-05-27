package com.pazukdev.auxiliary_services;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.Random;

// To run test run class
// By default test adds 3 new hotel categories and 4 new hotels
// To change number of new items edit invocation of addCategory(int quantityOfNewCategories)
// and addHotel(int quantityOfNewHotels) methods (change arguments)

public class TestService {
    private static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {

        prepareBrowser();

        String baseUrl = "http://localhost:8080/vaadin-first-app-1.0-SNAPSHOT/#!Hotels";
        //String baseUrl = "https://pazukdevtestapp1.herokuapp.com/#!Hotels";

        driver.get(baseUrl); // launch browser and open address page

        System.out.println("Test started");

        testScenario1(); // categories add test
        testScenario2(); // hotels add test

        System.out.println("Test finished");

        //close browser //driver.close();
    }


    private static void prepareBrowser() {
        //System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
        //driver = new FirefoxDriver();
        //to use FireFox comment the 4 lines below and uncomment 2 lines above
        System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
    }


    // categories add test
    private static void testScenario1() throws InterruptedException {
        System.out.println("Test scenario#1 started");

        Thread.sleep(2000);

        WebElement categoryMenu = driver.findElement(By.xpath(
                "//span[@class='v-menubar-menuitem'][contains(././span/text(), 'Categories')]"));

        Thread.sleep(2000);

        categoryMenu.click();

        Thread.sleep(2000);

        WebElement addCategoryButton = driver.findElement(By.id("addCategoryButton"));

        Thread.sleep(2000);

        addCategoryButton.click();

        Thread.sleep(2000);

        WebElement cancelButton = driver.findElement(By.id("cancelButton"));

        Thread.sleep(2000);

        cancelButton.click();

        Thread.sleep(2000);

        addCategory(3);

        Thread.sleep(2000);

        System.out.println("Test scenario#1 finished");
    }


    // hotels add test
    private static void testScenario2() throws InterruptedException {
        System.out.println("Test scenario#2 started");

        Thread.sleep(2000);

        WebElement hotelMenu = driver.findElement(By.xpath(
                "//span[@class='v-menubar-menuitem'][contains(././span/text(), 'Hotels')]"));

        Thread.sleep(2000);

        hotelMenu.click();

        Thread.sleep(2000);

        WebElement addHotelButton = driver.findElement(By.id("addHotelButton"));

        Thread.sleep(2000);

        addHotelButton.click();

        Thread.sleep(2000);

        WebElement cancelButton = driver.findElement(By.id("cancelButton"));

        Thread.sleep(2000);

        cancelButton.click();

        Thread.sleep(2000);

        addHotel(4);

        Thread.sleep(2000);

        System.out.println("Test scenario#2 finished");
    }


    private static void addCategory(int quantityOfNewCategories) throws InterruptedException {
        for(int i = 1; i <= quantityOfNewCategories; i++) {
            WebElement addCategoryButton = driver.findElement(By.id("addCategoryButton"));

            Thread.sleep(2000);

            addCategoryButton.click();

            Thread.sleep(2000);

            WebElement categoryNameField = driver.findElement(By.id("categoryNameTextField"));

            Thread.sleep(2000);

            categoryNameField.sendKeys("0 New test category " + i);

            Thread.sleep(2000);

            WebElement updateButton = driver.findElement(By.id("updateButton"));

            Thread.sleep(2000);

            updateButton.click();

            Thread.sleep(2000);
        }
    }


    private static void addHotel(int quantityOfNewHotels) throws InterruptedException {
        for(int i = 1; i <= quantityOfNewHotels; i++) {

            Random random = new Random();

            boolean iteratorIsEven = (i % 2) == 0;

            Thread.sleep(2000);

            WebElement addHotelButton = driver.findElement(By.id("addHotelButton"));
            addHotelButton.click();

            Thread.sleep(2000);

            WebElement hotelNameField = driver.findElement(By.id("hotelNameTextField"));

            Thread.sleep(2000);

            hotelNameField.sendKeys("0 New test hotel " + i);

            Thread.sleep(2000);

            WebElement hotelAddressField = driver.findElement(By.id("hotelAddressTextField"));

            Thread.sleep(2000);

            hotelAddressField.sendKeys("New address " + i);

            Thread.sleep(2000);

            WebElement hotelRatingField = driver.findElement(By.id("hotelRatingTextField"));

            Thread.sleep(2000);

            if(i == 1) {
                hotelRatingField.sendKeys("bla-bla");

                Thread.sleep(2000);

                hotelRatingField.clear();

                Thread.sleep(2000);

                hotelRatingField.sendKeys("6");

                Thread.sleep(2000);

                hotelRatingField.clear();

                Thread.sleep(2000);

                hotelRatingField.sendKeys("-1");

                Thread.sleep(2000);

                hotelRatingField.clear();

                Thread.sleep(2000);
            }

            hotelRatingField.sendKeys(String.valueOf(random.nextInt(6)));

            Thread.sleep(2000);

            WebElement hotelOperatesFromDateTextField = driver.findElement(By.xpath("//input[@class='v-textfield v-datefield-textfield']"));

            Thread.sleep(2000);

            hotelOperatesFromDateTextField.clear();

            Thread.sleep(2000);

            if(i == 1) {
                hotelOperatesFromDateTextField.sendKeys("bla-bla");

                Thread.sleep(2000);

                hotelOperatesFromDateTextField.clear();

                Thread.sleep(2000);

                hotelOperatesFromDateTextField.sendKeys("01.04.2025");

                Thread.sleep(2000);

                hotelOperatesFromDateTextField.clear();

                Thread.sleep(2000);
            }
            int day = random.nextInt(28) + 1;
            int month = random.nextInt(12) + 1;
            int year = random.nextInt(118) + 1900;
            hotelOperatesFromDateTextField.sendKeys(day + "." + month + "." + year);

            Thread.sleep(2000);

            Select select = new Select(driver.findElement(By.xpath("//select[@class='v-select-select']")));

            Thread.sleep(2000);

            select.selectByIndex(random.nextInt(select.getOptions().size() - 1) + 1);

            Thread.sleep(2000);

            if(iteratorIsEven) {
                WebElement hotelDescriptionTextField = driver.findElement(By.id("hotelDescriptionTextArea"));

                Thread.sleep(2000);

                hotelDescriptionTextField.sendKeys("Some description of New test hotel " + i);

                Thread.sleep(2000);
            }

            WebElement hotelUrlField = driver.findElement(By.id("hotelUrlTextField"));

            Thread.sleep(2000);

            if(i == 1) {
                hotelUrlField.sendKeys("wrongurl");

                Thread.sleep(2000);

                hotelUrlField.clear();

                Thread.sleep(2000);

                hotelUrlField.sendKeys("http://");
            }

            if(random.nextInt(10) == 0) hotelUrlField.sendKeys("sovietboxers.com");
            else hotelUrlField.sendKeys("hotelwebpage.com");

            Thread.sleep(2000);

            if(!iteratorIsEven) {
                WebElement radioButton = driver.findElement(By.xpath("//span[@class='v-radiobutton v-select-option']"));

                Thread.sleep(2000);

                radioButton.click();

                WebElement paymentValueField = driver.findElement(By.id("paymentValueTextField"));

                Thread.sleep(2000);

                if(i == 1) {
                    paymentValueField.sendKeys("bla-bla");

                    Thread.sleep(2000);

                    paymentValueField.clear();

                    Thread.sleep(2000);

                    paymentValueField.sendKeys("101");

                    Thread.sleep(2000);

                    paymentValueField.clear();

                    Thread.sleep(2000);

                    paymentValueField.sendKeys("0");

                    Thread.sleep(2000);

                    paymentValueField.clear();

                    Thread.sleep(2000);
                }

                paymentValueField.sendKeys(String.valueOf((random.nextInt(10)+1)*10));

                Thread.sleep(2000);
            }

            WebElement updateButton = driver.findElement(By.id("updateButton"));

            Thread.sleep(2000);

            updateButton.click();

            Thread.sleep(2000);
        }
    }




}