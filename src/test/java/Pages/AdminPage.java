package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class AdminPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    private final By adminMenu = By.xpath("//span[text()='Admin']");
    private final By addBtn = By.xpath("//button[normalize-space()='Add']");

    private final By roleDropdown = By.xpath("//label[text()='User Role']/following::div[contains(@class,'oxd-select-text')]");
    private final By statusDropdown = By.xpath("//label[text()='Status']/following::div[contains(@class,'oxd-select-text')]");

    private final By employeeInput = By.xpath("//input[@placeholder='Type for hints...']");
    private final By firstSuggestion = By.xpath("//div[@role='listbox']//span");

    private final By usernameInput = By.xpath("//label[text()='Username']/following::input");
    private final By passwordInput = By.xpath("//label[text()='Password']/following::input");
    private final By confirmPassInput = By.xpath("//label[text()='Confirm Password']/following::input");
    private final By saveBtn = By.xpath("//button[normalize-space()='Save']");

    private final By searchUserInput = By.xpath("//label[text()='Username']/following::input");
    private final By searchBtn = By.xpath("//button[normalize-space()='Search']");
    private final By trashIcon = By.xpath("//i[contains(@class,'bi-trash')]");
    private final By confirmDeleteBtn = By.xpath("//button[contains(@class,'oxd-button--label-danger')]");
    private final By noRecordMsg = By.xpath("//span[text()='No Records Found']");
    private final By tableSpinner = By.xpath("//div[contains(@class, 'oxd-loading-spinner')]");

    public void goToAdmin() {
        wait.until(ExpectedConditions.elementToBeClickable(adminMenu)).click();
    }

    public void createUser(String user, String role, String pass) {
        wait.until(ExpectedConditions.elementToBeClickable(addBtn)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(roleDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='listbox']//*[text()='" + role + "']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(statusDropdown)).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@role='listbox']//*[text()='Enabled']"))).click();
        WebElement empField = driver.findElement(employeeInput);
        empField.sendKeys("a");
        wait.until(ExpectedConditions.elementToBeClickable(firstSuggestion)).click();
        driver.findElement(usernameInput).sendKeys(user);
        driver.findElement(passwordInput).sendKeys(pass);
        driver.findElement(confirmPassInput).sendKeys(pass);

        driver.findElement(saveBtn).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(saveBtn));
    }

    public void searchUser(String user) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchUserInput));
        input.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        input.sendKeys(user);
        wait.until(ExpectedConditions.elementToBeClickable(searchBtn)).click();

        // Safely wait for spinner only if it shows up, otherwise proceed
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(tableSpinner));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(tableSpinner));
        } catch (TimeoutException ignored) {
            // Spinner did not appear within 2 seconds, DOM is already stable
        }
    }

    public boolean isUserPresent() {
        try {
            // Custom short wait to prevent test lagging if user is missing
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(trashIcon)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void deleteUser() {
        wait.until(ExpectedConditions.elementToBeClickable(trashIcon)).click();
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteBtn)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmDeleteBtn));
    }

    public boolean isUserDeleted() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordMsg)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}