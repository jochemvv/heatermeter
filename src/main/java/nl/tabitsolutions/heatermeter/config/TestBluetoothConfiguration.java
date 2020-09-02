package nl.tabitsolutions.heatermeter.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tinyb.BluetoothManager;

@Configuration
@ConditionalOnMissingBean(BluetoothConfiguration.class)
public class TestBluetoothConfiguration {

    @Bean
    public BluetoothManager bluetoothManager() {
        return tinyb.BluetoothManager.getBluetoothManager();
    }
}
