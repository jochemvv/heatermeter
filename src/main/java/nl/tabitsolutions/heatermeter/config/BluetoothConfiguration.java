package nl.tabitsolutions.heatermeter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

@Configuration
@Profile("gpio")
public class BluetoothConfiguration {

    @Bean
    public BluetoothManager bluetoothManager() {
        return new BluetoothManagerBuilder()
                .withTinyBTransport(true)
                .withDiscovering(true)
                .withCombinedAdapters(true)
                .withCombinedDevices(false)
                .build();
    }
}
