package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.SensorValue;
import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;
import nl.tabitsolutions.heatermeter.model.Unit;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TestTemperatureSensor extends AbstractTemperatureSensor {

    private Random random = new Random();
    private AtomicLong value = new AtomicLong(60);

    public TestTemperatureSensor(String identifier, boolean enabled, SteinhartHartEquationCalibrationProfile calibrationProfile) {
        super(identifier, calibrationProfile);
        this.setEnabled(enabled);
    }

    @Override
    public SensorValue<Long> getValue() {
        return new SensorValue<>(value.getAndAccumulate(random.nextInt(10), (v1, v2) -> random.nextInt(10) > 5 ? v1 + v2 : v1 - v2), Unit.CELSIUS);
    }
}
