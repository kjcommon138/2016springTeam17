package com.ncsu.csc492.group17.test;

import junit.framework.TestCase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by laureltimko on 2/2/16.
 */
abstract class SeleniumTest extends TestCase {

    protected void setUp() throws Exception {
    }


    /**
     * Asserts that the text is on the page
     * @param text
     * @param driver
     */
    public void assertTextPresent(String text, WebDriver driver) {
        List<WebElement> list = driver.findElements(By
                .xpath("//*[contains(text(),'" + text + "')]"));
        assertTrue("Text not found!", list.size() > 0);
    }

    /**
     * Asserts that the text is not on the page. Does not pause for text to appear.
     * @param text
     * @param driver
     */
    public void assertTextNotPresent(String text, WebDriver driver) {
        assertFalse("Text should not be found!",
                driver.findElement(By.cssSelector("BODY")).getText().contains(text));
    }

}