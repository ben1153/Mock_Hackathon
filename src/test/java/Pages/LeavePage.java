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

    private final By leaveModuleMenu   = By.xpath("//span[text()='Leave']");
    private final By leavePageTitle    = By.xpath("//h6[contains(@class,'oxd-topbar-header-breadcrumb')]");
    private final By employeeInput     = By.xpath("//label[normalize-space()='Employee Name']/following::input[1]");
    private final By employeeSuggestion = By.xpath("//div[@role='listbox']//span");
    private final By leaveTypeDropdown = By.xpath("//label[normalize-space()='Leave Type']/following::div[contains(@class,'oxd-select-text')][1]");
    private final By dropdownOptions   = By.xpath("//div[@role='listbox']//div[@role='option']");
    private final By listbox           = By.xpath("//div[@role='listbox']");
    private final By fromDateInput     = By.xpath("//label[contains(text(),'From Date')]/following::input[1]");
    private final By toDateInput       = By.xpath("//label[contains(text(),'To Date')]/following::input[1]");
    private final By commentTextarea   = By.xpath("//textarea");
    private final By assignBtn         = By.xpath("//button[@type='submit']");
    private final By globalLoader      = By.xpath("//div[contains(@class,'oxd-form-loader')]");
    private final By successToast      = By.xpath("//div[contains(@class,'oxd-toast--success')]");
    private final By tableBody         = By.xpath("//div[@class='oxd-table-body']");
    private final By noRecordsMsg      = By.xpath("//span[text()='No Records Found']");
    private final By leaveStatusCells  = By.xpath("//div[@class='oxd-table-body']//div[@role='row']//div[@role='cell'][5]");
    private final By fieldError        = By.xpath("//span[contains(@class,'oxd-input-field-error-message')]");

    public void navigateToLeaveModule() {
        wait.until(ExpectedConditions.elementToBeClickable(leaveModuleMenu)).click();
        wait.until(ExpectedConditions.urlContains("leave"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
    }

    public boolean isLeaveModuleLoaded() {
        try {
            wait.until(ExpectedConditions.urlContains("leave"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(leavePageTitle));
            return driver.getCurrentUrl().contains("leave");
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void goToApply() {
        String baseUrl = driver.getCurrentUrl().split("/web/")[0];
        driver.get(baseUrl + "/web/index.php/leave/assignLeave");
        wait.until(ExpectedConditions.urlContains("assignLeave"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeInput));
    }

    public void goToLeaveList() {
        String baseUrl = driver.getCurrentUrl().split("/web/")[0];
        driver.get(baseUrl + "/web/index.php/leave/viewLeaveList");
        wait.until(ExpectedConditions.urlContains("viewLeaveList"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));
    }

    public void applyLeave(String fromDate, String toDate, String comment) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(globalLoader));

        WebElement empField = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeInput));
        empField.clear();
        empField.sendKeys("Admin");
        wait.until(ExpectedConditions.visibilityOfElementLocated(employeeSuggestion));
        driver.findElements(By.xpath("//div[@role='listbox']//span")).get(0).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(listbox));

        WebElement dropdown = wait.until(ExpectedConditions.presenceOfElementLocated(leaveTypeDropdown));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", dropdown);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", dropdown);

        wait.until(ExpectedConditions.presenceOfElementLocated(listbox));
        List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(dropdownOptions));

        for (WebElement option : options) {
            String text = option.getText().trim();
            if (!text.isEmpty() && !text.equalsIgnoreCase("select")) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", option);
                break;
            }
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(listbox));
        enterDate(fromDateInput, fromDate);
        enterDate(toDateInput, toDate);

        try {
            WebElement textarea = wait.until(ExpectedConditions.presenceOfElementLocated(commentTextarea));
            textarea.clear();
            textarea.sendKeys(comment);
        } catch (TimeoutException ignored) {}

        WebElement submit = wait.until(ExpectedConditions.presenceOfElementLocated(assignBtn));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submit);
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
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(tableBody),
                    ExpectedConditions.visibilityOfElementLocated(noRecordsMsg)
            ));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isAppliedLeaveStatusVisible() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(tableBody));
            List<WebElement> statusCells = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(leaveStatusCells));
            for (WebElement cell : statusCells) {
                String status = cell.getText().trim().toLowerCase();
                if (status.contains("pending") || status.contains("approved") || status.contains("scheduled")) {
                    return true;
                }
            }
            return false;
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

    private void enterDate(By locator, String date) {
        WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", field);
        field.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        field.sendKeys(Keys.BACK_SPACE);
        field.sendKeys(date);
        field.sendKeys(Keys.TAB);
    }
}