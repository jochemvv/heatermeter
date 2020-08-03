package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Reading;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class ReadingsRepository {

    private final Map<String, List<Reading<?>>> readings = new ConcurrentHashMap<>();

    public void addReading(String identifier, Reading<?> reading) {
        readings.computeIfAbsent(identifier, s -> new ArrayList<>()).add(reading);
    }

    public Map<String, List<Reading<?>>> getReadings() {
        return Collections.unmodifiableMap(readings);
    }

    public Map<OffsetDateTime, List<Reading<?>>> getReadingsByTimeStamps() {
        return readings.values().stream().flatMap(Collection::stream).collect(Collectors.groupingBy(Reading::getTimestamp));
    }

}
