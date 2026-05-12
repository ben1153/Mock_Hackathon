package Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.List;

public class LeavePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public LeavePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private final By leaveTypeDropdown = By.xpath("//div[contains(@class,'oxd-select-text')]");
    private final By dropdownOptions = By.xpath("//div[@role='listbox']//div[@role='option']");
    private final By fromDateInput = By.xpath("//label[text()='From Date']/following::input");
    private final By toDateInput = By.xpath("//label[text()='To Date']/following::input");
    private final By commentTextarea = By.xpath("//textarea");
    private final By assignBtn = By.xpath("//button[@type='submit']");
    private final By globalLoader = By.xpath("//div[contains(@class,'oxd-form-loader')]");
    private final By successToast = By.xpath("//div[contains(@class,'oxd-toast--success')]");
    private final By tableBody = By.xpath("//div[@class='oxd-table-body']");
    private final By noRecordsMsg = By.xpath("//span[text()='No Records Found']");
    private final By fieldError = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");

    public void goToApply() {
        String currentUrl = driver.getCurrentUrl();
        String baseUrl = currentUrl.split("/web/")[0];
        driver.get(baseUrl + "/web/index.php/leave/applyLeave");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(assignBtn));
    }

    public void goToLeaveList() {
        String currentUrl = driver.getCurrentUrl();
        String baseUrl = currentUrl.split("/web/")[0];
        driver.get(baseUrl + "/web/index.php/leave/viewLeaveList");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
    }

    public void applyLeave(String fromDate, String toDate, String comment) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));

        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(leaveTypeDropdown));
        try {
            dropdown.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);
        }

        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(dropdownOptions));
        for (WebElement option : options) {
            String optionText = option.getText();
            if (!optionText.isEmpty() && !optionText.contains("Select")) {
                option.click();
                break;
            }
        }

        WebElement fromField = wait.until(ExpectedConditions.visibilityOfElementLocated(fromDateInput));
        fromField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        fromField.sendKeys(fromDate);

        WebElement toField = wait.until(ExpectedConditions.visibilityOfElementLocated(toDateInput));
        toField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        toField.sendKeys(toDate);

        try {
            driver.findElement(commentTextarea).sendKeys(comment);
        } catch (Exception ignored) {}

        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(assignBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
    }

    public boolean isLeaveAppliedSuccessfully() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(successToast)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isLeaveListContainerVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody)).isDisplayed() ||
                    wait.until(ExpectedConditions.visibilityOfElementLocated(noRecordsMsg)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPastDateErrorVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(fieldError)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }
}
