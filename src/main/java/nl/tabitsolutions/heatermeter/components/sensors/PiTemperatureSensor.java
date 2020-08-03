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
    private final I2CDevice i2cDevice;
    private final byte[] config;

    public PiTemperatureSensor(String identifier,
                               SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile,
                               I2CDevice i2cDevice,
                               byte[] config) {
        super(identifier);
        this.steinhartHartEquationCalibrationProfile = steinhartHartEquationCalibrationProfile;
        this.i2cDevice = i2cDevice;
        this.config = config;
    }

    @Override
    public SensorValue<Long> getValue() {
        synchronized (i2cDevice) {
            try {
                i2cDevice.write(0x01, config, 0, 2);
                Thread.sleep(500);
                byte[] data = new byte[2];
                i2cDevice.read(0x00, data, 0, 2);
                long rawReading = getRawReading(data);
                Long calibratedValue = steinhartHartEquationCalibrationProfile.getCalibratedValue(rawReading);

                logger.info("raw {}, calibrated {}", rawReading, calibratedValue);

                return new SensorValue<>(calibratedValue, Unit.CELSIUS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int getRawReading(byte[] data) {
        int rawReading = ((data[0] & 0xFF) * 256) + (data[1] & 0xFF);
        return rawReading;
    }
}
