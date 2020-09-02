package nl.tabitsolutions.heatermeter.components.targets;

import nl.tabitsolutions.heatermeter.components.actions.Action;
import nl.tabitsolutions.heatermeter.components.sensors.AbstractTemperatureSensor;
import nl.tabitsolutions.heatermeter.model.SensorValue;

public class TemperatureTarget {

    private final AbstractTemperatureSensor sensor;
    private volatile SensorValue<Long> targetTemperature;

    public TemperatureTarget(AbstractTemperatureSensor sensor,
                             SensorValue<Long> targetTemperature) {
        this.sensor = sensor;
        this.targetTemperature = targetTemperature;
    }


    public SensorValue<Long> getCurrentValue() {
        return sensor.getValue();
    }

    public SensorValue<Long> getTargetValue() {
        return targetTemperature;
    }

    public void setTargetValue(SensorValue<Long> value) {
        this.targetTemperature = value;
    }
}
