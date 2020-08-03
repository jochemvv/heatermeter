package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Sensor;
import nl.tabitsolutions.heatermeter.model.SensorValue;

import com.pi4j.io.i2c.I2CDevice;
import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;
import nl.tabitsolutions.heatermeter.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PiTemperatureSensor extends Sensor<Long> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile;
    private final Ads1115Device.AdcPin pin;

    public PiTemperatureSensor(String identifier,
                               SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile,
                               Ads1115Device.AdcPin pin) {
        super(identifier);
        this.steinhartHartEquationCalibrationProfile = steinhartHartEquationCalibrationProfile;
        this.pin = pin;
    }

    @Override
    public SensorValue<Long> getValue() {
            try {
                long rawReading = pin.getImmediateValue();
                Long calibratedValue = steinhartHartEquationCalibrationProfile.getCalibratedValue(rawReading);

                logger.info("{}, raw {}, calibrated {}", getIdentifier(), rawReading, calibratedValue);

                return new SensorValue<>(calibratedValue, Unit.CELSIUS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

    }
}
