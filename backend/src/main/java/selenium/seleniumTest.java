package selenium;

// Remember to add the Maven dependency! You may also
// need to option-enter (alt-enter) and pick a
// "Maven: add ... to classpath" option.
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class seleniumTest {


    /**
     * Calls the runExample function, which allows the HTML file to be opened on a Safari browser
     */
    public static void start() throws InterruptedException {
//         There will be various messages in the console...
//         (By default, Selenium prints a lot of info)
        WebDriverManager.safaridriver().setup();
        runExample("file:///Users/alexandravondergoltz/Desktop/cs32/project-2-avonderg-nxu4-sanand14/frontend/table/table.html");
    }


    /**
     * Runs each of the appropriate tester methods for the HTML test file
     */
    public static void runExample(String path) throws InterruptedException {
        SafariOptions options = new SafariOptions();
        SafariDriver driver = new SafariDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        Actions actions = new Actions(driver);

        driver.get(path);
        testTitles(driver);
        testLoadData(driver, actions);
//        testInputs(driver, actions);
        testStyles(driver);

        driver.quit();
        System.out.println("quit driver");
    }

    /**
     * Tests that all titles are uploaded correctly onto the site
     * @param driver - SafariDriver
     */
    public static void testTitles(SafariDriver driver) {
        // TEST: HTML page title is read correctly
        System.out.println("HTML file title: " + driver.getTitle());
        System.out.println("- - - - - - - - - - - - - - - - - - - -");

        // TEST: title is loaded correctly onto page
        List<WebElement> table_titles = driver.findElements(By.tagName("h1"));
        System.out.println("Webpage header is: " + table_titles.get(1).getText()); // gets the second element in the list -> corresponding to title
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
    }

    /**
     * Tests that the load data button can be pressed, and data is loaded onto appropriate tables correctly
     * @param driver - SafariDriver
     * @param actions - Actions object
     */
    public static void testLoadData(SafariDriver driver, Actions actions) {
        // TEST: 'load data' button can be pressed in order to load dynamic table
        List<WebElement> buttons = driver.findElements(By.tagName("button"));

        // items in dropdown menu can be selected and visualize the table
        WebElement dropdownDiv = driver.findElement(By.xpath("/html/body/div[1]"));
        List<WebElement> dropdownMenu = dropdownDiv.findElements(By.tagName("option"));
        // go through each possible database

        // TEST: data can be properly loaded from horoscope table
        dropdownMenu.get(0).click();
        System.out.println("Now testing table: " + dropdownMenu.get(0).getText());
        System.out.println(" ");
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver);

        // TEST: data can be properly loaded from ta_horoscope table
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        dropdownMenu.get(1).click();
        System.out.println("Now testing table: " + dropdownMenu.get(1).getText());
        System.out.println(" ");
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver);

        // TEST: data can be properly loaded from tas table
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        dropdownMenu.get(2).click();
        System.out.println("Now testing table: " + dropdownMenu.get(2).getText());
        System.out.println(" ");
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver);

        // TEST: data can be properly loaded from sqlite_sequence table
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        dropdownMenu.get(3).click();
        System.out.println("Now testing table: " + dropdownMenu.get(3).getText());
        System.out.println(" ");
        buttons.get(0).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits
        testTable(driver);

        System.out.println("- - - - - - - - - - - - - - - - - - - -");
    }

    /**
     * Tests that all input fields present on the page can be interacted with
     * @param driver - SafariDriver
     * @param actions- Actions object
     */
    public static void testInputs(SafariDriver driver, Actions actions) {
        // tests the text boxes for adding, deleting, and updating table values
        List<WebElement> inputs = driver.findElements(By.tagName("INPUT"));
        String text = "1";

        // TEST: text can be inserted into the "Add" Text Box
        actions.moveToElement(inputs.get(0));
        actions.sendKeys(text);
        actions.build().perform();
        actions.moveToElement(inputs.get(1));
        actions.sendKeys(text);
        actions.build().perform();
        System.out.println("Tested inserting into 'Add' textbox");

        // TEST: text can be inserted into the "Delete" Text Box
        actions.moveToElement(inputs.get(2));
        actions.sendKeys(text);
        actions.build().perform();
        System.out.println("Tested inserting into 'Delete' textbox");

        // TEST: text can be inserted into the "Update" Text Box
        actions.moveToElement(inputs.get(3));
        actions.sendKeys(text);
        actions.build().perform();
        actions.moveToElement(inputs.get(4));
        actions.sendKeys(text);
        actions.build().perform();
        actions.moveToElement(inputs.get(5));
        actions.sendKeys(text);
        actions.build().perform();
        System.out.println("Tested inserting into 'Update' textbox");
    }

    /**
     * Tests that the tables generated by the different datasets have the correct number of rows and columns,
     * as well as cell values
     * @param driver - SafariDriver
     */
    public static void testTable(SafariDriver driver) {
        // TEST: Finding number of Rows
        List<WebElement> rowsNumber = driver.findElements(By.xpath("/html/body/div[2]/table/tbody/tr"));
        int rowCount = rowsNumber.size();
        System.out.println("No of rows in this table : " + rowCount);


        // TEST: Finding number of columns
        List<WebElement> cols = driver.findElements(By.xpath("/html/body/div[2]/table/tbody/tr[1]/td"));
        int colCount = cols.size();
        System.out.println("No of columns in this table : " + colCount);


        // TEST: Locating all the values in the table

        // locating table
        WebElement tableValues = driver.findElement(By.xpath("/html/body/div[2]/table/tbody"));
        // extracting rows
        List<WebElement> allRows = tableValues.findElements(By.tagName("tr"));
        int totalRowcount = allRows.size();

        // looping through rows
        for (int row = 0; row < totalRowcount; row++) {
            List<WebElement> allCols = allRows.get(row).findElements(By.tagName("td"));
            for (int col = 0; col< colCount;col++) {
                String cellText = allCols.get(col).getText();
                System.out.println("Cell value of row number "+ row + " and column number " + col + " is: " + cellText);
            }
        }
    }

    /**
     * Tests that font styles are displayed correctly
     * @param driver
     */
    public static void testStyles(SafariDriver driver) {
        // TEST: checking if correct font is used for title, and for header of table
        //Locate text string element to read its font properties.
        WebElement text = driver.findElement(By.xpath("/html/body/h1[2]"));

        //Read font-size property and print It In console.
        String fontSize = text.getCssValue("font-size");
        System.out.println("Page Title Font Size -> "+fontSize);

        //Read font-family property and print It In console.
        String fontFamily = text.getCssValue("font-family");
        System.out.println("Page Title Font Family -> "+fontFamily);

        //Read text-align property and print It In console.
        String fonttxtAlign = text.getCssValue("text-align");
        System.out.println("Page Title Font Text Alignment -> "+fonttxtAlign);

        // Repeat process for header of table
        System.out.println("- - - - - - - - - - - - - - - - - - - -");
        //Locate text string element to read its font properties.
        WebElement text2 = driver.findElement(By.xpath("/html/body/div[2]/table/tbody/tr[1]"));

        //Read font-size property and print It In console.
        String fontSize2 = text2.getCssValue("font-size");
        System.out.println("Table Header Font Size -> "+fontSize2);

        //Read font-family property and print It In console.
        String fontFamily2 = text2.getCssValue("font-family");
        System.out.println("Table Header Font Family -> "+fontFamily2);

        //Read text-align property and print It In console.
        String fonttxtAlign2 = text2.getCssValue("text-align");
        System.out.println("Table Header Font Text Alignment -> "+fonttxtAlign2);
    }

}