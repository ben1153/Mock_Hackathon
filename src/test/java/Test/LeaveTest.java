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
    public void testLeaveModuleLoadsCorrectly() {
        leavePage.navigateToLeaveModule();
        Assert.assertTrue(leavePage.isLeaveModuleLoaded(),
                "Leave module did not load correctly");
    }

    @Test(priority = 2)
    public void testApplyLeaveWithValidDates() {
        leavePage.goToApply();
        leavePage.applyLeave("06/01/2026", "06/03/2026", "Automation Leave Request");
        Assert.assertTrue(leavePage.isLeaveAppliedSuccessfully(),
                "Success toast was not displayed after applying leave with valid dates");
    }

    @Test(priority = 3)
    public void testAppliedLeaveAppearsInListWithStatus() {
        leavePage.goToApply();
        leavePage.applyLeave("06/01/2026", "06/03/2026", "Status Check Request");
        Assert.assertTrue(leavePage.isLeaveAppliedSuccessfully(),
                "Leave was not applied before checking the list");
        leavePage.goToLeaveList();
        Assert.assertTrue(leavePage.isLeaveListContainerVisible(),
                "Leave list container was not visible");
        Assert.assertTrue(leavePage.isAppliedLeaveStatusVisible(),
                "Applied leave did not appear in the list with an expected status");
    }

    @Test(priority = 4)
    public void testApplyLeaveWithPastDatesShowsError() {
        leavePage.goToApply();
        leavePage.applyLeave("01/01/2020", "01/03/2020", "Past Date Request");
        Assert.assertTrue(leavePage.isPastDateErrorVisible(),
                "Expected a field error for past dates but none appeared");
    }
}