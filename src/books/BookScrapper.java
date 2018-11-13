package books;

import books.models.Book;
import com.sun.javafx.PlatformUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BookScrapper {

    private String FNAC_URL = "https://www.fnac.es";
    private String AMAZON_URL = "https://www.amazon.es/b?ie=UTF8&node=667333031";

    private WebDriver driver = null;

    private void launchFirefox(String pageUrl) {
        System.setProperty("webdriver.gecko.driver", getDriverPath());
        driver = new FirefoxDriver();
        driver.get(pageUrl);
        driver.manage().window().maximize();
    }

    private String getDriverPath() {
        Path currentRelativePath = Paths.get("");
        String projectRootPath = currentRelativePath.toAbsolutePath().toString();
        File projectRootPathAsFile = new File(projectRootPath);
        File binPathFile = new File(projectRootPathAsFile, "bin");
        File driverPath;
        if (PlatformUtil.isWindows()) {
            driverPath = new File(binPathFile, "geckodriver-windows.exe");
        } else {
            driverPath = new File(binPathFile, "geckodriver-mac");
        }
        return driverPath.getAbsolutePath();
    }

    List<Book> searchFnac(String searchTitle, String searchAuthor) {
        if ((searchTitle == null || searchTitle.isEmpty()) && (searchAuthor == null || searchAuthor.isEmpty()))
            throw new IllegalArgumentException("Title and author are null or empty, provide one of them");

        launchFirefox(FNAC_URL);

        WebDriverWait waiting = new WebDriverWait(driver, 10);

        WebElement categoryBox = driver.findElement(By.className("Header__aisle"));
        categoryBox.click();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.className("select-content")));

        WebElement bookCategory = driver.findElements(By.className("select-option")).get(1);
        bookCategory.click();

        WebElement searchBox = driver.findElement(By.id("Fnac_Search"));
        searchBox.sendKeys(String.format("%s %s", searchTitle, searchAuthor));
        searchBox.submit();

        waiting.until(ExpectedConditions.presenceOfElementLocated(By.id("dontTouchThisDiv")));

        List<WebElement> booksElementsList = driver.findElements(By.xpath("//li[@class='clearfix Article-item js-ProductList']"));
        List<Book> booksList = new ArrayList<>();

        for (WebElement book : booksElementsList) {
            String title = book.findElement(By.className("js-minifa-title")).getText();
            String author = book.findElement(By.className("Article-descSub")).getText();
            String price = book.findElement(By.className("userPrice")).getText();

            String previousPrice;
            try {
                previousPrice = book.findElement(By.className("oldPrice")).getText();
            } catch (Exception e) {
                previousPrice = null;
            }
            booksList.add(new Book(title, author, price, previousPrice));
        }

        return booksList;
    }

    List<Book> searchAmazon(String searchTitle, String searchAuthor) {
        if ((searchTitle == null || searchTitle.isEmpty()) && (searchAuthor == null || searchAuthor.isEmpty()))
            throw new IllegalArgumentException("Title and author are null or empty, provide one of them");

        launchFirefox(AMAZON_URL);

        WebElement titleBox = driver.findElement(By.xpath("//div[3]/input[@class='asField' and 1]"));
        titleBox.sendKeys(searchTitle);

        if (searchAuthor != null) {
            WebElement authorBox = driver.findElement(By.xpath("//div[2]/input[@class='asField' and 1]"));
            authorBox.sendKeys(searchAuthor);
        }

        titleBox.submit();

        WebDriverWait waiting = new WebDriverWait(driver, 10);
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.id("centerMinus")));

        List<WebElement> booksElementsList = driver.findElements(By.xpath("//div[@class='a-fixed-left-grid-inner']"));
        List<Book> booksList = new ArrayList<>();

        for (WebElement book : booksElementsList) {
            String title = book.findElement(By.xpath("//h2")).getText();
            String author = book.findElement(By.xpath("//span/a[@class='a-link-normal a-text-normal' and 1]")).getText();
            String price = book.findElement(By.xpath("//span[@class='a-size-base a-color-price s-price a-text-bold' and 2]")).getText();
            booksList.add(new Book(title, author, price, null));
        }

        return booksList;
    }
}
