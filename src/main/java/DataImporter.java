import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import models.Author;
import models.Publication;

@NoArgsConstructor
public class DataImporter {

  public HashMap<String, Author> importAuthors(CSVReader csvReader)
      throws CsvValidationException, IOException {
    HashMap<String, Author> authors = new HashMap<>();
    String[] csvAuthorRow;
    while ((csvAuthorRow = csvReader.readNext()) != null) {
      authors.put(csvAuthorRow[0], createAuthor(csvAuthorRow));
    }
    csvReader.close();

    return authors;
  }

  public HashMap<String, Publication> importBooks(
      CSVReader csvReader, HashMap<String, Author> authors)
      throws CsvValidationException, IOException {
    HashMap<String, Publication> books = new HashMap<>();
    String[] csvBookRow;
    while ((csvBookRow = csvReader.readNext()) != null) {
      List<Author> PublicationAuthors = getAuthorsList(csvBookRow[2], authors);
      Publication book =
          Publication.builder()
              .title(csvBookRow[0])
              .isbn(csvBookRow[1])
              .authors(PublicationAuthors)
              .description(csvBookRow[3])
              .build();
      books.put(csvBookRow[1], book);
      connectToAuthors(book, PublicationAuthors);
    }
    csvReader.close();
    return books;
  }

  private void connectToAuthors(Publication book, List<Author> PublicationAuthors) {
    PublicationAuthors.forEach(
        PublicationAuthor -> PublicationAuthor.getPublicationList().add(book));
  }

  public HashMap<String, Publication> importMagazines(
      CSVReader csvReader, HashMap<String, Author> authors)
      throws CsvValidationException, IOException {
    HashMap<String, Publication> magazines = new HashMap<>();
    String[] csvMagazineRow;
    while ((csvMagazineRow = csvReader.readNext()) != null) {
      List<Author> PublicationAuthors = getAuthorsList(csvMagazineRow[2], authors);
      Publication magazine =
          Publication.builder()
              .title(csvMagazineRow[0])
              .isbn(csvMagazineRow[1])
              .authors(PublicationAuthors)
              .publishDate(csvMagazineRow[3])
              .build();
      magazines.put(csvMagazineRow[1], magazine);
      connectToAuthors(magazine, PublicationAuthors);
    }
    csvReader.close();
    return magazines;
  }

  private Author createAuthor(String[] line) {
    Author author = new Author();
    author.setEmail(line[0]);
    author.setFirstName(line[1]);
    author.setLastName(line[2]);
    return author;
  }

  private List<Author> getAuthorsList(String emails, HashMap<String, Author> authors) {
    return Arrays.stream(emails.split(",")).map(authors::get).collect(Collectors.toList());
  }
}
