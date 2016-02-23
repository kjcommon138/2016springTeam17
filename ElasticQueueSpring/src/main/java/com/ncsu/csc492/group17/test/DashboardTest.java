package com.ncsu.csc492.group17.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by laureltimko on 2/2/16.
 */
public class DashboardTest extends com.ncsu.csc492.group17.test.SeleniumTest {

    private WebDriver driver;
    private String baseUrl;
    private StringBuffer verificationErrors = new StringBuffer();

    @Before
    protected void setUp() throws Exception {
        super.setUp();

        System.setProperty("webdriver.chrome.driver", "lib/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

    }

    /**
     * Test for selecting a server from the table.
     * Expect to see the server's load adjusted in the progress bar.
     * Expect to get a new table to appear and message stating what
     * Server's queues are being displayed.
     * @throws Exception
     */
    @Test
    public void testCheckServerLoad() throws Exception {
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

        assertTextPresent("Load: High", driver);
        WebElement loadPercent = driver.findElement(By.className("progress-bar"));
        assertEquals("70%", loadPercent.getText());
    }


    /**
     * Test for checking the contents of the server list.
     * Expect to see the complete list of servers both active and inactive.
     * @throws Exception
     */
    @Test
    public void testCheckServerList() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        int rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(2, rowCount);

        // find the row 1
        List<WebElement> rows = serverTable.findElements(By.tagName("tr"));
        List<WebElement> rowOne = rows.get(1).findElements(By.tagName("td"));
        assertEquals("Server1", rowOne.get(0).getText());
        assertEquals("Active", rowOne.get(1).getText());
        assertEquals("Master", rowOne.get(2).getText());

        // find the row 2
        List<WebElement> rowTwo = rows.get(2).findElements(By.tagName("td"));
        assertEquals("Server2", rowTwo.get(0).getText());
        assertEquals("Active", rowTwo.get(1).getText());
        assertEquals("Slave", rowTwo.get(2).getText());

    }

    /**
     * Test that information is available for a failed server.
     * Expects to view selected inactive server's information.
     * @throws Exception
     */
    @Test
    public void testCheckInvalidServerStatus() throws Exception {
        //TO DO
        //NEED INVALID SERVER DATA
    }

    /**
     * Test that information is available for active servers.
     * Expects to view selected active server's information
     * including queues and server load.
     * @throws Exception
     */
    @Test
    public void testCheckValidServerStatus() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);
        //Get the row for Server1
        WebElement row = serverTable.findElement(By.xpath("//tr[string(td[1]) = 'Server1']"));
        //Get the server's data
        List<WebElement> cells = row.findElements(By.tagName("td"));
        assertTrue(row.getText().contains("Server1"));
        assertEquals("Server1", cells.get(0).getText());
        assertEquals("Active", cells.get(1).getText());
    }

    /**
     * Test that master nodes are displayed in the table of servers.
     * Expect that master servers are displayed in the table and that
     * selecting them displays their information.
     * @throws Exception
     */
    @Test
    public void testCheckMasterNode() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);
        //Get the row for Server1
        WebElement row = serverTable.findElement(By.xpath("//tr[string(td[1]) = 'Server1']"));
        //Get the server's data
        List<WebElement> cells = row.findElements(By.tagName("td"));
        assertTrue(row.getText().contains("Server1"));
        assertEquals("Server1", cells.get(0).getText());
        assertEquals("Master", cells.get(2).getText());
    }

    /**
     * Test that slave nodes are displayed in the table of servers.
     * Expect that slave servers are displayed in the table and that
     * selecting them displays their information.
     * @throws Exception
     */
    @Test
    public void testCheckSlaveNode() throws Exception {
        driver.get(baseUrl + "/index.jsp");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);
        WebElement row = serverTable.findElement(By.xpath("//tr[string(td[1]) = 'Server2']"));
        List<WebElement> cells = row.findElements(By.tagName("td"));
        assertTrue(row.getText().contains("Server2"));
        assertEquals("Server2", cells.get(0).getText());
        assertEquals("Slave", cells.get(2).getText());
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
