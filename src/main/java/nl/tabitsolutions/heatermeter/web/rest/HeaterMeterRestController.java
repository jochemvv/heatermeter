package nl.tabitsolutions.heatermeter.web.rest;

import nl.tabitsolutions.heatermeter.components.sensors.TemperatureSensorsService;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heater-meter")
public class HeaterMeterRestController {

    public final TemperatureSensorsService sensorsService;

    public HeaterMeterRestController(TemperatureSensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @GetMapping("/health")
    public String health() {
        return "up";
    }

    @GetMapping("/reading/{sensorIdentifier}")
    public SensorValue<?> getReading(@PathVariable("sensorIdentifier") String sensorIdentifier) {
        return sensorsService.getReadingFrom(sensorIdentifier);
    }
}
