package books.models;

public class Book {
    private String title;
    private String price;
    private String author;
    private String previousPrice;

    public Book(String title, String author, String price, String previousPrice) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.previousPrice = previousPrice;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(String previousPrice) {
        this.previousPrice = previousPrice;
    }
}
