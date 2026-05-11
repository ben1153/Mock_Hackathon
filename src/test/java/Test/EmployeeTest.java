package Test;

import org.testng.Assert;
import org.testng.annotations.Test;
import Pages.LoginPage;
import Pages.EmployeePage;

public class EmployeeTest extends BaseTest {

    @Test
    public void addEmployeeTest() {
        LoginPage lp = new LoginPage(driver);
        lp.login("Admin", "admin123");
        EmployeePage emp = new EmployeePage(driver);
        String firstName = "John" + System.currentTimeMillis();
        String lastName  = "Doe";
        emp.goToPIM();
        emp.addEmployee(firstName, lastName);
        Assert.assertTrue(emp.isEmployeeAdded(), "Employee not added");
    }
}