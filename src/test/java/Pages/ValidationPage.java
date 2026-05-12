package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class ValidationPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ValidationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private final By pimMenu = By.xpath("//span[text()='PIM']");
    private final By addEmpTab = By.xpath("//a[text()='Add Employee']");
    private final By firstName = By.name("firstName");
    private final By lastName = By.name("lastName");
    private final By saveBtn = By.xpath("//button[@type='submit']");
    private final By errorMessages = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");
    private final By dateInput = By.xpath("//label[text()='Date of Birth']/following::input");
    private final By nationalityDrp = By.xpath("//label[text()='Nationality']/following::div[contains(@class,'oxd-select-text')]");
    private final By drpOptions = By.xpath("//div[@role='listbox']//div[@role='option']");
    private final By globalLoader = By.xpath("//div[contains(@class,'oxd-form-loader')]");

    public void navigateToAddEmployee() {
        wait.until(ExpectedConditions.elementToBeClickable(pimMenu)).click();
        wait.until(ExpectedConditions.elementToBeClickable(addEmpTab)).click();
    }

    public void clickSave() {
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
    }

    public int getValidationErrorsCount() {
        try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
        return driver.findElements(errorMessages).size();
    }

    public void createDummyEmployee() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName)).sendKeys("Test");
        driver.findElement(lastName).sendKeys("User");
        wait.until(ExpectedConditions.elementToBeClickable(saveBtn)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
    }

    public String enterInvalidDateAndGetError(String date) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
        field.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        field.sendKeys(date);
        WebElement submitButton = wait.until(ExpectedConditions.presenceOfElementLocated(saveBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitButton);
        By dateError = By.xpath("//label[text()='Date of Birth']/following::span[contains(@class,'oxd-input-field-error-message')]");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(dateError)).getText();
    }

    public boolean verifyDropdownSelection() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(nationalityDrp));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(drpOptions));
        if (!options.isEmpty()) {
            WebElement optionToClick = options.get(1);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", optionToClick);
            return true;
        }
        return false;
    }
}
