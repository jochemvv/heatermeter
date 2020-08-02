package nl.tabitsolutions.heatermeter.config;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import nl.tabitsolutions.heatermeter.components.sensors.PiTemperatureSensor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConditionalOnProperty(name = "heatermeater.pimode", havingValue = "true")
public class I2CConfiguration {

    @Bean
    public I2CDevice i2cDevice() throws IOException, I2CFactory.UnsupportedBusNumberException {
        // Create I2C bus
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        // Get I2C device, ADS1115 I2C address is 0x48(72)
        I2CDevice device = bus.getDevice(0x48);

        return device;
    }

    @Bean(name = "channel0")
    public PiTemperatureSensor channel0(I2CDevice i2CDevice) {
        byte[] config = {(byte)0xC4, (byte)0x83};
        return new PiTemperatureSensor("channel0", i2CDevice, config);
    }

    @Bean(name = "channel1")
    public PiTemperatureSensor channel1(I2CDevice i2CDevice) {
        byte[] config = {(byte)0xD4, (byte)0x83};
        return new PiTemperatureSensor("channel1", i2CDevice, config);
    }

    @Bean(name = "channel2")
    public PiTemperatureSensor channel2(I2CDevice i2CDevice) {
        byte[] config = {(byte)0xE4, (byte)0x83};
        return new PiTemperatureSensor("channel2", i2CDevice, config);
    }

    @Bean(name = "channel3")
    public PiTemperatureSensor channel3(I2CDevice i2CDevice) {
        byte[] config = {(byte)0xF4, (byte)0x83};
        return new PiTemperatureSensor("channel3", i2CDevice, config);
    }
}
