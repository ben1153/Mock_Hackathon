package Test;

import org.testng.Assert;
import org.testng.annotations.*;
import Pages.LoginPage;

public class LoginTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() {
        return new Object[][]{
                {ConfigReader.get("username"), ConfigReader.get("password"), true},
                {ConfigReader.get("username"), "wrong123", false}
        };
    }
    @Test(dataProvider = "loginData", priority = 1)
    public void loginTest(String user, String pass, boolean expected) {

        LoginPage lp = new LoginPage(driver);
        lp.login(user, pass);

        if (expected) {
            Assert.assertTrue(lp.isDashboardVisible(), "Valid login failed");
        } else {
            Assert.assertTrue(lp.getInvalidError().contains("Invalid"),
                    "Invalid login error not shown");
        }
    }

    @Test(priority = 2)
    public void emptyLoginTest() {

        LoginPage lp = new LoginPage(driver);
        lp.login("", "");

        Assert.assertTrue(lp.getRequiredError().contains("Required"),
                "Required validation not shown");
    }
    @Test(priority = 3)
    public void logoutTest() {

        LoginPage lp = new LoginPage(driver);

        lp.login(
                ConfigReader.get("username"),
                ConfigReader.get("password")
        );

        lp.logout();

        Assert.assertTrue(driver.getCurrentUrl().contains("login"),
                "Logout failed");
    }
}