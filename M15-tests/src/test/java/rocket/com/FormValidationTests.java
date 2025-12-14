package rocket.com;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


import static org.junit.jupiter.api.Assertions.*;

public class FormValidationTests {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    // --- M15 Contact Form Test ---
    @Test
    void testContactForm_Filling() {
        driver.get("http://localhost:5500/M3/index.html"); // or contact page

        driver.findElement(By.id("fullname")).sendKeys("Aaron Calkins");
        driver.findElement(By.id("email")).sendKeys("aaron@test.com");
        driver.findElement(By.id("phone")).sendKeys("5551234567");
        driver.findElement(By.id("company_name")).sendKeys("Codeboxx");
        driver.findElement(By.id("project_name")).sendKeys("Elevator Project");
        driver.findElement(By.id("project_desc")).sendKeys("Install new residential elevators");

        Select department = new Select(driver.findElement(By.id("department")));
        department.selectByVisibleText("Residential");

        driver.findElement(By.id("message")).sendKeys("Please contact me for details.");

        driver.findElement(By.cssSelector("#contact-form button[type='submit']")).click();
        // You could add assertions to check the success modal here
    }

    // --- Responsiveness Test for M9 Home Page ---
    @Test
    void testHomePage_Responsiveness() {
        driver.get("http://127.0.0.1:5173/login");

        // Fill login form
        driver.findElement(By.id("email")).sendKeys("aaron.calkins123@gmail.com");
        driver.findElement(By.id("password")).sendKeys("1234");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Wait until home page loads and navbar is visible
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement navbar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".navbar")));

        // Test responsiveness
        driver.manage().window().setSize(new Dimension(1920, 1080));
        assertTrue(navbar.isDisplayed());

        driver.manage().window().setSize(new Dimension(768, 1024));
        assertTrue(navbar.isDisplayed());

        driver.manage().window().setSize(new Dimension(375, 667));
        assertTrue(navbar.isDisplayed());


    }
    @Test
        void testQuoteForm_FillOut() {
            driver.get("http://localhost:5500/M3/quote.html");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // --- STEP 1: Select Building Type ---
            WebElement buildingType = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("building-type"))
            );
            buildingType.sendKeys("Residential"); // selects Residential

            // --- STEP 2: Wait for Step 2 card to appear ---
            WebElement step2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("step2")));
            assertTrue(step2.isDisplayed(), "Step 2 card should be displayed");

            // Wait for parent divs to remove 'd-none' and be visible
            WebElement floorsDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#number-of-floors")));
            WebElement basementsDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#number-of-basements")));
            WebElement apartmentsDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#number-of-apartments")));

            // Scroll each input into view and enter values
            WebElement floors = floorsDiv.findElement(By.tagName("input"));
            js.executeScript("arguments[0].scrollIntoView(true);", floors);
            floors.sendKeys("10");

            WebElement basements = basementsDiv.findElement(By.tagName("input"));
            js.executeScript("arguments[0].scrollIntoView(true);", basements);
            basements.sendKeys("2");

            WebElement apartments = apartmentsDiv.findElement(By.tagName("input"));
            js.executeScript("arguments[0].scrollIntoView(true);", apartments);
            apartments.sendKeys("20");

            // --- STEP 3: Select Product Line ---
            WebElement standardRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id("standard")));
            js.executeScript("arguments[0].scrollIntoView(true);", standardRadio);
            standardRadio.click();

            // --- STEP 4: Check Pricing Display ---
            WebElement unitPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#elevator-unit-price input")));
            WebElement totalPrice = driver.findElement(By.cssSelector("#elevator-total-price input"));
            WebElement installationFee = driver.findElement(By.cssSelector("#installation-fees input"));
            WebElement finalPrice = driver.findElement(By.cssSelector("#final-price input"));

            // Scroll each pricing input into view and assert
            js.executeScript("arguments[0].scrollIntoView(true);", unitPrice);
            assertTrue(unitPrice.isDisplayed());

            js.executeScript("arguments[0].scrollIntoView(true);", totalPrice);
            assertTrue(totalPrice.isDisplayed());

            js.executeScript("arguments[0].scrollIntoView(true);", installationFee);
            assertTrue(installationFee.isDisplayed());

            js.executeScript("arguments[0].scrollIntoView(true);", finalPrice);
            assertTrue(finalPrice.isDisplayed());
        }
    }

