package tests;

import driverManager.DriverManagerTest;
import flight_reservation_app.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pojo_modells.VendorReservationPojo;
import utils.ConfigurationProperties;
import utils.Constants;
import utils.JsonUtil;

import java.io.IOException;

public class FlightReservationTest extends DriverManagerTest {

    private VendorReservationPojo vendorReservationPojo;

    @BeforeTest
    @Parameters("PathToDataInJson")//parameter is given from .xml file
    public void setUpFields(String PathToDataInJson) throws IOException {
        vendorReservationPojo = JsonUtil.getTestData(PathToDataInJson,VendorReservationPojo.class);
    }

    @Test
    public void userRegistrationTest(){

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.goTo(ConfigurationProperties.get(Constants.FLIGHT_RESERVATION_URL));
        Assert.assertTrue(registrationPage.isAt());
        registrationPage.enterUserDetails(vendorReservationPojo.userFirstName(), vendorReservationPojo.userLastaname());
        registrationPage.enterUserCredentials(vendorReservationPojo.email(),vendorReservationPojo.password());
        registrationPage.enterAddress(vendorReservationPojo.street(), vendorReservationPojo.city(), vendorReservationPojo.zip());
        registrationPage.clickRegisterButton();
    }

    @Test(dependsOnMethods = {"userRegistrationTest"})
    public void registrationConfirmation(){

        RegistrationConfirmationPage registrationConfirmationPage = new RegistrationConfirmationPage(driver);
        registrationConfirmationPage.isAt();
        Assert.assertEquals(registrationConfirmationPage.getUserName(), vendorReservationPojo.userFirstName());
        registrationConfirmationPage.pressGoToFlightSerchButton();
    }

    @Test(dependsOnMethods = {"registrationConfirmation"})
    public void flightSerchTest(){

        FlightSerchPage flightSerchPage = new FlightSerchPage(driver);
        Assert.assertTrue(flightSerchPage.isAt());
        flightSerchPage.selectPassengersNumber(vendorReservationPojo.nomberOfPassenjers());
        flightSerchPage.serchForFlight();
    }

    @Test(dependsOnMethods = {"flightSerchTest"})
    public void flightSelectionTest(){

        FlightSelectionPage flightSelectionPage = new FlightSelectionPage(driver);
        Assert.assertTrue(flightSelectionPage.isAt());
        flightSelectionPage.selectFlights();
        flightSelectionPage.confirmFlights();
    }

    @Test(dependsOnMethods = {"flightSelectionTest"})
    public void flightConfirmation(){
        FlightKonfirmationPage flightKonfirmationPage = new FlightKonfirmationPage(driver);
        Assert.assertTrue(flightKonfirmationPage.isAt());
        Assert.assertEquals(flightKonfirmationPage.getPrice(),vendorReservationPojo.price());
    }


}
