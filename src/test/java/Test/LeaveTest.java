package Test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.LoginPage;
import Pages.LeavePage;

public class LeaveTest extends BaseTest {
    private LeavePage leavePage;

    @BeforeMethod
    public void setupModule() {
        new LoginPage(driver).login("Admin", "admin123");
        leavePage = new LeavePage(driver);
    }

    @Test(priority = 1)
    public void testApplyLeaveSuccessfully() {
        leavePage.goToApply();
        leavePage.applyLeave("2026-06-01", "2026-06-03", "Automation Leave Request");
        Assert.assertTrue(leavePage.isLeaveAppliedSuccessfully());
    }

    @Test(priority = 2)
    public void testVerifyAppliedLeaveInList() {
        leavePage.goToLeaveList();
        Assert.assertTrue(leavePage.isLeaveListContainerVisible());
    }

    @Test(priority = 3)
    public void testApplyLeaveWithPastDates() {
        leavePage.goToApply();
        leavePage.applyLeave("2020-01-01", "2020-01-03", "Past Date Request");
        Assert.assertTrue(leavePage.isPastDateErrorVisible());
    }
}
