package com.pazukdev.auxiliary_services;

import com.pazukdev.ui.MainUI;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.support.ui.Select;
import java.util.Random;

// To run test run class
// By default test adds 3 new hotel categories and 4 new hotels
// To change number of new items edit invocation of addCategory(int quantityOfNewCategories)
// and addHotel(int quantityOfNewHotels) methods (change arguments)

public class DemoService {
    private static WebDriver driver;

    static Thread demoThread;

    private MainUI mainUI = new MainUI();

    public boolean isWorking = false;


    public void runDemo(String browser) {
        try {
            demoThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        System.out.println("Demo thread runned");
                        isWorking = true;
                        executeTasks(browser);
                        System.out.println("Demo thread run() finished");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        System.out.println("Demo thread was interrupted");
                    }
                }
            });
            demoThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            if(demoThread != null) {
                demoThread.interrupt();
                System.out.println("Demo thread was interrupted");
            }
        }
    }

    public static void stopDemo() {
        demoThread.interrupt();
    }


    private void executeTasks(String browser) throws InterruptedException {
        prepareBrowser(browser);

        String baseUrl = "http://localhost:8080/vaadin-first-app-1.0-SNAPSHOT/#!Hotels";
        //String baseUrl = "https://pazukdevtestapp1.herokuapp.com/#!Hotels";

        driver.get(baseUrl); // launch browser and open address page
        driver.manage().window().maximize();

        Thread.sleep(2000);

        System.out.println("Test started");

        testFiltersScenario(); // filters work test
        testAddCategoriesScenario(); // categories add test
        testAddHotelsScenario(browser); // hotels add test

        System.out.println("Test finished");

        //close browser //driver.close();
    }

    private void prepareBrowser(String browser) {
        // get webdrivers with absolute path to webdriver.exe on local machine
        // Mozilla FireFox
        //System.setProperty("webdriver.firefox.marionette","C:\\geckodriver.exe");
        //driver = new FirefoxDriver();
        // Chrome
        //System.setProperty("webdriver.chrome.driver","C:\\chromedriver.exe");
        //driver = new ChromeDriver();

        // get last version of required webdriver.exe with WebDriver Manager
        if(browser.equals("Chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }

        if(browser.equals("FireFox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }

        if(browser.equals("IE")) {
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();
        }
    }


    // filters work test
    private void testFiltersScenario() throws InterruptedException {
        System.out.println("Test filters scenario started");

        Thread.sleep(2000);

        WebElement filterByNameField = driver.findElement(By.id("filterHotelsByNameTextField"));

        filterByNameField.sendKeys("guesthouse");

        Thread.sleep(3000);

        WebElement filterByAddressField = driver.findElement(By.id("filterHotelsByAddressTextField"));

        filterByAddressField.sendKeys("luang");

        Thread.sleep(4000);

        filterByAddressField.clear();

        Thread.sleep(2000);

        filterByNameField.clear();

        Thread.sleep(2000);

        System.out.println("Test filters scenario finished");
    }


    // categories add test
    private void testAddCategoriesScenario() throws InterruptedException {
        System.out.println("Test add categories scenario started");

        addCategory(3);

        Thread.sleep(2000);

        System.out.println("Test add categories scenario finished");
    }


    // hotels add test
    private void testAddHotelsScenario(String browser) throws InterruptedException {
        System.out.println("Test add hotels scenario started");

        addHotel(4, browser);

        Thread.sleep(2000);

        System.out.println("Test add hotels scenario finished");
    }


    private void addCategory(int quantityOfNewCategories) throws InterruptedException {
        for(int i = 1; i <= quantityOfNewCategories; i++) {

            Thread.sleep(2000);

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


    private void addHotel(int quantityOfNewHotels, String browser) throws InterruptedException {
        for(int i = 1; i <= quantityOfNewHotels; i++) {

            Random random = new Random();

            boolean iteratorIsEven = (i % 2) == 0;

            Thread.sleep(2000);

            WebElement addHotelButton = driver.findElement(By.id("addHotelButton"));

            Thread.sleep(2000);

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

            Select select = new Select(driver.findElement(By.xpath("(//select[@class='v-select-select'])[2]")));

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
                if(browser.equals("IE")) hotelUrlField.clear();

                hotelUrlField.sendKeys("wrongurl");

                Thread.sleep(2000);

                hotelUrlField.clear();

                Thread.sleep(2000);

                hotelUrlField.sendKeys("http://");
            }

            if(browser.equals("IE")) hotelUrlField.clear();
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