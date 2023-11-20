package flight_reservation_app.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class FlightSelectionPage extends AbstractPage{
    Random random=new Random();

    @FindBy(name = "departure-flight") private List<WebElement> departureFlightsOptions;
    @FindBy(name = "arrival-flight") private List<WebElement> arrivalFlightsOptions;
    @FindBy(xpath = "//button[@id='confirm-flights']")private WebElement confirmFlightButton;

    public FlightSelectionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(confirmFlightButton));
        return confirmFlightButton.isDisplayed();
    }

    public void selectFlights(){
        int randomFlight=random.nextInt(departureFlightsOptions.size());
        departureFlightsOptions.get(randomFlight).click();
        arrivalFlightsOptions.get(randomFlight).click();
    }

    public void confirmFlights(){
        confirmFlightButton.click();
    }
}
