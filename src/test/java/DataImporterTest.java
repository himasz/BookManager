import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.util.HashMap;
import models.Author;
import models.Publication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DataImporterTest {
  public static final String EMAIL = "pr-walter@optivo.de";
  public static final String FIRST_NAME = "Eb";
  public static final String LAST_NAME = "Z";
  public static final String TITLE = "Sch�ner kochen";
  public static final String DATE = "21.05.2006";
  public static final String ISBN = "5454-5587-3210";
  public static final String DESCRIPTION = "This is a book!";

  DataImporter IMPORTER = new DataImporter();
  CSVReader CSV_READER = mock(CSVReader.class);

  @Test
  void importAuthors() throws CsvValidationException, IOException {
    // given
    String example = EMAIL + ";" + FIRST_NAME + ";" + LAST_NAME;

    // when
    when(CSV_READER.readNext()).thenReturn(example.split(";")).thenReturn(null);
    HashMap<String, Author> authors = IMPORTER.importAuthors(CSV_READER);

    // then
    Assertions.assertEquals(1, authors.size());
    Author author = authors.get(EMAIL);
    Assertions.assertNotNull(author);
    Assertions.assertEquals(EMAIL, author.getEmail());
    Assertions.assertEquals(FIRST_NAME, author.getFirstName());
    Assertions.assertEquals(LAST_NAME, author.getLastName());
  }

  @Test
  void importBooks() throws CsvValidationException, IOException {
    // given
    String example = TITLE + ";" + ISBN + ";" + EMAIL + ";" + DESCRIPTION;

    // when
    when(CSV_READER.readNext()).thenReturn(example.split(";")).thenReturn(null);
    HashMap<String, Publication> books = IMPORTER.importBooks(CSV_READER, getAuthors());

    // then
    Assertions.assertEquals(1, books.size());
    Publication book = books.get(ISBN);
    Assertions.assertNotNull(book);
    Assertions.assertEquals(TITLE, book.getTitle());
    Assertions.assertEquals(ISBN, book.getIsbn());
    Assertions.assertEquals(DESCRIPTION, book.getDescription());
    Assertions.assertEquals(1, book.getAuthors().size());

    Author author = book.getAuthors().get(0);
    Assertions.assertEquals(EMAIL, author.getEmail());
    Assertions.assertEquals(FIRST_NAME, author.getFirstName());
    Assertions.assertEquals(LAST_NAME, author.getLastName());
  }

  @Test
  void importMagazines() throws CsvValidationException, IOException {
    // given
    String example = TITLE + ";" + ISBN + ";" + EMAIL + ";" + DATE;

    // when
    when(CSV_READER.readNext()).thenReturn(example.split(";")).thenReturn(null);
    HashMap<String, Publication> magazines = IMPORTER.importMagazines(CSV_READER, getAuthors());

    // then
    Assertions.assertEquals(1, magazines.size());
    Publication magazine = magazines.get(ISBN);
    Assertions.assertNotNull(magazine);
    Assertions.assertEquals(TITLE, magazine.getTitle());
    Assertions.assertEquals(ISBN, magazine.getIsbn());
    Assertions.assertEquals(DATE, magazine.getPublishDate());
    Assertions.assertEquals(1, magazine.getAuthors().size());

    Author author = magazine.getAuthors().get(0);
    Assertions.assertEquals(EMAIL, author.getEmail());
    Assertions.assertEquals(FIRST_NAME, author.getFirstName());
    Assertions.assertEquals(LAST_NAME, author.getLastName());
  }

  private HashMap<String, Author> getAuthors() {
    HashMap<String, Author> authors = new HashMap<>();
    authors.put(EMAIL, createAuthor());
    return authors;
  }

  private Author createAuthor() {
    Author author = new Author();
    author.setEmail(EMAIL);
    author.setFirstName(FIRST_NAME);
    author.setLastName(LAST_NAME);
    return author;
  }
}
