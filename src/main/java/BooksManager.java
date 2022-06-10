import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import models.Author;
import models.Publication;

public class BooksManager {
  public static final DataImporter IMPORTER = new DataImporter();
  public static final CSVParser PARSER =
      new CSVParserBuilder().withSeparator(';').withIgnoreQuotations(true).build();

  HashMap<String, Author> authors;
  HashMap<String, Publication> publications;

  public void importData() {
    try {
      authors = IMPORTER.importAuthors(getCSVReader("data/autoren.csv"));
      publications = IMPORTER.importBooks(getCSVReader("data/buecher.csv"), authors);
      publications.putAll(
          IMPORTER.importMagazines(getCSVReader("data/zeitschriften.csv"), authors));
    } catch (CsvValidationException | IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public void openBookstore() {
    System.out.println("Welcome to our Bookstore");
    printInsructions();
    Scanner in = new Scanner(System.in);
    int number = 0;
    while ((number = in.nextInt()) != 0) {
      if (number == 1) {
        publications.values().forEach(System.out::println);
      } else if (number == 2) {
        getSortedPublications().forEach(System.out::println);
      } else if (number == 3) {
        System.out.println("Please enter the ISBN number");
        System.out.println(getPublicationInfo(in.next()));
      } else if (number == 4) {
        System.out.println("Please enter the Author name");
        getAuthorPublications(in.next()).forEach(System.out::println);
      } else {
        System.out.println("Please enter the correct value");
        printInsructions();
        continue;
      }
      System.out.println("----------------------------");
      printInsructions();
    }
  }

  private List<Publication> getAuthorPublications(String authorName) {
    List<Publication> publications =
        authors.values().stream()
            .filter(author -> checkAuthor(authorName, author))
            .map(Author::getPublicationList)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
    return publications;
  }

  private boolean checkAuthor(String authorName, Author author) {
    return author.getFirstName().concat(author.getLastName()).contains(authorName);
  }

  private String getPublicationInfo(String isbn) {
    Publication publication = publications.get(isbn);
    String PublicationInfo =
        publication != null ? publication.toString() : "No book/magazine was found";
    return PublicationInfo;
  }

  private List<Publication> getSortedPublications() {
    List<Publication> publicationList = new ArrayList<>(publications.values());
    publicationList.sort(
        (Publication publication1, Publication publication2) ->
            publication1.getTitle().compareTo(publication2.getTitle()));
    return publicationList;
  }

  private void printInsructions() {
    System.out.println("Press 1 - to get a list of all books and magazines");
    System.out.println("Press 2 - to get a sorted list of all books and magazines");
    System.out.println("Press 3 - to get a book / magazine with ISBN number");
    System.out.println("Press 4 - to get a book / magazine with Author name");
  }

  private CSVReader getCSVReader(String path) throws IOException, URISyntaxException {
    Reader reader = Files.newBufferedReader(Paths.get(ClassLoader.getSystemResource(path).toURI()));
    return new CSVReaderBuilder(reader).withSkipLines(1).withCSVParser(PARSER).build();
  }
}
