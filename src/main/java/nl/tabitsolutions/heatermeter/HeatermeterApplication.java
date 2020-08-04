package nl.tabitsolutions.heatermeter;

import io.micronaut.runtime.Micronaut;
import nl.tabitsolutions.heatermeter.config.CalibrationConfiguration;
import nl.tabitsolutions.heatermeter.config.I2CConfiguration;
import nl.tabitsolutions.heatermeter.config.TestConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Import({CalibrationConfiguration.class, I2CConfiguration.class, TestConfiguration.class})
@EnableScheduling
public class HeatermeterApplication {

	public static void main(String[] args) {
		Micronaut.run(HeatermeterApplication.class);
//		SpringApplication.run(HeatermeterApplication.class, args);
	}
}
