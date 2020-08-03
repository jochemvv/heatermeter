package nl.tabitsolutions.heatermeter.model;

import java.time.OffsetDateTime;

public class Reading<T extends Number> {

    private final String identifier;
    private final OffsetDateTime timestamp;
    private final SensorValue<T> value;

    public Reading(String identifier,
                   OffsetDateTime timestamp, SensorValue<T> value) {
        this.identifier = identifier;
        this.timestamp = timestamp;
        this.value = value;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public SensorValue<T> getValue() {
        return value;
    }

    public String getIdentifier() {
        return identifier;
    }
}
