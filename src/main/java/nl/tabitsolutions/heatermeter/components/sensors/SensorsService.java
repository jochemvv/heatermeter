package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Sensor;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class SensorsService {

    private final Map<String, Sensor<?>> sensors;

    public SensorsService(List<Sensor<?>> sensors) {
        this.sensors = sensors.stream().collect(toMap(Sensor::getIdentifier, s -> s));
    }

    public SensorValue<?> getReadingFrom(String identifier) {
        Sensor<?> sensor = sensors.get(identifier);
        if (sensor == null) {
            throw new RuntimeException("Unkown sensor");
        }
        return sensor.getValue();
    }

}
