//package tablevistesting;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import static org.junit.Assert.assertEquals;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.By;
//import org.openqa.selenium.remote.RemoteWebDriver;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.openqa.selenium.support.Color;
//
//import org.openqa.selenium.support.ui.Select;
//
//import java.time.Duration;
//import java.util.List;
//
//
//public class ReactTableTest {
//  private RemoteWebDriver driver = null;
//  private String indexPath = "http://localhost:3000/";
//
//  @Before
//  public void setup() {
//    WebDriverManager.chromedriver().setup();
//
//    ChromeOptions options = new ChromeOptions();
//    this.driver = new ChromeDriver(options);
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//  }
//
//  @After
//  public void tearDown() {
//    driver.quit();
//  }
//
//  @Test
//  public void testLoadedDB() {
//    driver.get(indexPath);
//    assertEquals(driver.getTitle(), "React App");
//
//    Select drpDB = new Select(driver.findElement(By.className("selectTable")));
//
//    List<WebElement> optionList = drpDB.getOptions();
//    assertEquals(5, optionList.size());
//
//    drpDB.selectByVisibleText("actor");
//    WebElement loadButton = driver.findElement(By.className("awesomeButton"));
//    loadButton.click();
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    List<WebElement> actorRows = driver.findElements(By.tagName("tr"));
//    assertEquals(34, actorRows.size());
//
//    drpDB.selectByVisibleText("name_lookup");
//    loadButton.click();
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    List<WebElement> nameLookupRows = driver.findElements(By.tagName("tr"));
//    assertEquals(30, nameLookupRows.size());
//
//    drpDB.selectByVisibleText("actor_film");
//    loadButton.click();
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    List<WebElement> actorFilmRows = driver.findElements(By.tagName("tr"));
//    assertEquals(30, actorFilmRows.size());
//
//    drpDB.selectByVisibleText("film");
//    loadButton.click();
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    List<WebElement> filmRows = driver.findElements(By.tagName("tr"));
//    assertEquals(152, filmRows.size());
//  }
//
//  @Test
//  public void testInsertUpdateDelete() {
//    driver.get(indexPath);
//    Select drpDB = new Select(driver.findElement(By.className("selectTable")));
//    drpDB.selectByVisibleText("actor");
//    WebElement loadButton = driver.findElement(By.className("awesomeButton"));
//    loadButton.click();
//
//    WebElement insertButton = driver.findElement(By.className("insertButton"));
//    List<WebElement> inputBoxes = driver.findElements(By.className("inputBox"));
//    inputBoxes.get(0).sendKeys("New Actor");
//    inputBoxes.get(1).sendKeys("Neil Xu");
//    insertButton.click();
//
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    List<WebElement> actorRows = driver.findElements(By.tagName("tr"));
//    assertEquals(35, actorRows.size());
//
//    WebElement newRow = driver.findElement(By.id("34"));
//    List<WebElement> newCells = newRow.findElements(By.tagName("td"));
//    String newId = newCells.get(1).getText();
//    String newName = newCells.get(2).getText();
//
//    assertEquals("New Actor", newId);
//    assertEquals("Neil Xu", newName);
//
//    WebElement updateButton = driver.findElement(By.className("updateButton"));
//    List<WebElement> updateBoxes = driver.findElements(By.className("updateBox"));
//    updateBoxes.get(0).sendKeys("34");
//    updateBoxes.get(1).sendKeys("New Actor 2");
//    updateBoxes.get(2).sendKeys("Suraj Anand");
//    updateButton.click();
//
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    WebElement updatedRow = driver.findElement(By.id("34"));
//    List<WebElement> updatedCells = updatedRow.findElements(By.tagName("td"));
//    System.out.println("got update cells, num update cells: " + Integer.toString(updatedCells.size()));
//    String updatedId = updatedCells.get(1).getText();
//    String updatedName = updatedCells.get(2).getText();
//
//    assertEquals("New Actor 2", updatedId);
//    assertEquals("Suraj Anand", updatedName);
//
//    WebElement deletedRows = driver.findElement(By.id("34"));
//    WebElement deleteButton = deletedRows.findElement(By.className("deleteButton"));
//    deleteButton.click();
//    driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
//    List<WebElement> finalRows = driver.findElements(By.tagName("tr"));
//
//    assertEquals(34, finalRows.size());
//  }
//}
