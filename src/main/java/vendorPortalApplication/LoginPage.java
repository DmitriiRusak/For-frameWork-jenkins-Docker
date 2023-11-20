package vendorPortalApplication;

import flight_reservation_app.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends AbstractPage {

    @FindBy(xpath = "//input[@id='username']")private WebElement userNameInput;
    @FindBy(xpath = "//input[@id='password']")private WebElement userPasswordInput;
    @FindBy(xpath = "//a[@id='login']")private WebElement loginButton;


    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void goTo(String url){
        driver.get(url);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(userNameInput));
        return userNameInput.isDisplayed();
    }

    public void login(String userName, String password){
        this.userNameInput.sendKeys(userName);
        this.userPasswordInput.sendKeys(password);
        loginButton.click();
    }
}
