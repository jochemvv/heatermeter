package nl.tabitsolutions.heatermeter.model;

import io.micronaut.test.annotation.MicronautTest;
import nl.tabitsolutions.heatermeter.config.CalibrationConfiguration;
import nl.tabitsolutions.heatermeter.config.I2CConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SteinhartHartEquationCalibrationProfileTest {


    @Test
    public void testIkeaProbe() {
        SteinhartHartEquationCalibrationProfile calibrationProfile = new CalibrationConfiguration().ikeaCalibration();
        Long calibratedValue = calibrationProfile.getCalibratedValue(  12411L);

        System.out.println("" + calibratedValue);
    }

    @Test
    public void testBluetoothProbe() {
        SteinhartHartEquationCalibrationProfile calibrationProfile = new CalibrationConfiguration().bluetoothCalibration();
        Long calibratedValue = calibrationProfile.getCalibratedValue(  20850L);

        System.out.println("" + calibratedValue);
    }
}