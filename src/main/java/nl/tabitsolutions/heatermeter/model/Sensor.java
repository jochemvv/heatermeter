package nl.tabitsolutions.heatermeter.model;

public abstract class Sensor<T extends Number> {

    private final String identifier;
    private boolean enabled = true;

    protected Sensor(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract SensorValue<T> getValue();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        return this.identifier + " " + this.getValue();
    }
}
