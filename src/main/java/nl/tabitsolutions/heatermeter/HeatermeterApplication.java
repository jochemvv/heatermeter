package nl.tabitsolutions.heatermeter;

import io.micronaut.runtime.Micronaut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeatermeterApplication {

	public static void main(String[] args) {
		Micronaut.run(HeatermeterApplication.class);
//		SpringApplication.run(HeatermeterApplication.class, args);
	}
}
