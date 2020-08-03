package nl.tabitsolutions.heatermeter.config;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import nl.tabitsolutions.heatermeter.components.sensors.Ads1115Device;
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
        I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
        return bus.getDevice(0x48);
    }

    @Bean(name = "ikeaCalibration")
    public SteinhartHartEquationCalibrationProfile ikeaCalibration() {
        return new SteinhartHartEquationCalibrationProfile(142000d, 52000d, 4300d, 275d, 298d, 364d, 100000d);
    }

//    @Bean(name = "bluetoothCalibration")
    public SteinhartHartEquationCalibrationProfile bluetoothCalibration() {
        return new SteinhartHartEquationCalibrationProfile(785000d, 248000d, 22000d, 273d, 296d, 355d, 100000d);
    }

    @Bean
    public Ads1115Device ads1115Device(I2CDevice i2cDevice) {
        return new Ads1115Device(i2cDevice);
    }

    @Bean(name = "channel0")
    public PiTemperatureSensor channel0(Ads1115Device ads1115Device, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        return new PiTemperatureSensor("channel0", steinhartHartEquationCalibrationProfile, ads1115Device.openAdcPin(Ads1115Device.Pin.PIN0, Ads1115Device.ProgrammableGainAmplifierValue.PGA_4_096V));
    }

    @Bean(name = "channel1")
    public PiTemperatureSensor channel1(Ads1115Device ads1115Device, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        return new PiTemperatureSensor("channel1", steinhartHartEquationCalibrationProfile, ads1115Device.openAdcPin(Ads1115Device.Pin.PIN1, Ads1115Device.ProgrammableGainAmplifierValue.PGA_4_096V));
    }

    @Bean(name = "channel2")
    public PiTemperatureSensor channel2(Ads1115Device ads1115Device, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        return new PiTemperatureSensor("channel2", steinhartHartEquationCalibrationProfile, ads1115Device.openAdcPin(Ads1115Device.Pin.PIN2, Ads1115Device.ProgrammableGainAmplifierValue.PGA_4_096V));
    }

    @Bean(name = "channel3")
    public PiTemperatureSensor channel3(Ads1115Device ads1115Device, SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        return new PiTemperatureSensor("channel3", steinhartHartEquationCalibrationProfile, ads1115Device.openAdcPin(Ads1115Device.Pin.PIN3, Ads1115Device.ProgrammableGainAmplifierValue.PGA_4_096V));
    }
}
