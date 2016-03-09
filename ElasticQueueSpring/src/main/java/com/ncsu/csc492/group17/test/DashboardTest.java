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

        System.setProperty("webdriver.chrome.driver", "lib/chromedriver");
        driver = new ChromeDriver();
        baseUrl = "http://localhost:8080";

        //Make sure to wait for items to load before throwing exception
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
        driver.get(baseUrl + "/ElasticQueue");

        assertTextPresent("Server", driver);

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the first row.
        List<WebElement> tableRows = serverTable.findElements(By.tagName("tr"));

        tableRows.get(1).click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("152.14.106.22:30009", serverSelected.getText());

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
        driver.get(baseUrl + "/ElasticQueue");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        int rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(9, rowCount);

        // find the row 1
        List<WebElement> rows = serverTable.findElements(By.tagName("tr"));
        List<WebElement> rowOne = rows.get(1).findElements(By.tagName("td"));
        assertEquals("Active", rowOne.get(0).getText());
        assertEquals("Slave", rowOne.get(1).getText());
        assertEquals("152.14.106.22", rowOne.get(2).getText());
        assertEquals("30009", rowOne.get(3).getText());

        // find the row 6
        List<WebElement> rowSix = rows.get(6).findElements(By.tagName("td"));
        assertEquals("Active", rowSix.get(0).getText());
        assertEquals("Master", rowSix.get(1).getText());
        assertEquals("152.14.106.22", rowSix.get(2).getText());
        assertEquals("30002", rowSix.get(3).getText());

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
        driver.get(baseUrl + "/ElasticQueue");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the first row.
        List<WebElement> tableRows = serverTable.findElements(By.tagName("tr"));

        //Get the server's data
        List<WebElement> cells = tableRows.get(5).findElements(By.tagName("td"));
        assertEquals("30003", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        assertEquals("Active", cells.get(0).getText());
    }

    /**
     * Test that master nodes are displayed in the table of servers.
     * Expect that master servers are displayed in the table and that
     * selecting them displays their information.
     * @throws Exception
     */
    @Test
    public void testCheckMasterNode() throws Exception {
        driver.get(baseUrl + "/ElasticQueue");

        Thread.sleep(3000);
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the first Master which is the first row.
        List<WebElement> tableRows = serverTable.findElements(By.tagName("tr"));
        List<WebElement> cells = tableRows.get(5).findElements(By.tagName("td"));
        assertEquals("30003", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        assertEquals("Master", cells.get(1).getText());
    }

    /**
     * Test that slave nodes are displayed in the table of servers.
     * Expect that slave servers are displayed in the table and that
     * selecting them displays their information.
     * @throws Exception
     */
    @Test
    public void testCheckSlaveNode() throws Exception {
        driver.get(baseUrl + "/ElasticQueue");

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the first Slave which is the first row.
        List<WebElement> tableRows = serverTable.findElements(By.tagName("tr"));
        List<WebElement> cells = tableRows.get(1).findElements(By.tagName("td"));
        assertEquals("30009", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        assertEquals("Slave", cells.get(1).getText());
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
        driver.get(baseUrl + "/ElasticQueue");

        assertTextPresent("Server", driver);

        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        // find the first row.
        List<WebElement> tableRows = serverTable.findElements(By.tagName("tr"));
        tableRows.get(1).click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("152.14.106.22:30009", serverSelected.getText());

        WebElement queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

        //Change server selection to the 5th row.
        tableRows.get(5).click();

        serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("152.14.106.22:30003", serverSelected.getText());

        queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

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
