package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class LoginPage {

    WebDriver driver;
    WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(Integer.parseInt(Test.ConfigReader.get("timeout"))));
    }

    // Locators
    By username = By.name("username");
    By password = By.name("password");
    By loginBtn = By.cssSelector("button[type='submit']");

    By dashboard = By.xpath("//h6[text()='Dashboard']");
    By invalidError = By.xpath("//p[contains(@class,'oxd-alert-content-text')]");
    By requiredError = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");

    By profileMenu = By.xpath("//span[@class='oxd-userdropdown-tab']");
    By logoutBtn = By.xpath("//a[text()='Logout']");

    // Actions
    public void login(String user, String pass) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(username)).clear();
        driver.findElement(username).sendKeys(user);

        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(pass);

        driver.findElement(loginBtn).click();
    }

    public boolean isDashboardVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dashboard)).isDisplayed();
    }

    public String getInvalidError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(invalidError)).getText();
    }

    public String getRequiredError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(requiredError)).getText();
    }

    public void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(profileMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }
}