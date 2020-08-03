package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Reading;
import nl.tabitsolutions.heatermeter.model.Sensor;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
public class SensorsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Sensor<?>> sensors;
    private final ReadingsRepository readingsRepository;

    public SensorsService(List<Sensor<?>> sensors,
                          ReadingsRepository readingsRepository) {

        this.sensors = sensors.stream().collect(toMap(Sensor::getIdentifier, s -> s));
        this.readingsRepository = readingsRepository;
    }

    public SensorValue<?> getReadingFrom(String identifier) {
        Sensor<?> sensor = sensors.get(identifier);
        if (sensor == null) {
            throw new RuntimeException("Unkown sensor");
        }
        return sensor.getValue();
    }

    @Scheduled(fixedDelay = 1000)
    public Map<String, SensorValue<?>> getCurrentReadings() {

        logger.info("resgistered sensors: " + sensors);

        Map<String, SensorValue<?>> currentReadings = this.sensors.entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> entry.getValue().getValue()));

        OffsetDateTime now = OffsetDateTime.now();
        currentReadings.forEach((key, value) -> readingsRepository.addReading(key, new Reading<>(key, now, value)));

        return currentReadings;
    }

    public Map<String, List<Reading<?>>> getReadings() {
        return readingsRepository.getReadings();
    }

    public Map<OffsetDateTime, List<Reading<?>>> getReadingsByTimeStamps() {
        return readingsRepository.getReadingsByTimeStamps();
    }
}
