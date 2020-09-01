package nl.tabitsolutions.heatermeter.components.actuators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.DeviceDiscoveryListener;
import org.sputnikdev.bluetooth.manager.DiscoveredDevice;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class Fan implements DeviceDiscoveryListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BluetoothManager bluetoothManager;
    private final AtomicReference<DiscoveredDevice> uartDevice = new AtomicReference<>();

    public Fan(BluetoothManager bluetoothManager) {
        this.bluetoothManager = bluetoothManager;

    }

    @PostConstruct
    public void printBleDevices() {
        Set<DiscoveredDevice> discoveredDevices = bluetoothManager.getDiscoveredDevices();

        discoveredDevices.stream()
                .peek(device -> logger.info("BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias()));

        if (discoveredDevices.isEmpty()) {
            logger.info("NO DEVICES FOUND!");
        } else {
            logger.info("DEVICES FOUND! {}", discoveredDevices.size());
        }

        this.bluetoothManager.addDeviceDiscoveryListener(this);
    }

    public void turnOn() {
        Optional.ofNullable(uartDevice.get())
                .ifPresent(device ->  {
                    bluetoothManager.getCharacteristicGovernor(device.getURL(), true)
                            .write("fan on".getBytes());
                });
    }

    public void turnOff() {
        Optional.ofNullable(uartDevice.get())
                .ifPresent(device ->  {
                    bluetoothManager.getCharacteristicGovernor(device.getURL(), true)
                            .write("fan off".getBytes());
                });
    }

    @Override
    public void discovered(DiscoveredDevice device) {
        if (device.isBleEnabled()) {
            logger.info("!!! BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias());
            if (device.getDisplayName().toUpperCase().contains("UART")) {
                logger.info("!!! UART DEVICE FOUND !!!");
                uartDevice.set(device);
            }
        }
    }

    @Override
    public void deviceLost(DiscoveredDevice device) {
        if (device.isBleEnabled()) {
            logger.info("!!! BLE device lost !!! : {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias());
            uartDevice.set(null);
        }
    }
}
