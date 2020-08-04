package nl.tabitsolutions.heatermeter.model;

public class SensorInfo {

    private final String identifier;
    private final Object reading;
    private final String unit;
    private final String mode;

    public SensorInfo(String identifier, Object reading, String unit, String mode) {
        this.identifier = identifier;
        this.reading = reading;
        this.unit = unit;
        this.mode = mode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Object getReading() {
        return reading;
    }

    public String getUnit() {
        return unit;
    }

    public String getMode() {
        return mode;
    }
}
