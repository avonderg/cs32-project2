package selenium;

// Remember to add the Maven dependency! You may also
// need to option-enter (alt-enter) and pick a
// "Maven: add ... to classpath" option.
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class seleniumTest {


    public static void start() throws InterruptedException {
//         There will be various messages in the console...
//         (By default, Selenium prints a lot of info)
        WebDriverManager.safaridriver().setup();
        runExample("file:///Users/alexandravondergoltz/Desktop/cs32/project-2-avonderg-nxu4-sanand14/frontend/table/table.html");

// useful documentation
// https://www.selenium.dev/documentation/webdriver/elements/finders/
    }

    public static void runExample(String path) throws InterruptedException {
        SafariOptions options = new SafariOptions();
        SafariDriver driver = new SafariDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500)); // waits

        driver.get(path);
        System.out.println(driver.getTitle());

        // lots of ways to "get an element":
//        WebElement new_round = driver.findElement(By.className("new-round"));
//        System.out.println(new_round);

        // TEST: title is loaded correctly onto page
        List<WebElement> table_titles = driver.findElements(By.tagName("h1"));
        System.out.println(table_titles.get(1).getText()); // gets the second element in the list -> corresponding to title

//        String attr = driver.switchTo().activeElement().getAttribute("content");
//        System.out.println(attr);


//        ((JavascriptExecutor) driver).executeScript("scroll(0,300)");
//        Actions ac = new Actions(driver);
//        WebElement wb = driver.findElement(By.xpath("/html/body/div[1]"));
//        ac.moveToElement(wb).build().perform();
//        Thread.sleep(3000);
//        WebElement plan = driver.findElement(By.xpath("/html/body/div[1]"));
//        plan.click();
//        Thread.sleep(2000);


        // clicks 'load data' button
        List<WebElement> buttons = driver.findElements(By.tagName("button"));
        buttons.get(0).click();
//        driver.findElement(By.id("loader")).click();
        System.out.println("Load data button has been pressed to load table data");



//        // Don't get _all_ inputs, just the inputs in the new-round div
//        List<WebElement> inputs = new_round.findElements(By.tagName("input"));
//        WebElement submit = new_round.findElement(By.tagName("button"));
//        // ^ button click, waiting , going into table to check contents
//
//
//        System.out.println(inputs);
//        int val = 1;
//        for(WebElement in : inputs) {
//            in.sendKeys(String.valueOf(val));
//            val++;
//        }
//        submit.click();
//
//        // Beware: what happens if we re-use the same WebElement values?
//        for(WebElement in : inputs) {
//            in.sendKeys(String.valueOf(val));
//            val++;
//        }
//        submit.click();
//        // We're actually OK! But this isn't guaranteed.
//        // Selenium uses locators to define where elements live;
//        // these can become stale, etc.

        driver.quit();
    }
}