package tests;

import driverManager.DriverManagerTest;
import pojo_modells.VendorPortalPojo;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import utils.ConfigurationProperties;
import utils.Constants;
import utils.JsonUtil;
import vendorPortalApplication.DashBordPage;
import vendorPortalApplication.LoginPage;

import java.io.IOException;


public class VendorPortalTest extends DriverManagerTest {

    private LoginPage loginPage;
    private DashBordPage dashBordPage;
    private VendorPortalPojo vendorPortalPojo;

    @BeforeTest
    @Parameters("PathToDataInJson")
     public void setUpFields(String PathToDataInJson) throws IOException {
        this.loginPage = new LoginPage(driver);
        this.dashBordPage = new DashBordPage(driver);
        this.vendorPortalPojo= JsonUtil.getTestData(PathToDataInJson,VendorPortalPojo.class);
    }

    @Test
    public void userRegistrationTest(){

        loginPage.goTo(ConfigurationProperties.get(Constants.VENDOR_PORTAL_URL));
        Assert.assertTrue(loginPage.isAt());
        loginPage.login(vendorPortalPojo.userName(), vendorPortalPojo.password());
    }
    @Test(dependsOnMethods = {"userRegistrationTest"})
    public void dashbordTest(){
        Assert.assertTrue(dashBordPage.isAt());
        Assert.assertEquals(dashBordPage.getMunthlyErning(), vendorPortalPojo.monthlyEarning());
        Assert.assertEquals(dashBordPage.getAnnualEarning(), vendorPortalPojo.annyalEarning());
        Assert.assertEquals(dashBordPage.getProfit_margin(), vendorPortalPojo.profitMargin());
        Assert.assertEquals(dashBordPage.getAvailableInventory(), vendorPortalPojo.availibleEnventory());

        dashBordPage.serchOrderHistory(vendorPortalPojo.serchKeyword());
        Assert.assertEquals(dashBordPage.getResultCount(),vendorPortalPojo.serchResultCount());
     }

    @Test(dependsOnMethods = {"dashbordTest"})
    public void LogOut() throws InterruptedException {
        dashBordPage.logOut();
        Assert.assertTrue(loginPage.isAt());
    }

}
