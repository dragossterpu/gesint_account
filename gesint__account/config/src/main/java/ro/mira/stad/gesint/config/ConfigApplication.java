package ro.mira.stad.gesint.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author STAD
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigApplication {

	public static void main(final String[] args) {
		SpringApplication.run(ConfigApplication.class, args);
	}
}
