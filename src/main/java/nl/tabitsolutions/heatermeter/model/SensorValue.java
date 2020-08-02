package nl.tabitsolutions.heatermeter.model;

public class SensorValue<T extends Number> {

    private final T value;
    private final Unit unit;

    public SensorValue(T value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public T getValue() {
        return value;
    }

    public Unit getUnit() {
        return unit;
    }
}
