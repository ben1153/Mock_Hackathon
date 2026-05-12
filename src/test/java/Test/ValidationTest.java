package Test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import Pages.LoginPage;
import Pages.ValidationPage;

public class ValidationTest extends BaseTest {
    private ValidationPage vp;

    @BeforeMethod
    public void setupModule() {
        new LoginPage(driver).login("Admin", "admin123");
        vp = new ValidationPage(driver);
    }

    @Test(priority = 1)
    public void testEmptyMandatoryFields() {
        vp.navigateToAddEmployee();
        vp.clickSave();
        Assert.assertTrue(
                vp.getValidationErrorsCount() >= 2,
                "Expected at least 2 validation errors but got: " + vp.getValidationErrorsCount()
        );
    }

    @Test(priority = 2)
    public void testInvalidDateFormat() {
        vp.navigateToAddEmployee();
        vp.createDummyEmployee();
        String error = vp.enterInvalidDateAndGetError("99-99-9999");
        Assert.assertTrue(
                error.toLowerCase().contains("valid"),
                "Expected error message to contain 'valid' but got: " + error
        );
    }

    @Test(priority = 3)
    public void testDropdownSelectability() {
        vp.navigateToAddEmployee();
        vp.createDummyEmployee();
        Assert.assertTrue(
                vp.verifyDropdownSelection(),
                "Dropdown selection failed — no options found or selection not registered"
        );
    }
}