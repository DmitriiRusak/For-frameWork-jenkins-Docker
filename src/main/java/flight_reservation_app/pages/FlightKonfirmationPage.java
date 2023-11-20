package flight_reservation_app.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlightKonfirmationPage extends AbstractPage{

    @FindBy(xpath = "/html/body/div[1]/div[5]/div/div/div/form/div/div/div[1]/div[2]/p") private WebElement flightKonfirmation;
    @FindBy(xpath = "/html/body/div[1]/div[5]/div/div/div/form/div/div/div[3]/div[2]/p") private WebElement totalPrice;

    private final static Logger LOGGER = LoggerFactory.getLogger(FlightKonfirmationPage.class);

    public FlightKonfirmationPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(flightKonfirmation));
        return flightKonfirmation.isDisplayed();
    }


    public String getPrice(){
        String flightConfirmation = flightKonfirmation.getText();
        String price = totalPrice.getText();
        return price;
    }
}
