package nl.tabitsolutions.heatermeter.components.actuators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.BluetoothSmartDeviceListener;
import org.sputnikdev.bluetooth.manager.CharacteristicGovernor;
import org.sputnikdev.bluetooth.manager.DeviceDiscoveryListener;
import org.sputnikdev.bluetooth.manager.DeviceGovernor;
import org.sputnikdev.bluetooth.manager.DiscoveredDevice;
import org.sputnikdev.bluetooth.manager.GattCharacteristic;
import org.sputnikdev.bluetooth.manager.GattService;
import org.sputnikdev.bluetooth.manager.GenericBluetoothDeviceListener;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.stream.Collectors.toList;

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

        discoveredDevices.forEach(device -> {
            logger.info("BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias());
            addDevice(device);
        });

        if (discoveredDevices.isEmpty()) {
            logger.info("NO DEVICES FOUND!");
        } else {
            logger.info("DEVICES FOUND! {}", discoveredDevices.size());
        }

        this.bluetoothManager.addDeviceDiscoveryListener(this);
    }

    public void turnOn() {
        logger.error("turning on");
        Optional.ofNullable(uartDevice.get())
                .ifPresent(device ->  {
                    logger.error("UART present");

                    List<CharacteristicGovernor> characteristicGovernors = bluetoothManager.getDeviceGovernor(device.getURL(), true)
                            .getCharacteristicGovernors();

                    if(characteristicGovernors.isEmpty()) {
                        logger.error("no characteristics");
                    };

                    characteristicGovernors
                            .stream()
                            .peek(characteristic -> {
                                logger.info("characteristic {} {} {}", characteristic.getFlags(), characteristic.isWritable(), characteristic.getURL());
                            })
                            .filter(CharacteristicGovernor::isWritable)
                            .forEach(characteristic -> characteristic.write("fan on".getBytes()));
                });
        logger.error("turning on done");
    }

    public void turnOff() {
        Optional.ofNullable(uartDevice.get())
                .ifPresent(device ->  {
                    bluetoothManager.getDeviceGovernor(device.getURL(), true)
                            .getCharacteristicGovernors()
                            .stream()
                            .peek(characteristic -> {
                                logger.info("characteristic {} {} {}", characteristic.getFlags(), characteristic.isWritable(), characteristic.getURL());
                            })
                            .filter(CharacteristicGovernor::isWritable)
                            .forEach(characteristic -> characteristic.write("fan off".getBytes()));
                });
    }

    @Override
    public void discovered(DiscoveredDevice device) {
        if (device.isBleEnabled()) {
            logger.info("!!! BLE device found: {} {} {} {}", device.getDisplayName(), device.isBleEnabled(), device.getName(), device.getAlias());
            addDevice(device);
        }
    }

    private void addDevice(DiscoveredDevice device) {
        if (device.getDisplayName().toUpperCase().contains("UART")) {
            logger.info("!!!######################### UART DEVICE FOUND ######################################!!!");

            DeviceGovernor deviceGovernor = bluetoothManager.getDeviceGovernor(device.getURL());
            deviceGovernor.addBluetoothSmartDeviceListener(new BluetoothSmartDeviceListener() {
                @Override
                public void servicesResolved(List<GattService> gattServices) {
                    logger.info("################################################### Trying to authenticate...{} ", gattServices.size());

                    gattServices.stream()
                            .peek(service -> logger.info("################################################### service {}", service.getCharacteristics().stream().map(GattCharacteristic::getFlags).collect(toList())));
                }

                @Override
                public void connected() {

                }
            });
            deviceGovernor.addGenericBluetoothDeviceListener(new GenericBluetoothDeviceListener() {
                @Override
                public void online() {
                    logger.info("################################################### Online");
                }

                @Override
                public void offline() {
                    logger.info("################################################### Offline");
                }

                @Override
                public void blocked(boolean blocked) {
                    logger.info("################################################### blocked {}", blocked);
                }

                @Override
                public void rssiChanged(short rssi) {
                    logger.info("################################################### rssi {}", rssi);
                }
            });


            uartDevice.set(device);
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
