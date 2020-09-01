package nl.tabitsolutions.heatermeter.components.actuators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.DiscoveredDevice;

import javax.annotation.PostConstruct;

@Component
public class Fan {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BluetoothManager bluetoothManager;

    public Fan(BluetoothManager bluetoothManager) {
        this.bluetoothManager = bluetoothManager;
    }

    @PostConstruct
    public void printBleDevices() {
        bluetoothManager.getDiscoveredDevices().stream()
                .peek(device -> logger.info("BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias()));
    }

    public void turnOn() {

    }

    public void turnOff() {

    }

}
