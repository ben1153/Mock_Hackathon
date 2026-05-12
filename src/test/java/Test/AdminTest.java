package Test;

import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.LoginPage;
import Pages.AdminPage;

public class AdminTest extends BaseTest {

    private static String uniqueUser;
    private final String password = "Password@123";
    private final String userRole = "ESS";

    private void ensureLoggedIn() {
        LoginPage lp = new LoginPage(driver);
        AdminPage admin = new AdminPage(driver);
        if (driver.getCurrentUrl().contains("login") || !driver.getCurrentUrl().contains("admin")) {
            lp.login("Admin", "admin123");
            admin.goToAdmin();
        }
    }

    @Test(priority = 1, description = "Create a new system user and assign an ESS role")
    public void createSystemUserTest() {
        AdminPage admin = new AdminPage(driver);
        ensureLoggedIn();

        uniqueUser = "TestUser_" + System.currentTimeMillis();
        admin.createUser(uniqueUser, userRole, password);
    }

    @Test(priority = 2, dependsOnMethods = {"createSystemUserTest"}, description = "Verify the new user appears in the admin user list")
    public void verifyUserInAdminListTest() {
        AdminPage admin = new AdminPage(driver);
        ensureLoggedIn();

        admin.searchUser(uniqueUser);
        Assert.assertTrue(admin.isUserPresent(), "Error: User " + uniqueUser + " was not created.");
    }

    @Test(priority = 3, dependsOnMethods = {"verifyUserInAdminListTest"}, description = "Delete the created user and verify it no longer appears")
    public void deleteAndVerifyUserRemovalTest() {
        AdminPage admin = new AdminPage(driver);
        ensureLoggedIn();

        admin.searchUser(uniqueUser);
        admin.deleteUser();
        admin.searchUser(uniqueUser);
        Assert.assertTrue(admin.isUserDeleted(), "Error: User was not deleted successfully.");
    }
}