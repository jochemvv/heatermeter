package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.components.actuators.BluetoothDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;
import org.sputnikdev.bluetooth.manager.transport.BluetoothObjectFactory;
import tinyb.BluetoothManager;

@Configuration
@Profile("gpio")
public class BluetoothConfiguration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public BluetoothManager bluetoothManager() {
        loadTinyB();
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

    private void loadTinyB() {
        try {
            Class<?> tinybFactoryClass =
                    Class.forName("org.sputnikdev.bluetooth.manager.transport.tinyb.TinyBFactory");
            boolean loaded = (boolean) tinybFactoryClass.getDeclaredMethod("loadNativeLibraries")
                    .invoke(tinybFactoryClass);
            if (!loaded) {
                logger.warn("Native libraries for TinyB transport could not be loaded.");

            }
        } catch (Exception ex) {
            logger.warn("Could not initialize TinyB transport: {}", ex.getMessage());
        }
    }
}
