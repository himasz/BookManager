package models;

import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
public class Publication {
  String title;
  String isbn;
  String description;
  String publishDate;
  List<Author> authors;

  @Override
  public String toString() {
    return "title='"
        + title
        + ", isbn='"
        + isbn
        + ", description='"
        + description
        + ", publishDate='"
        + publishDate
        + ", authors="
        + authors;
  }
}
