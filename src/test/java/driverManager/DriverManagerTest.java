package driverManager;

import com.google.common.util.concurrent.Uninterruptibles;
import listener.TestListener;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.annotations.*;
import utils.ConfigurationProperties;
import utils.Constants;

import java.net.MalformedURLException;
import java.net.URL;
// Class creates driver. In default.properties we define properties but it can be override with POM

//for example POM.xml difines wether driver will be remote or local
// by using system parameter 'selenium_grid_enabled' = true or false

@Listeners(TestListener.class)// this annotation is for screenShot
public abstract class DriverManagerTest {

    protected WebDriver driver;

    @BeforeSuite
    public void setUpConfigurationProperties(){
        ConfigurationProperties.initializeProperties();
    }

    @BeforeTest
    public void setUpDriver(ITestContext tct) throws MalformedURLException {
        //this value is set in pom.xml (it is controlling the way how ro run the framework wia Docker or locally)
        this.driver=Boolean.parseBoolean(ConfigurationProperties.get(Constants.GRID_ENABLED)) ? getRemoteDriver() : getLocalDriver();
        tct.setAttribute(Constants.DRIVER, this.driver);
        //ITestContext is class that stores information about current test, and one of the methods is to create
        //additional attribute which we can use later. Every test will have its own object of ITestContext.
    }

    //with aid of that method we run framework on docker !!!
    private WebDriver getRemoteDriver() throws MalformedURLException {
        Capabilities capabilities = new ChromeOptions(); // < --by default it is going to be chrome
        if(Constants.FIREFOX.equalsIgnoreCase(ConfigurationProperties.get(Constants.BROWSER))){
            capabilities=new FirefoxOptions();
        }
        String urlFormat = ConfigurationProperties.get(Constants.GRID_URL_FORMAT);
        String hubHost = ConfigurationProperties.get(Constants.GRID_HUB_HOST);
        String formatedUrl = String.format(urlFormat,hubHost);

        System.out.println("---> URL wich we use for seleniumGrid <-- "+formatedUrl);
        return new RemoteWebDriver(new URL(formatedUrl), capabilities);
    }

    private WebDriver getLocalDriver(){
        //on local machine brawser will be only chrome any changes in POM.xml doesnot matter.
        return new ChromeDriver();
    }

    @AfterTest
    public void qiutDriver(){
        driver.quit();
    }
}
