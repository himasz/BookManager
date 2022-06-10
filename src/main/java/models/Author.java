package models;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
public class Author {
  String email;
  String firstName;
  String lastName;
  List<Publication> publicationList = new ArrayList<>();

  @Override
  public String toString() {
    return "email='" + email + ", firstName='" + firstName + ", lastName='" + lastName;
  }
}
