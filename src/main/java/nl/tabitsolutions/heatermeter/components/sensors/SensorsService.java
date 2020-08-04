package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Reading;
import nl.tabitsolutions.heatermeter.model.Sensor;
import nl.tabitsolutions.heatermeter.model.SensorInfo;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@Service
public class SensorsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Sensor<?>> sensors;
    private final ReadingsRepository readingsRepository;
    private volatile Map<String, SensorValue<?>> lastReadings = new ConcurrentHashMap<>();

    public SensorsService(List<Sensor<?>> sensors,
                          ReadingsRepository readingsRepository) {

        this.sensors = sensors.stream().collect(toMap(Sensor::getIdentifier, s -> s));
        this.readingsRepository = readingsRepository;
    }

    public SensorValue<?> getReadingFrom(String identifier) {
        Sensor<?> sensor = sensors.get(identifier);
        if (sensor == null) {
            throw new RuntimeException("Unknown sensor");
        }
        return sensor.getValue();
    }


    public Map<String, SensorInfo> getSensorInfo() {
        return this.sensors.entrySet().stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(),
                            new SensorInfo(v.getKey(),
                                    this.lastReadings.containsKey(v.getKey()) ? this.lastReadings.get(v.getKey()).getValue() : null,
                                    this.lastReadings.containsKey(v.getKey()) ? this.lastReadings.get(v.getKey()).getUnit().toString() : null,
                                    v.getValue().isEnabled() ? "Ikea" : "Disabled"
                            )
                        ),
                 HashMap::putAll);
    }

    @Scheduled(fixedDelay = 1000)
    public void readSensors() {
        logger.debug("registered sensors: " + sensors);

        Map<String, SensorValue<?>> currentReadings = this.sensors.entrySet().stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue().isEnabled() ? v.getValue().getValue() : null), HashMap::putAll);

        OffsetDateTime now = OffsetDateTime.now();
        currentReadings.forEach((key, value) -> readingsRepository.addReading(key, new Reading<>(key, now, value)));

        this.lastReadings.clear();

        this.lastReadings.putAll(currentReadings.entrySet().stream().filter(entry -> entry.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    public Map<String, List<Reading<?>>> getReadings() {
        return readingsRepository.getReadings();
    }

    public Map<OffsetDateTime, List<Reading<?>>> getReadingsByTimeStamps() {
        return readingsRepository.getReadingsByTimeStamps();
    }
}
