package telran.ashkelon2020.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BookConfiguration {
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper
		  .getConfiguration () // Получить конфигурацию
		  .setFieldMatchingEnabled ( true )   // Включает сопоставление полей
		  .setFieldAccessLevel ( org.modelmapper.config . Configuration . AccessLevel . PRIVATE );
//		  .setMatchingStrategy(MatchingStrategies.LOOSE);
		  return modelMapper;
	}

}
