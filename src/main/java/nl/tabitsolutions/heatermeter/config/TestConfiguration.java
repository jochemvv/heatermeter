package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.components.sensors.SensorsProvider;
import nl.tabitsolutions.heatermeter.components.sensors.TestTemperatureSensor;
import nl.tabitsolutions.heatermeter.model.Sensor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Configuration
@ConditionalOnMissingBean(I2CConfiguration.class)
public class TestConfiguration {

    @Bean(name = "channel0")
    public TestTemperatureSensor channel0() {
        return new TestTemperatureSensor("channel0");
    }

    @Bean(name = "channel1")
    public TestTemperatureSensor channel1() {
        return new TestTemperatureSensor("channel1");
    }

    @Bean(name = "channel2")
    public TestTemperatureSensor channel2() {
        return new TestTemperatureSensor("channel2");
    }

    @Bean(name = "channel3")
    public TestTemperatureSensor channel3() {
        return new TestTemperatureSensor("channel3");
    }

}
