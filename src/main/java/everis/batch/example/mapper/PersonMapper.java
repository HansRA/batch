package everis.batch.example.mapper;

import everis.batch.example.dto.PersonDTO;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;

/**
 * The {@link PersonMapper} is a mapper for person.
 *
 * @author Hans Rolando Ramos Aguilar
 * @since 0.0.1
 */
public class PersonMapper {

  public LineMapper<PersonDTO> createPersonLineMapper() {
    DefaultLineMapper<PersonDTO> personLineMapper = new DefaultLineMapper<>();

    LineTokenizer personLineTokenizer = createPersonLineTokenizer();
    personLineMapper.setLineTokenizer(personLineTokenizer);

    FieldSetMapper<PersonDTO> personInformationMapper = createPersonInformationMapper();
    personLineMapper.setFieldSetMapper(personInformationMapper);

    return personLineMapper;
  }

  public LineTokenizer createPersonLineTokenizer() {
    DelimitedLineTokenizer personLineTokenizer = new DelimitedLineTokenizer();
    personLineTokenizer.setDelimiter(",");
    personLineTokenizer.setNames(new String[]{"id", "firstName", "lastName"});
    return personLineTokenizer;
  }

  public FieldSetMapper<PersonDTO> createPersonInformationMapper() {
    BeanWrapperFieldSetMapper<PersonDTO> personInformationMapper = new BeanWrapperFieldSetMapper<>();
    personInformationMapper.setTargetType(PersonDTO.class);
    return personInformationMapper;
  }
}
