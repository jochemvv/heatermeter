package nl.tabitsolutions.heatermeter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

@Configuration
@ConditionalOnMissingBean(BluetoothConfiguration.class)
public class TestBluetoothConfiguration {

    @Bean
    public BluetoothManager bluetoothManager() {
        return new BluetoothManagerBuilder()
                .withDiscovering(true)
                .withCombinedAdapters(true)
                .build();
    }
}
