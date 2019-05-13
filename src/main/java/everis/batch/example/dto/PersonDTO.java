package everis.batch.example.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * The {@link PersonDTO} class is an Abstract Data Type (ADT) modeling a person.
 *
 * @author Hans Rolando Ramos Aguilar
 * @see lombok
 * @since 0.0.1
 */
@Getter
@Setter
public class PersonDTO {
  String id;
  String firstName;
  String lastName;
}
