package nl.tabitsolutions.heatermeter.model;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class SteinhartHartEquationCalibrationProfileTest {


    @Test
    public void test() {
        SteinhartHartEquationCalibrationProfile calibrationProfile = new SteinhartHartEquationCalibrationProfile(155000d, 52000d, 4300d, 174d, 298d, 364d, 100000d);
        Long calibratedValue = calibrationProfile.getCalibratedValue(18856L);

        System.out.println("" + calibratedValue);
    }
}