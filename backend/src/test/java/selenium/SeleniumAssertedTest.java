package selenium;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.Color;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class SeleniumAssertedTest {

//    @Test
//    /**
//     * Runs Selenium and sets up the SafariDriver, tests that all tests are passed, and then quits the driver
//     */
//    public void testTableViz() {
//        WebDriverManager.safaridriver().setup();
//        SafariOptions options = new SafariOptions();
//        SafariDriver driver = new SafariDriver(options);
//        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
//
//        driver.get("../../../../../frontend/table/table.html");
//
//        testTitles(driver);
//        testLoadData(driver);
//        testStyles(driver);
//        testInputs(driver);
//
//        driver.quit();
//    }

    /**
     * Tests that all titles appear properly on the HTML page
     * @param driver
     */
    public void testTitles(SafariDriver driver) {
        assertEquals(driver.getTitle(), "Table");
        // TEST: title is loaded correctly onto page
        List<WebElement> table_titles = driver.findElements(By.tagName("h1"));
        System.out.println("Webpage header is: " + table_titles.get(1).getText()); // gets the second element in the list -> corresponding to title
       assertEquals(table_titles.get(1).getText(), "Sprint 3 Table Visualization");
    }

    /**
     * Tests that dropdown options on the main menu can be selected, and data from the table can be loaded properly
     * onto the webpage
     * @param driver
     */
    public void testLoadData(SafariDriver driver) {
        List<WebElement> buttons = driver.findElements(By.tagName("button"));

        // items in dropdown menu can be selected and visualize the table
        WebElement dropdownDiv = driver.findElement(By.xpath("/html/body/div[1]"));
        List<WebElement> dropdownMenu = dropdownDiv.findElements(By.tagName("option"));
        // go through each possible database

        // TEST: data can be properly loaded from horoscope table
        dropdownMenu.get(0).click();
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver, dropdownMenu.get(0).getText());

        // TEST: data can be properly loaded from ta_horoscope table
        dropdownMenu.get(1).click();
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver, dropdownMenu.get(1).getText());

        // TEST: data can be properly loaded from tas table
        dropdownMenu.get(2).click();
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver, dropdownMenu.get(2).getText());

        // TEST: data can be properly loaded from sqlite_sequence table
        dropdownMenu.get(3).click();
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver, dropdownMenu.get(3).getText());
    }

    /**
     * Tests that tables are loaded properly, as well as their cell values
     * @param driver
     * @param type
     */
    public void testTable(SafariDriver driver, String type) {
        List<WebElement> rowsNumber = driver.findElements(By.xpath("/html/body/div[2]/table/tbody/tr"));
        int rowCount = rowsNumber.size();

        List<WebElement> cols = driver.findElements(By.xpath("/html/body/div[2]/table/tbody/tr[1]/td"));
        int colCount = cols.size();

        // locating table
        WebElement tableValues = driver.findElement(By.xpath("/html/body/div[2]/table/tbody"));
        // extracting rows
        List<WebElement> allRows = tableValues.findElements(By.tagName("tr"));
        int totalRowcount = allRows.size();
        List<String> values = new ArrayList<>();

        // looping through rows
        for (int row = 0; row < totalRowcount; row++) {
            List<WebElement> allCols = allRows.get(row).findElements(By.tagName("td"));
            for (int col = 0; col< colCount;col++) {
                if ((row == 0) && (col == 0)) {
                    values.add(allCols.get(col).getText());
                }
                else if (row == 1) {
                    if (col == 0) {
                        values.add(allCols.get(col).getText());
                    }
                    else if (col == 1) {
                        values.add(allCols.get(col).getText());
                    }
                }
            }
        }

        if (type.equals("horoscopes")) {
            assertEquals(rowCount, 13);
            assertEquals(colCount, 3);
            assertEquals(values.get(0), "row");
            assertEquals(values.get(1), "0");
            assertEquals(values.get(2), "1");
        }
        else if (type.equals("ta_horoscope")) {
            assertEquals(rowCount, 7);
            assertEquals(colCount, 3);
            assertEquals(values.get(0), "row");
            assertEquals(values.get(1), "0");
            assertEquals(values.get(2), "2");
        }
        else if (type.equals("tas")) {
            assertEquals(rowCount, 9);
            assertEquals(colCount, 4);
            assertEquals(values.get(0), "row");
            assertEquals(values.get(1), "0");
            assertEquals(values.get(2), "1");
        }
        else if (type.equals("sqlite_sequence")) {
            assertEquals(rowCount, 2);
            assertEquals(colCount, 3);
            assertEquals(values.get(0), "row");
            assertEquals(values.get(1), "0");
            assertEquals(values.get(2), "tas");
        }
    }

    /**
     * Tests that certain CSS styles on the webpage are loaded properly
     * @param driver
     */
    public void testStyles(SafariDriver driver) {
        //Locate text string element to read its font properties.
        WebElement text = driver.findElement(By.xpath("/html/body/h1[2]"));

        //Read font-size property and print It In console.
        String fontSize = text.getCssValue("font-size");
        assertEquals(fontSize, "32px");

        // Verify font color
        String fontColor = text.getCssValue("color");
        String c = Color.fromString(fontColor).asHex();
        assertEquals(c, "#000000");

        //Read font-family property and print It In console.
        String fontFamily = text.getCssValue("font-family");
        assertEquals(fontFamily,"Impact, sans-serif");

        //Read text-align property and print It In console.
        String fonttxtAlign = text.getCssValue("text-align");
        assertEquals(fonttxtAlign, "center");

        // Repeat process for header of curr table
        // Locate text string element to read its font properties.
        WebElement text2 = driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]"));

        //Read font-size property and print It In console.
        String fontSize2 = text2.getCssValue("font-size");
        assertEquals(fontSize2,"16px");

        // Verify font color
        String fontColor2 = text.getCssValue("color");
        String c2 = Color.fromString(fontColor2).asHex();
        assertEquals(c2, "#000000");

        //Read font-family property and print It In console.
        String fontFamily2 = text2.getCssValue("font-family");
        assertEquals(fontFamily2,"Impact, sans-serif");

        //Read text-align property and print It In console.
        String fonttxtAlign2 = text2.getCssValue("text-align");
        assertEquals(fonttxtAlign2, "left");

        // test background color
        String bckgclr = driver.findElement(By.xpath("/html/body"))
                .getCssValue("background-color");
        assertEquals(bckgclr, "rgb(152, 164, 166)");
        System.out.println("Webpage background color: " + bckgclr);
    }

    /**
     * Tests input text boxes for add and delete functionality.
     * @param driver
     */
    public void testInputs(SafariDriver driver) {
        // get all input elements (for insert and delete functionalities)
        List<WebElement> inputs = driver.findElements(By.tagName("input"));
        String text = "1";

        // User can input text in “Insert Row” text box
        inputs.get(0).click();
        inputs.get(0).sendKeys(text);
        assertEquals(text, inputs.get(0).getAttribute("value"));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits

        // User can input row number in “Delete Row” text box
        inputs.get(1).click();
        inputs.get(1).sendKeys(text);
        assertEquals(text, inputs.get(1).getAttribute("value"));
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
    }


}
