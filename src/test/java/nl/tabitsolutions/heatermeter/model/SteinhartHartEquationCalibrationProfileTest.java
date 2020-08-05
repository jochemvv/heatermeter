package nl.tabitsolutions.heatermeter.model;

import nl.tabitsolutions.heatermeter.config.CalibrationConfiguration;
import org.junit.jupiter.api.Test;

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
        SteinhartHartEquationCalibrationProfile calibrationProfile = new CalibrationConfiguration().mastradCalibration();
        Long calibratedValue = calibrationProfile.getCalibratedValue(  20850L);

        System.out.println("" + calibratedValue);
    }
}