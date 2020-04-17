package ro.mira.stad.gesint.statistics;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import ro.mira.stad.gesint.statistics.repository.converter.DataPointIdReaderConverter;
import ro.mira.stad.gesint.statistics.repository.converter.DataPointIdWriterConverter;

/**
 * @author STAD
 */
@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableDiscoveryClient
@EnableOAuth2Client
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class StatisticsApplication {

	@Configuration
	static class CustomConversionsConfig {

		@Bean
		public CustomConversions customConversions() {
			return new CustomConversions(
					Arrays.asList(new DataPointIdReaderConverter(), new DataPointIdWriterConverter()));
		}
	}

	public static void main(final String[] args) {
		SpringApplication.run(StatisticsApplication.class, args);
	}
}
