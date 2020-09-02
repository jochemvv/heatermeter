package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.components.actuators.BluetoothDevice;
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
        new BluetoothManagerBuilder().withDiscovering(false).withTinyBTransport(true).build(); // forces lib to be installed etc.
        return BluetoothManager.getBluetoothManager();
    }

    @Bean
    public BluetoothDevice bluetoothDevice(BluetoothManager bluetoothManager) {
        BluetoothDevice bluetoothDevice = new BluetoothDevice(bluetoothManager);
        try {
            bluetoothDevice.initBluetoothDevice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bluetoothDevice;
    }
}
