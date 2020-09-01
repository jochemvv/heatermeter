package nl.tabitsolutions.heatermeter.web.rest;

import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import nl.tabitsolutions.heatermeter.components.actuators.Fan;
import nl.tabitsolutions.heatermeter.components.sensors.TemperatureSensorsService;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heater-meter")
@OpenAPIDefinition(
        info = @Info(
                title = "HeaterMeter API",
                version = "0.01-DRAFT",
                description = "HeaterMeter API"
        )
)
public class HeaterMeterRestController {

    public final TemperatureSensorsService sensorsService;
    public final Fan fan;

    public HeaterMeterRestController(TemperatureSensorsService sensorsService,
                                     Fan fan) {
        this.sensorsService = sensorsService;
        this.fan = fan;
    }

    @Get("/health")
    public String health() {
        return "up";
    }

    @Get("/reading/{sensorIdentifier}")
    @Schema(name="SensorValue")
    public SensorValue<Long> getReading(@PathVariable("sensorIdentifier") String sensorIdentifier) {
        return sensorsService.getReadingFrom(sensorIdentifier);
    }

    @Get("/fanOn")
    public String fanOn() {
        fan.turnOn();
        return "ok";
    }

    @Get("/fanOff")
    public String fanOff() {
        fan.turnOff();
        return "ok";
    }
}
