package ro.mira.stad.gesint.notification;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

import ro.mira.stad.gesint.notification.repository.converter.FrequencyReaderConverter;
import ro.mira.stad.gesint.notification.repository.converter.FrequencyWriterConverter;

/**
 * @author STAD
 */
@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableDiscoveryClient
@EnableOAuth2Client
@EnableFeignClients
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableScheduling
public class NotificationServiceApplication {

	@Configuration
	static class CustomConversionsConfig {

		@Bean
		public CustomConversions customConversions() {
			return new CustomConversions(Arrays.asList(new FrequencyReaderConverter(), new FrequencyWriterConverter()));
		}
	}

	public static void main(final String[] args) {
		SpringApplication.run(NotificationServiceApplication.class, args);
	}
}
