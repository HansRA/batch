package everis.batch.example.step;

import everis.batch.example.dto.PersonDTO;
import everis.batch.example.mapper.PersonMapper;
import everis.batch.example.processors.ItemProcessorPerson;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * The {@link StepConfig} is the class that contains the configuration of all the steps that will
 * execute, in addition to the order of execution of these steps.
 *
 * @author Hans Rolando Ramos Aguilar
 * @since 0.0.1
 */
@Configuration
@EnableBatchProcessing
public class StepConfig extends DefaultBatchConfigurer {

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  PersonMapper personMapper;

  //false method to not use database, spring batch by default asks you for a bd
  @Override
  public void setDataSource(DataSource dataSource) {
  }

  //first read
  @Bean
  public FlatFileItemReader<PersonDTO> itemReaderPerson() {
    personMapper = new PersonMapper();
    FlatFileItemReader<PersonDTO> reader = new FlatFileItemReader<>();
    reader.setResource(new FileSystemResource("test_files/input.csv"));
    reader.setLinesToSkip(1);
    LineMapper<PersonDTO> personDTOLineMapper = personMapper.createPersonLineMapper();
    reader.setLineMapper(personDTOLineMapper);
    return reader;
  }

  //second process
  @Bean
  ItemProcessor<PersonDTO, PersonDTO> itemProcessorPerson() {
    return new ItemProcessorPerson();
  }

  //last write
  @Bean
  FlatFileItemWriter<PersonDTO> itemWriterPerson() {
    FlatFileItemWriter<PersonDTO> writer = new FlatFileItemWriter<>();
    writer.setResource(new FileSystemResource("test_files/output.txt"));
    DelimitedLineAggregator<PersonDTO> delLineAgg = new DelimitedLineAggregator<PersonDTO>();
    delLineAgg.setDelimiter(",");
    BeanWrapperFieldExtractor<PersonDTO> fieldExtractor = new BeanWrapperFieldExtractor<>();
    fieldExtractor.setNames(new String[]{"id", "firstName", "lastName"});
    delLineAgg.setFieldExtractor(fieldExtractor);
    writer.setLineAggregator(delLineAgg);
    return writer;
  }

  //configure the step sequence
  @Bean
  Step stepCSVtoTXT() {
    return stepBuilderFactory
        .get("stepCSVtoTXT")
        .<PersonDTO, PersonDTO>chunk(1)
        .reader(itemReaderPerson())
        .processor(itemProcessorPerson())
        .writer(itemWriterPerson())
        .build();
  }

  //job execution
  @Bean
  Job stepCSVtoTXTJOB() {
    return jobBuilderFactory.get("stepCSVtoTXTJOB")
        .incrementer(new RunIdIncrementer())
        .flow(stepCSVtoTXT())
        .end()
        .build();
  }

}
