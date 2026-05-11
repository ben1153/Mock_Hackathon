package Test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(ConfigReader.get("url"));
    }

    @AfterMethod
    public void teardown() {
        driver.quit();
    }
}