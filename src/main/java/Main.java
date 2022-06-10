import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    BooksManager booksManager = new BooksManager();
    booksManager.importData();
    booksManager.openBookstore();
  }
}
