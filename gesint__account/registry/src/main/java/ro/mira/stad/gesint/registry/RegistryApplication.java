package ro.mira.stad.gesint.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author STAD
 */
@SpringBootApplication
@EnableEurekaServer
public class RegistryApplication {

	public static void main(final String[] args) {
		SpringApplication.run(RegistryApplication.class, args);
	}
}
