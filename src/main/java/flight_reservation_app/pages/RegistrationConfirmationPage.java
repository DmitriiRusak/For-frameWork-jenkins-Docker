package flight_reservation_app.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RegistrationConfirmationPage extends AbstractPage{

    @FindBy(xpath = "//div[@id='registration-confirmation-section']/div/div/div/p/b")
    private WebElement userName;

    @FindBy(xpath = "//a[@id='go-to-flights-search']")
    private WebElement goToFlightSerchButton;

    public RegistrationConfirmationPage(WebDriver driver){
        super(driver);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(goToFlightSerchButton));
        return goToFlightSerchButton.isDisplayed();
    }

    public void pressGoToFlightSerchButton(){
        goToFlightSerchButton.click();
    }

    public String getUserName(){
        return userName.getText();
    }

}
