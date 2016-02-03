package test;

import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;


/**
 * Created by laureltimko on 2/2/16.
 */
public class DashboardTest extends SeleniumTest {

    private WebDriver driver;
    private String baseUrl;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }


    /**
     * Test for selecting a single Server from the table.
     * Expect to get a new table to appear and message stating what
     * Server's queues are being displayed.
     * @throws Exception
     */
    @Test
    public void testSelectASingleServer() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        assertTextPresent("Server", driver);

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        // find the row containing Server1
        WebElement row = serverTable.findElement(By.xpath("//tr/td[contains(text(), 'Server1')]"));
        row.click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("Server1", serverSelected.getText());

        WebElement queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

    }

    /**
     * Test for selecting a single server from the table and
     * then changing the server selected.
     * Expect to get a new table to appear and message stating what
     * Server's queues are being displayed.
     * @throws Exception
     */
    @Test
    public void testChangeServerSelection() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        assertTextPresent("Server", driver);

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        // find the row containing Server1
        WebElement row = serverTable.findElement(By.xpath("//tr/td[contains(text(), 'Server1')]"));
        row.click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("Server1", serverSelected.getText());

        WebElement queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

        //Change server selection.
        row = serverTable.findElement(By.xpath("//tr/td[contains(text(), 'Server2')]"));
        row.click();

        serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("Server2", serverSelected.getText());

        queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

    }

    /**
     * Test for selecting a single server from the table
     * and then selecting a queue from the new table.
     * Expect to get two new tables to appear and message stating what
     * Server's queues are being displayed and what the queue's items are.
     * @throws Exception
     */
    @Test
    public void testSelectAServerQueue() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        assertTextPresent("Server", driver);

        // find the customer table
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        // find the row
        WebElement row = serverTable.findElement(By.xpath("//tr/td[contains(text(), 'Server1')]"));
        row.click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("Server1", serverSelected.getText());

        WebElement queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

        row = queueTable.findElement(By.xpath("//tr/td[contains(text(), 'Test2')]"));
        row.click();

        WebElement redisTable = driver.findElement(By.id("redisTable"));
        assertNotNull(redisTable);

    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

}
