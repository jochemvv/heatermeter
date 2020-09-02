package nl.tabitsolutions.heatermeter.components.actuators;

import com.pi4j.component.gyroscope.MultiAxisGyro;
import nl.tabitsolutions.heatermeter.HeatermeterApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tinyb.BluetoothGattCharacteristic;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class BluetoothDevice {

    private static final Logger logger = LoggerFactory.getLogger(HeatermeterApplication.class);

    public static final String UART = "UART";
    public static final int MAX_TRIES = 30;

    private final BluetoothManager manager;
    private volatile String address;
    private final AtomicReference<tinyb.BluetoothDevice> uartDevice = new AtomicReference<>();
    private final AtomicReference<BluetoothGattCharacteristic> gatt = new AtomicReference<>();

    public BluetoothDevice(BluetoothManager bluetoothManager) {
        this.manager = bluetoothManager;
    }

    @PostConstruct
    public void initBluetoothDevice() throws InterruptedException {
        try {
            manager.startDiscovery();
            int tries = 0;
            while (tries < MAX_TRIES && uartDevice.get() == null) {
                List<tinyb.BluetoothDevice> devices = manager.getDevices();
                logger.debug("!!!######################### Discovered devices: {}", devices.size());
                devices.forEach(device -> {
                    try {
                        checkDevice(manager, device);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                Thread.sleep(2000L);
                tries++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            manager.stopDiscovery();
        }
    }

    private void checkDevice(BluetoothManager manager, tinyb.BluetoothDevice device) throws InterruptedException {
        if (device.getName().toUpperCase().contains(UART)) {
            logger.debug("!!!######################### UART DEVICE FOUND ######################################!!!");
            boolean connect = device.connect();
            logger.debug("!!!######################### UART CONNECTION {}", connect);
            findWriteGatt(device);
        }
    }

    private void findWriteGatt(tinyb.BluetoothDevice device) throws InterruptedException {
        int tries = 0;
        while (!device.getServicesResolved() && tries < MAX_TRIES) {
            logger.debug("!!!######################### waiting for services to be resolved {}", device.getConnected());
            Thread.sleep(1000L);
            tries++;
        }
        logger.debug("!!!######################### resolved services {}", device.getServices().size());
        for (BluetoothGattService resolvedService : device.getServices()) {
            logger.debug("!!!######################### resolved service {}, {}",
                    resolvedService.getCharacteristics().stream().flatMap(c -> Arrays.stream(c.getFlags())).collect(Collectors.joining()),
                    resolvedService.getUUID());

            resolvedService.getCharacteristics().stream()
                    .filter(c -> Arrays.stream(c.getFlags()).anyMatch(flag -> flag != null && flag.contains("write")))
                    .findFirst()
                    .ifPresent(c -> {
                        logger.info("!!!######################### Found write GATT!");
                        this.uartDevice.set(device);
                        this.gatt.set(c);
                        address = device.getAddress();
                    });
        }
    }

    public synchronized void sendMessage(String message) {
        logger.info("sending message: {}, {} {}", message, this.gatt.get(), this.uartDevice.get());
        Optional.ofNullable(this.gatt.get())
                .ifPresent(g -> {
                    if (!g.getService().getDevice().getConnected()) {
                        try {
                            logger.info("reconnecting: {} {}", this.gatt.get(), this.uartDevice.get());
                            g.getService().getDevice().connect();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    if (g.getService().getDevice().getConnected()) {
                        g.writeValue(message.getBytes());
                    } else {
                        logger.info("skipping sending message: {}, {} {}", message, this.gatt.get(), this.uartDevice.get());
                        logger.info("reconnecting: {} {}", this.gatt.get(), this.uartDevice.get());
                        reconnect();
                    }
                });
    }

    private synchronized void reconnect() {
        this.uartDevice.set(null);
        this.gatt.set(null);
        try {
            this.initBluetoothDevice();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
