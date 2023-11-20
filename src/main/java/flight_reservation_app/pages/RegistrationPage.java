package flight_reservation_app.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegistrationPage extends AbstractPage {

    @FindBy(xpath = "//input[@id='firstName']")
    private WebElement firstNameInput;

    @FindBy(xpath = "//input[@id='lastName']")
    private WebElement lastNameInput;

    @FindBy(xpath = "//input[@id='email']")
    private WebElement emailInput;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement passwordInput;

    @FindBy(xpath = "//input[@name='street']")
    private WebElement streetInput;

    @FindBy(xpath = "//input[@name='city']")
    private WebElement cityInpyt;

    @FindBy(xpath = "//input[@name='zip']")
    private WebElement zipInput;

    @FindBy(xpath = "//button[@id='register-btn']")
    private WebElement buttonRegistry;

    public RegistrationPage(WebDriver driver){
       super(driver);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        return firstNameInput.isDisplayed();
    }

    public void goTo(String url){
        driver.get(url);
    }

    public void enterUserDetails(String userFirstName, String userLastaname){
        firstNameInput.sendKeys(userFirstName);
        lastNameInput.sendKeys(userLastaname);
    }

    public void enterUserCredentials(String email, String password){
        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
    }

    public void enterAddress(String street, String city, String zip){
        streetInput.sendKeys(street);
        cityInpyt.sendKeys(city);
        zipInput.sendKeys(zip);
    }

    public void clickRegisterButton(){
        buttonRegistry.click();
    }
}
