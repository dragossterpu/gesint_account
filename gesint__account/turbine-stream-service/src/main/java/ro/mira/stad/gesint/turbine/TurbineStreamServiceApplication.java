package ro.mira.stad.gesint.turbine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

/**
 * @author STAD
 */
@SpringBootApplication
@EnableTurbineStream
@EnableDiscoveryClient
public class TurbineStreamServiceApplication {

	public static void main(final String[] args) {
		SpringApplication.run(TurbineStreamServiceApplication.class, args);
	}
}
