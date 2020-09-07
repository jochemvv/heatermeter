package nl.tabitsolutions.heatermeter.model;

public class SensorInfo {

    private String identifier;
    private Object reading;
    private Object target;
    private String unit;
    private String mode;

    public SensorInfo() {

    }

    public SensorInfo(String identifier,
                      Object reading,
                      Object target,
                      String unit,
                      String mode) {
        this.identifier = identifier;
        this.reading = reading;
        this.target = target;
        this.unit = unit;
        this.mode = mode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Object getReading() {
        return reading;
    }

    public void setReading(Object reading) {
        this.reading = reading;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
