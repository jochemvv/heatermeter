package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.components.sensors.TestTemperatureSensor;
import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(I2CConfiguration.class)
public class TestConfiguration {

    @Bean(name = "channel0")
    public TestTemperatureSensor channel0(SteinhartHartEquationCalibrationProfile calibrationProfile) {
        return new TestTemperatureSensor("channel0", true, calibrationProfile);
    }

    @Bean(name = "channel1")
    public TestTemperatureSensor channel1(SteinhartHartEquationCalibrationProfile calibrationProfile) {
        return new TestTemperatureSensor("channel1", false, calibrationProfile);
    }

    @Bean(name = "channel2")
    public TestTemperatureSensor channel2(SteinhartHartEquationCalibrationProfile calibrationProfile) {
        return new TestTemperatureSensor("channel2", false, calibrationProfile);
    }

    @Bean(name = "channel3")
    public TestTemperatureSensor channel3(SteinhartHartEquationCalibrationProfile calibrationProfile) {
        return new TestTemperatureSensor("channel3", true, calibrationProfile);
    }

}
