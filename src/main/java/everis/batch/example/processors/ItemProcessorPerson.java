package everis.batch.example.processors;

import everis.batch.example.dto.PersonDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * The {@link ItemProcessorPerson} is a ItemProcessor.
 *
 * @author Hans Rolando Ramos Aguilar
 * @since 0.0.1
 */
public class ItemProcessorPerson implements ItemProcessor<PersonDTO, PersonDTO> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ItemProcessorPerson.class);

  @Override
  public PersonDTO process(PersonDTO personDTO) throws Exception {
    LOGGER.info("View a person: {}", personDTO.getFirstName() + "," + personDTO.getLastName());
    return personDTO;
  }
}
