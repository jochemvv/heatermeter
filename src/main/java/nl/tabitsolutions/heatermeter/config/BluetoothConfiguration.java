package nl.tabitsolutions.heatermeter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import tinyb.BluetoothManager;

@Configuration
@Profile("gpio")
public class BluetoothConfiguration {

    @Bean
    public BluetoothManager bluetoothManager() {
        return BluetoothManager.getBluetoothManager();
    }
}
