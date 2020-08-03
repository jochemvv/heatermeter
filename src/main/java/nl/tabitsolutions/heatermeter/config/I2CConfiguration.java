package nl.tabitsolutions.heatermeter.config;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import nl.tabitsolutions.heatermeter.components.sensors.PiTemperatureSensor;
import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;

@Configuration
@Profile("gpio")
public class I2CConfiguration {

    @Bean
    public I2CDevice i2cDevice() throws IOException, I2CFactory.UnsupportedBusNumberException {
        // Create I2C bus
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        // Get I2C device, ADS1115 I2C address is 0x48(72)
        I2CDevice device = bus.getDevice(0x48);

        return device;
    }

    @Bean(name = "ikeaCalibration")
    public SteinhartHartEquationCalibrationProfile ikeaCalibration() {
        return new SteinhartHartEquationCalibrationProfile(155000d, 52000d, 4300d, 174d, 298d, 364d, 100000d);
    }

    @Bean(name = "channel0")
    public PiTemperatureSensor channel0(I2CDevice i2CDevice, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        byte[] config = {(byte)0xC4, (byte)0x83};
        return new PiTemperatureSensor("channel0", steinhartHartEquationCalibrationProfile, i2CDevice, config);
    }

    @Bean(name = "channel1")
    public PiTemperatureSensor channel1(I2CDevice i2CDevice, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        byte[] config = {(byte)0xD4, (byte)0x83};
        return new PiTemperatureSensor("channel1", steinhartHartEquationCalibrationProfile, i2CDevice, config);
    }

    @Bean(name = "channel2")
    public PiTemperatureSensor channel2(I2CDevice i2CDevice, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        byte[] config = {(byte)0xE4, (byte)0x83};
        return new PiTemperatureSensor("channel2", steinhartHartEquationCalibrationProfile, i2CDevice, config);
    }

    @Bean(name = "channel3")
    public PiTemperatureSensor channel3(I2CDevice i2CDevice, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        byte[] config = {(byte)0xF4, (byte)0x83};
        return new PiTemperatureSensor("channel3", steinhartHartEquationCalibrationProfile, i2CDevice, config);
    }
}
