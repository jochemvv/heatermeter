package nl.tabitsolutions.heatermeter.components.actuators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.DeviceDiscoveryListener;
import org.sputnikdev.bluetooth.manager.DiscoveredDevice;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class Fan implements DeviceDiscoveryListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BluetoothManager bluetoothManager;

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
        }

        this.bluetoothManager.addDeviceDiscoveryListener(this);
    }

    public void turnOn() {

    }

    public void turnOff() {

    }

    @Override
    public void discovered(DiscoveredDevice device) {
        logger.info("!!! BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias());
    }

    @Override
    public void deviceLost(DiscoveredDevice device) {
        logger.info("!!! BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias());
    }
}
