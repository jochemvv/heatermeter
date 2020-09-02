package nl.tabitsolutions.heatermeter;

import io.micronaut.runtime.Micronaut;
import nl.tabitsolutions.heatermeter.config.CalibrationConfiguration;
import nl.tabitsolutions.heatermeter.config.I2CConfiguration;
import nl.tabitsolutions.heatermeter.config.TestConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.BluetoothSmartDeviceListener;
import org.sputnikdev.bluetooth.manager.DeviceGovernor;
import org.sputnikdev.bluetooth.manager.DiscoveredDevice;
import org.sputnikdev.bluetooth.manager.GattCharacteristic;
import org.sputnikdev.bluetooth.manager.GattService;
import org.sputnikdev.bluetooth.manager.GenericBluetoothDeviceListener;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
@Import({CalibrationConfiguration.class, I2CConfiguration.class, TestConfiguration.class})
@EnableScheduling
public class HeatermeterApplication {

	private static final Logger logger = LoggerFactory.getLogger(HeatermeterApplication.class);

	public static void main(String[] args) throws InterruptedException {
		testBluetooth();
//		Micronaut.build(args).eagerInitSingletons(true).mainClass(HeatermeterApplication.class).start();
//		SpringApplication.run(HeatermeterApplication.class, args);
	}

	public static void testBluetooth() throws InterruptedException {
		BluetoothManager manager = new BluetoothManagerBuilder()
				.withTinyBTransport(true)
				.withIgnoreTransportInitErrors(true)
				.withDiscovering(true)
				.withCombinedAdapters(true)
				.withCombinedDevices(false)
				.build();

		while (true) {
			Set<DiscoveredDevice> discoveredDevices = manager.getDiscoveredDevices();
			logger.info("!!!######################### Discovered devices: {}", discoveredDevices.size());
			discoveredDevices.forEach(device -> {
				try {
					checkDevice(manager, device);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
			Thread.sleep(2000L);
		}
	}

	public static void checkDevice(BluetoothManager bluetoothManager, DiscoveredDevice device) throws InterruptedException {
		if (device.getDisplayName().toUpperCase().contains("UART")) {


			logger.info("!!!######################### UART DEVICE FOUND ######################################!!!");

			DeviceGovernor deviceGovernor = bluetoothManager.getDeviceGovernor(device.getURL());
			while (!deviceGovernor.isServicesResolved()) {
				logger.info("!!!######################### waiting for services to be resolved {}", deviceGovernor.isConnected());
				Thread.sleep(1000L);
			}
			logger.info("!!!######################### resolved services {}", deviceGovernor.getResolvedServices().size());
			for (GattService resolvedService : deviceGovernor.getResolvedServices()) {
				logger.info("!!!######################### resolved service {}, {}", resolvedService.getCharacteristics(), resolvedService.getURL());
			}
		}
	}
}
