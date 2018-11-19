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

public class FnacScrapper {
    private WebDriver driver = null;

    private String URL = "https://www.fnac.es";

    private void launchFirefox(String pageUrl) {
        System.setProperty("webdriver.gecko.driver", Utils.getDriverPath());
        driver = new FirefoxDriver();
        driver.get(pageUrl);
        driver.manage().window().maximize();
    }

    List<Book> search(String searchTitle, String searchAuthor) {
        if ((searchTitle == null || searchTitle.isEmpty()) && (searchAuthor == null || searchAuthor.isEmpty()))
            throw new IllegalArgumentException("Title and author are null or empty, provide one of them");

        if (searchTitle == null) {
            searchTitle = "";
        }
        if (searchAuthor == null) {
            searchAuthor = "";
        }

        launchFirefox(URL);

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

        List<Book> booksList = new ArrayList<>(getBooksFromPageFnac());

        while (fnacHasNextPage()) {
            fnacChangePage();
            booksList.addAll(getBooksFromPageFnac());
        }

        if (driver != null) driver.close();

        return booksList;
    }

    private List<Book> getBooksFromPageFnac() {
        List<WebElement> booksElementsList = driver.findElements(By.xpath("//li[@class='clearfix Article-item js-ProductList']"));
        List<Book> booksList = new ArrayList<>();

        for (WebElement book : booksElementsList) {
            try {
                String title = book.findElement(By.className("js-minifa-title")).getText();
                String author = book.findElement(By.className("Article-descSub")).getText();
                String price = book.findElement(By.className("userPrice")).getText();

                String previousPrice;
                try {
                    previousPrice = book.findElement(By.className("oldPrice")).getText();
                } catch (Exception e) {
                    previousPrice = "X";
                }
                if (!previousPrice.equals("X")) {
                    String aux = previousPrice;
                    previousPrice = price;
                    price = aux;
                }
                booksList.add(new Book(title, author, price, previousPrice, "Fnac"));
            } catch (Exception ignored) {
            }
        }
        return booksList;
    }

    private boolean fnacHasNextPage() {
        try {
            driver.findElement(By.cssSelector("li[class='nextLevel1 hide']"));
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    private void fnacChangePage() {
        try {
            driver.findElements(By.className("nextLevel1")).get(0).click();
        } catch (Exception e) {
        }
    }
}
