package vendorPortalApplication;

import flight_reservation_app.pages.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashBordPage extends AbstractPage {

     @FindBy(xpath = "//div[@id='monthly-earning']")private WebElement monthlyEarning;
     @FindBy(xpath = "//div[@id='annual-earning']")private WebElement annualEarning;
     @FindBy(xpath = "//div[@id='profit-margin']")private WebElement profit_margin;
     @FindBy(xpath = "//div[@id='available-inventory']")private WebElement availableInventory;
     @FindBy(xpath = "//div[@id='dataTable_filter']/label/input")private WebElement serchInput;
     @FindBy(xpath = "//div[@id='dataTable_info']")private WebElement serchResultsCountElement;
     @FindBy(xpath = "//a[@id='userDropdown']//img")private WebElement userProfilePicture;
     @FindBy(xpath = "//a[@data-target='#logoutModal']")private WebElement logoutLink;
     @FindBy(xpath = "//a[text()='Logout']")private WebElement logoutButton;


    public DashBordPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isAt() {
        wait.until(ExpectedConditions.visibilityOf(monthlyEarning));
        return monthlyEarning.isDisplayed();
    }

    public String getMunthlyErning(){
        return monthlyEarning.getText();
    }
    public String getAnnualEarning(){
        return annualEarning.getText();
    }
    public String getProfit_margin(){
        return profit_margin.getText();
    }
    public String getAvailableInventory(){
        return availableInventory.getText();
    }
    public void serchOrderHistory(String str){
        serchInput.sendKeys(str);
    }
    public int getResultCount() {
        String wholeResult = serchResultsCountElement.getText();
        String result[] = wholeResult.split(" ");
        int count=Integer.parseInt(result[5]);
        return count;
    }

    public void logOut(){
        userProfilePicture.click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }
}
