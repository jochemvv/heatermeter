package nl.tabitsolutions.heatermeter.model;

public abstract class Sensor<T extends Number> {

    private final String identifier;

    protected Sensor(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract SensorValue<T> getValue();

}
