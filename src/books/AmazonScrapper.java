package books;

import books.models.Book;
import books.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class AmazonScrapper {
    private WebDriver driver = null;

    private String URL = "https://www.amazon.es/b?ie=UTF8&node=667333031";

    private void launchFirefox(String pageUrl) {
        System.setProperty("webdriver.gecko.driver", Utils.getDriverPath());
        driver = new FirefoxDriver();
        driver.get(pageUrl);
        driver.manage().window().maximize();
    }

    List<Book> search(String searchTitle, String searchAuthor) {
        if ((searchTitle == null || searchTitle.isEmpty()) && (searchAuthor == null || searchAuthor.isEmpty()))
            throw new IllegalArgumentException("Title and author are null or empty, provide one of them");

        launchFirefox(URL);

        WebElement titleBox = driver.findElement(By.xpath("//div[3]/input[@class='asField' and 1]"));
        titleBox.sendKeys(searchTitle);

        if (searchAuthor != null) {
            WebElement authorBox = driver.findElement(By.xpath("//div[2]/input[@class='asField' and 1]"));
            authorBox.sendKeys(searchAuthor);
        }

        titleBox.submit();

        WebDriverWait waiting = new WebDriverWait(driver, 10);
        waiting.until(ExpectedConditions.presenceOfElementLocated(By.id("centerMinus")));

        List<Book> booksList = new ArrayList<>(getBooksFromPageAmazon());

        while (amazonHasNextPage()) {
            amazonChangePage();
            booksList.addAll(getBooksFromPageAmazon());
        }

        return booksList;
    }

    private List<Book> getBooksFromPageAmazon() {
        List<WebElement> booksElementsList = driver.findElements(By.xpath("//div[@class='a-fixed-left-grid-inner']"));
        List<Book> booksList = new ArrayList<>();

        for (WebElement book : booksElementsList) {
            try {
                String title = book.findElement(By.xpath("//h2")).getText();
                String author = book.findElement(By.xpath("//span/a[@class='a-link-normal a-text-normal' and 1]")).getText();
                String price = book.findElement(By.xpath("//span[@class='a-size-base a-color-price s-price a-text-bold' and 2]")).getText();
                booksList.add(new Book(title, author, price, null));
            } catch (Exception ignored) {
            }

        }

        return booksList;
    }

    private boolean amazonHasNextPage() {
        try {
            driver.findElement(By.className("pagnRA1"));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void amazonChangePage() {
        driver.findElement(By.id("pagnNextString")).click();
    }
}
