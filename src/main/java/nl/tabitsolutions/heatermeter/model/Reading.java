package nl.tabitsolutions.heatermeter.model;

import java.time.OffsetDateTime;

public class Reading<T extends Number> {

    private final OffsetDateTime timestamp;
    private final SensorValue<T> value;

    public Reading(OffsetDateTime timestamp, SensorValue<T> value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public SensorValue<T> getValue() {
        return value;
    }
}
