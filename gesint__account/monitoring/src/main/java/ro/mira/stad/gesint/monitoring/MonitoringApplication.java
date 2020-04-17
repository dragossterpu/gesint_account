package ro.mira.stad.gesint.monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author STAD
 */
@SpringBootApplication
@EnableHystrixDashboard
public class MonitoringApplication {

	public static void main(final String[] args) {
		SpringApplication.run(MonitoringApplication.class, args);
	}
}
