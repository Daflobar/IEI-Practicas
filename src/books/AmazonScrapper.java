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
                if (title.equals("")) title = "X";
                if (author.equals("")) author = "X";
                if (price.equals("")) price = "X";


                booksList.add(new Book(title, author, price, "X", "Amazon"));
            } catch (Exception ignored) {
            }

        }

        return booksList;
    }

    private boolean amazonHasNextPage() {
        try {
            //Si lo encuentra no tiene siguiente página
            driver.findElement(By.className("pagnRA1"));
            return false;
        } catch (Exception e) {
            String results = driver.findElement(By.id("s-result-count")).getText().split(":")[0];
            if (results.contains("de")) {
                //Si contiene 'de' tiene página siguiente
                return true;
            } else {
                //Si no contiene 'de no tiene página siguiente y es solo una pagina");
                return false;
            }
        }
    }

    private void amazonChangePage() {
        try{
            driver.findElement(By.cssSelector("span[class='srSprite pagnNextArrow']")).click();
        } catch (Exception e) {}
    }
}
