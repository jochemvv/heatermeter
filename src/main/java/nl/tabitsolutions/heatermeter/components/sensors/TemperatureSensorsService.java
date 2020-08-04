package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.components.drivers.SSD1306_I2C_Display;
import nl.tabitsolutions.heatermeter.model.CalibrationProfile;
import nl.tabitsolutions.heatermeter.model.Reading;
import nl.tabitsolutions.heatermeter.model.Sensor;
import nl.tabitsolutions.heatermeter.model.SensorInfo;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toMap;

@Service
public class TemperatureSensorsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Map<String, AbstractTemperatureSensor> sensors;
    private final List<CalibrationProfile<Long>>  calibrationProfiles;
    private final ReadingsRepository readingsRepository;
    private final Map<String, SensorValue<Long>> lastReadings = new ConcurrentHashMap<>();
    private final SSD1306_I2C_Display display;

    public TemperatureSensorsService(List<AbstractTemperatureSensor> sensors,
                                     List<CalibrationProfile<Long>> calibrationProfiles,
                                     ReadingsRepository readingsRepository,
                                     Optional<SSD1306_I2C_Display> display) {

        this.sensors = sensors.stream().collect(toMap(Sensor::getIdentifier, s -> s));
        this.calibrationProfiles = calibrationProfiles;
        this.readingsRepository = readingsRepository;
        this.display = display.orElse(null);
    }

    public boolean isEnabled(String identifier) {
        return Optional.ofNullable(this.sensors.get(identifier)).map(Sensor::isEnabled).orElse(false);
    }

    public SensorValue<Long> getReadingFrom(String identifier) {
        AbstractTemperatureSensor sensor = sensors.get(identifier);
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
                                    v.getValue().isEnabled() ? v.getValue().getSteinhartHartEquationCalibrationProfile().getIdentifier() : "Disabled"
                            )
                        ),
                 HashMap::putAll);
    }

    @Scheduled(fixedDelay = 5000)
    public void readSensors() {
        logger.debug("registered sensors: " + sensors);

        Map<String, SensorValue<Long>> currentReadings = this.sensors.entrySet().stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue().isEnabled() ? v.getValue().getValue() : null), HashMap::putAll);

        OffsetDateTime now = OffsetDateTime.now();
        currentReadings.forEach((key, value) -> readingsRepository.addReading(key, new Reading<>(key, now, value)));

        this.lastReadings.clear();

        Map<String, SensorValue<Long>> lr = currentReadings.entrySet().stream().filter(entry -> entry.getValue() != null).collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        this.lastReadings.putAll(lr);

        if (this.display != null) {
            try {
                this.display.begin();
                List<String> toDisplay = new ArrayList<>();
                for (String sensorName : this.sensors.keySet()) {
                    toDisplay.add(sensorName + ": " + (lr.containsKey(sensorName) ? lr.get(sensorName).getValue() : "--"));
                }
                this.display.displayString(toDisplay.toArray(new String[0]));
            } catch (Exception e) {
                logger.warn("Error with display", e);
            }
        }
    }

    public Map<String, List<Reading<?>>> getReadings() {
        return readingsRepository.getReadings();
    }

    public Map<OffsetDateTime, List<Reading<?>>> getReadingsByTimeStamps() {
        return readingsRepository.getReadingsByTimeStamps();
    }

    public void updateSensor(String identifier, boolean enabled, String mode) {
        AbstractTemperatureSensor abstractTemperatureSensor = sensors.get(identifier);
        if (abstractTemperatureSensor != null) {
            abstractTemperatureSensor.setEnabled(enabled);
            this.calibrationProfiles.stream()
                    .filter(profile -> Objects.equals(mode, profile.getIdentifier()))
                    .filter(profile -> profile instanceof SteinhartHartEquationCalibrationProfile)
                    .map(profile -> (SteinhartHartEquationCalibrationProfile) profile)
                    .findFirst()
                    .ifPresent(
                            abstractTemperatureSensor::setSteinhartHartEquationCalibrationProfile
                    );
        }
    }
}
