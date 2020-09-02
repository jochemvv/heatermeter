package nl.tabitsolutions.heatermeter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;
import tinyb.BluetoothManager;

@Configuration
@Profile("gpio")
public class BluetoothConfiguration {

    @Bean
    public BluetoothManager bluetoothManager() {
        new BluetoothManagerBuilder().build(); // forces lib to be installed etc.
        return BluetoothManager.getBluetoothManager();
    }
}
