package flight_reservation_app.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class FlightSerchPage extends AbstractPage{

    @FindBy(xpath = "//select[@id='passengers']")private WebElement selectPasangers;
    @FindBy(xpath = "//button[@id='search-flights']")private WebElement serchFlightButton;

    public FlightSerchPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(selectPasangers));
        return selectPasangers.isDisplayed();
    }

    public void selectPassengersNumber(String number){
        Select select=new Select(selectPasangers);
        select.selectByValue(number);
    }

    public void serchForFlight(){
        serchFlightButton.click();
    }
}
