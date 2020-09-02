package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.components.actuators.BluetoothDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tinyb.BluetoothManager;

@Configuration
@ConditionalOnMissingBean(BluetoothConfiguration.class)
public class TestBluetoothConfiguration {

    @Bean
    public BluetoothDevice bluetoothDevice() {
        Logger logger = LoggerFactory.getLogger(BluetoothDevice.class);
        return new BluetoothDevice(null) {
            @Override
            public void initBluetoothDevice() {

            }

            @Override
            public void sendMessage(String message) {
                logger.info("Sending message to bluetooth device: {}", message);
            }
        };
    }
}
