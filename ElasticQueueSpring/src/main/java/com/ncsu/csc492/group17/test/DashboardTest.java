package com.ncsu.csc492.group17.test;

import com.lambdaworks.redis.cluster.api.StatefulRedisClusterConnection;
import com.ncsu.csc492.group17.web.model.Server;

import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.cluster.RedisClusterClient;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.cluster.api.sync.RedisClusterCommands;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.*;

import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by laureltimko on 2/2/16.
 */
@SuppressWarnings("deprecated")
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

        driver.get(baseUrl + "/ElasticQueue");
        Thread.sleep(1000);

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
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Master 30001. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30002']]"));

        targetRow.click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("152.14.106.22:30002", serverSelected.getText());

        WebElement queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.id("progressHeading"), "Load: Low"));

        assertTextPresent("Load: Low", driver);
    }


    /**
     * Test for checking the contents of the server list.
     * Expect to see the complete list of servers both active and inactive.
     * @throws Exception
     */
    @Test
    public void testCheckServerList() throws Exception {
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Master 30001. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30001']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30001", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        assertEquals("Master", cells.get(1).getText());
        assertEquals("Active", cells.get(0).getText());

        //Get the Slave 30005. Don't delete this one.
        targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30005']]"));
        cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30005", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        assertEquals("Slave", cells.get(1).getText());
        assertEquals("Active", cells.get(0).getText());

    }

    /**
     * Test that information is available for active servers.
     * Expects to view selected active server's information
     * including queues and server load.
     * @throws Exception
     */
    @Test
    public void testCheckValidServerStatus() throws Exception {
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Master 30001. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30001']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30001", cells.get(3).getText());
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
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Master 30001. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30001']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30001", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        assertEquals("Master", cells.get(1).getText());
        assertEquals("Active", cells.get(0).getText());
    }

    /**
     * Test that slave nodes are displayed in the table of servers.
     * Expect that slave servers are displayed in the table and that
     * selecting them displays their information.
     * @throws Exception
     */
    @Test
    public void testCheckSlaveNode() throws Exception {
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Slave 30005. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30005']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30005", cells.get(3).getText());
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
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Click the Master 30001. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30001']]"));
        targetRow.click();

        WebElement serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("152.14.106.22:30001", serverSelected.getText());

        WebElement queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

        //Change the selection to select the Slave 30005. Don't delete this one.
        targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30005']]"));
        targetRow.click();

        serverSelected = driver.findElement(By.className("serverValue"));
        assertTextPresent("Showing queues for:", driver);
        assertEquals("152.14.106.22:30005", serverSelected.getText());

        queueTable = driver.findElement(By.id("queueTable"));
        assertNotNull(queueTable);

    }

    /**
     * Test removing a server from the table.
     * The user clicks the trashcan item of a master node.
     * The user confirms they want to delete the master.
     * The master node is deleted and a slave takes over for it.
     * @throws Exception
     */
    @Test
    public void testRemoveSever() throws Exception {
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        int rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(8, rowCount);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Click the Master 30003. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30003']]"));
        WebElement cell = targetRow.findElement(By.xpath("//td[text()='Master']"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30003", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        cells.get(4).click();

        Thread.sleep(2000);

        Alert alert = driver.switchTo().alert();
        assertEquals(alert.getText().contains("Are you sure you want to remove 152.14.106.22:30003?"), true);


        alert.accept();

        Thread.sleep(2000);

        driver.manage().timeouts().implicitlyWait(300000, TimeUnit.SECONDS);

        WebElement alertMessage = driver.findElement(By.id("loadingMessage"));
        //assertTextPresent("Deleting 152.14.106.22:30003. Please wait.", driver);
        assertEquals(alertMessage.getText().contains("Deleting 152.14.106.22:30003. Please wait."), true);

        waitForAlert(driver, alert);
        assertEquals(alert.getText().contains("Successful Failover"), true);
        alert.accept();

        serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        rowLoad = serverTable.findElement(By.id("row0"));

        targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30007']]"));
        cell = targetRow.findElement(By.xpath("//td[text()='Master']"));
        assertEquals(cell.getText(), "Master");
        cells = targetRow.findElements(By.tagName("td"));
        assertEquals("Master", cells.get(1).getText());

    }

    /**
     * Test adding an existing master server to the cluster.
     * The user clicks the Servers dropdown.
     * The user selects the Add Server menu option.
     * The user enters a new host IP address and new port number of server.
     * The user selects an existing master node in the cluster to connect to
     * and presses submit.
     * The user confirms they want to add the master.
     * The new master node appears in the table.
     * @throws Exception
     */
    @Test
    public void testAddServer() throws Exception {
        WebElement menu = driver.findElement(By.className("dropdown-toggle"));
        menu.click();
        Thread.sleep(1000);
        WebElement addOption = driver.findElement(By.id("addOption"));
        addOption.click();

        WebElement newHostInput = driver.findElement(By.id("newHost"));
        newHostInput.sendKeys("152.14.106.22");
        WebElement newPortInput = driver.findElement(By.id("newPort"));
        newPortInput.sendKeys("30003");

        WebElement listMasterServers = driver.findElement(By.id("currentMasterList"));
        listMasterServers.sendKeys("152.14.106.22:30007");
        WebElement submitButton = driver.findElement(By.id("submit"));
        Thread.sleep(1000);
        submitButton.click();

        Thread.sleep(3000);

        Alert alert = driver.switchTo().alert();
        assertEquals(alert.getText().contains("Do you want to add 152.14.106.22:30003?"), true);

        alert.accept();

        Thread.sleep(2000);

        driver.manage().timeouts().implicitlyWait(300000, TimeUnit.SECONDS);

        WebElement alertMessage = driver.findElement(By.id("loadingMessage"));
        //assertTextPresent("Deleting 152.14.106.22:30003. Please wait.", driver);
        assertEquals(alertMessage.getText().contains("Adding 152.14.106.22:30003. Please wait."), true);

        //Wait for the finished adding alert.
        waitForAlert(driver, alert);
        //assertEquals(alert.getText().contains("Server 152.14.106.22 30003 added."), true);
        alert.accept();

        Thread.sleep(2000);

        WebElement serverTable = driver.findElement(By.id("serverTable"));
        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));
        //Check the server added as a slave.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30003']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("Slave", cells.get(1).getText());

        int rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(8, rowCount);

    }


    /**
     * Test adding an existing slave node to the cluster.
     * The user clicks the Servers dropdown.
     * The user selects the Add Server menu option.
     * The user enters a new host IP address and new port number of server.
     * The user selects an existing master node in the cluster to connect to
     * and presses submit.
     * The user confirms they want to add the node.
     * The new slave node appears in the table.
     * @throws Exception
     */
    @Test
    public void testAddASlave() throws Exception {
        WebElement menu = driver.findElement(By.className("dropdown-toggle"));
        menu.click();
        Thread.sleep(1000);
        WebElement addOption = driver.findElement(By.id("addOption"));
        addOption.click();

        WebElement newHostInput = driver.findElement(By.id("newHost"));
        newHostInput.sendKeys("152.14.106.22");
        WebElement newPortInput = driver.findElement(By.id("newPort"));
        newPortInput.sendKeys("30006");

        WebElement listMasterServers = driver.findElement(By.id("currentMasterList"));
        listMasterServers.sendKeys("152.14.106.22:30002");
        WebElement submitButton = driver.findElement(By.id("submit"));
        Thread.sleep(1000);
        submitButton.click();

        Thread.sleep(3000);

        Alert alert = driver.switchTo().alert();
        assertEquals(alert.getText().contains("Do you want to add 152.14.106.22:30006?"), true);

        alert.accept();

        Thread.sleep(2000);

        driver.manage().timeouts().implicitlyWait(300000, TimeUnit.SECONDS);

        WebElement alertMessage = driver.findElement(By.id("loadingMessage"));
        //assertTextPresent("Deleting 152.14.106.22:30003. Please wait.", driver);
        assertEquals(alertMessage.getText().contains("Adding 152.14.106.22:30006. Please wait."), true);

        //Wait for the finished adding alert.
        waitForAlert(driver, alert);
        //assertEquals(alert.getText().contains("Server 152.14.106.22 30003 added."), true);
        alert.accept();

        Thread.sleep(2000);

        WebElement serverTable = driver.findElement(By.id("serverTable"));
        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));
        //Check the server added as a slave.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30006']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("Slave", cells.get(1).getText());

        int rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(7, rowCount);
    }


    /**
     * Test removing a server from the table.
     * The user clicks the trashcan item of a slave node.
     * The user confirms they want to delete the node.
     * The slave node is deleted.
     * @throws Exception
     */
    @Test
    public void testRemoveASlave() throws Exception {
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        int rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(7, rowCount);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Click the Master 30003. Don't delete this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30006']]"));
        WebElement cell = targetRow.findElement(By.xpath("//td[text()='Master']"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));
        assertEquals("30006", cells.get(3).getText());
        assertEquals("152.14.106.22", cells.get(2).getText());
        cells.get(4).click();

        Thread.sleep(2000);

        Alert alert = driver.switchTo().alert();
        assertEquals(alert.getText().contains("Are you sure you want to remove 152.14.106.22:30006?"), true);


        alert.accept();

        Thread.sleep(2000);

        driver.manage().timeouts().implicitlyWait(300000, TimeUnit.SECONDS);

        WebElement alertMessage = driver.findElement(By.id("loadingMessage"));
        //assertTextPresent("Deleting 152.14.106.22:30006. Please wait.", driver);
        assertEquals(alertMessage.getText().contains("Deleting 152.14.106.22:30006. Please wait."), true);

        waitForAlert(driver, alert);
        assertEquals(alert.getText().contains("Successful Failover"), true);
        alert.accept();

        serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        rowLoad = serverTable.findElement(By.id("row0"));

        rowCount = driver.findElements(By.xpath("//table[@id='serverTable']/tbody/tr")).size();
        assertEquals(6, rowCount);
    }


    /**
     * Test to make sure a disabled server is reflected in the table.
     * A server is shutdown. The server appears as disabled in the table.
     * @throws Exception
     */
    @Test
    public void testDisabledStatus() throws Exception {
        // find the table of Servers
        WebElement serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        WebElement rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Slave 30008. Will kill this one.
        WebElement targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[text()='30008']]"));
        List<WebElement> cells = targetRow.findElements(By.tagName("td"));

        assertEquals("Active", cells.get(0).getText());

        String host = cells.get(2).getText();
        int port = Integer.parseInt(cells.get(3).getText());

        assertEquals(30008, port);
        assertEquals("152.14.106.22", host);

        Server server1 = new Server();
        server1.setHost(host);
        server1.setPort(port);

        shutdownServer(server1);

        Thread.sleep(1000);

        driver.get(baseUrl + "/ElasticQueue");

        serverTable = driver.findElement(By.id("serverTable"));
        assertNotNull(serverTable);

        //Wait for rows to appear.
        rowLoad = serverTable.findElement(By.id("row0"));

        //Get the Slave 30008. Will kill this one.
        targetRow = serverTable.findElement(By.xpath("//tr[descendant::td[contains(.,'30008')]]"));
        cells = targetRow.findElements(By.tagName("td"));

        assertEquals("Disabled", cells.get(0).getText());

    }


    @After
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    /**
     * Method to shutdown a specified server. This will result in a disabled state.
     * @param server1
     */
    public void shutdownServer(Server server1) {
        RedisClient client;
        RedisClusterClient clusterClient;

        StatefulRedisConnection<String, String> redis6;

        RedisClusterCommands<String, String> redissync6;

        client = RedisClient.create(RedisURI.Builder.redis(server1.getHost(), server1.getPort()).build());
        clusterClient = RedisClusterClient.create(RedisURI.Builder.redis(server1.getHost(), server1.getPort())
                .build());

        redis6 = client.connect(RedisURI.Builder.redis(server1.getHost(), server1.getPort()).build());

        redissync6 = redis6.sync();

        redissync6.shutdown(false);

    }

    /**
     * Method to make sure the test waits appropriately for an
     * alert to appear.
     * @param driver
     * @param alert
     * @throws Exception
     */
    public void waitForAlert(WebDriver driver, Alert alert) throws Exception {
        boolean done = false;
        while(!done)
        {
            try
            {
                alert = driver.switchTo().alert();
                done = true;
                break;
            }
            catch(NoAlertPresentException e)
            {
                Thread.sleep(1000);
                continue;
            }
        }
    }


}
