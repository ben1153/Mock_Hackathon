package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;

public class EmployeePage {

    WebDriver driver;
    WebDriverWait wait;

    public EmployeePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // 🔹 Menu
    By pimMenu = By.xpath("//span[text()='PIM']");
    By addBtn = By.xpath("//button[normalize-space()='Add']");

    // 🔹 Form
    By firstName = By.name("firstName");
    By lastName  = By.name("lastName");
    By saveBtn   = By.xpath("//button[@type='submit']");

    // 🔹 Loader (IMPORTANT)
    By loader = By.xpath("//div[contains(@class,'oxd-form-loader')]");

    // 🔹 Success page
    By personalDetailsHeader = By.xpath("//h6[normalize-space()='Personal Details']");

    // ================= METHODS =================

    public void goToPIM() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
    }

    public void addEmployee(String fName, String lName) {

        // Open Add form
        wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();

        // Wait for form fields
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));

        // Fill data
        driver.findElement(firstName).clear();
        driver.findElement(firstName).sendKeys(fName);

        driver.findElement(lastName).clear();
        driver.findElement(lastName).sendKeys(lName);

        // 🔥 WAIT for loader to disappear (CRITICAL FIX)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));

        // Optional scroll (extra safety)
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView(true);",
                driver.findElement(saveBtn)
        );

        // Click Save
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();

        // Wait for success page
        wait.until(ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader));
    }

    public boolean isEmployeeAdded() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(personalDetailsHeader)
        ).isDisplayed();
    }
}