package selenium;
import edu.brown.cs.student.main.Repl;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.Color;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class SeleniumAssertedTest {

    @Test
    public void testTableViz() {
        WebDriverManager.safaridriver().setup();
        SafariOptions options = new SafariOptions();
        SafariDriver driver = new SafariDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        Actions actions = new Actions(driver);
        driver.get("file:///Users/alexandravondergoltz/Desktop/cs32/project-2-avonderg-nxu4-sanand14/frontend/table/table.html");

        testTitles(driver);
        testLoadData(driver, actions);
        testStyles(driver);

        driver.quit();
    }

    public void testTitles(SafariDriver driver) {
//        System.out.println("HTML file title: " + driver.getTitle());
//        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        assertEquals(driver.getTitle(), "Table");
        // TEST: title is loaded correctly onto page
        List<WebElement> table_titles = driver.findElements(By.tagName("h1"));
        System.out.println("Webpage header is: " + table_titles.get(1).getText()); // gets the second element in the list -> corresponding to title
       assertEquals(table_titles.get(1).getText(), "Sprint 3 Table Visualization");
    }

    public void testLoadData(SafariDriver driver, Actions actions) {
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

}
