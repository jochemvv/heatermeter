package nl.tabitsolutions.heatermeter.model;

import io.micronaut.test.annotation.MicronautTest;
import nl.tabitsolutions.heatermeter.config.I2CConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SteinhartHartEquationCalibrationProfileTest {


    @Test
    public void testIkeaProbe() {
        SteinhartHartEquationCalibrationProfile calibrationProfile = new I2CConfiguration().ikeaCalibration();
        Long calibratedValue = calibrationProfile.getCalibratedValue(  12411L);

        System.out.println("" + calibratedValue);
    }

    @Test
    public void testBluetoothProbe() {
        SteinhartHartEquationCalibrationProfile calibrationProfile = new I2CConfiguration().bluetoothCalibration();
        Long calibratedValue = calibrationProfile.getCalibratedValue(  18767L);

        System.out.println("" + calibratedValue);
    }
}