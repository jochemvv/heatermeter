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
import tinyb.BluetoothDevice;
import tinyb.BluetoothGattService;
import tinyb.BluetoothManager;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@SpringBootApplication
@Import({CalibrationConfiguration.class, I2CConfiguration.class, TestConfiguration.class})
@EnableScheduling
public class HeatermeterApplication {

	private static final Logger logger = LoggerFactory.getLogger(HeatermeterApplication.class);

	public static void main(String[] args) throws InterruptedException {
		if (args.length > 0 && "testbt".equals(args[0])) {
			testBluetooth();
		} else {
			Micronaut.build(args).eagerInitSingletons(true).mainClass(HeatermeterApplication.class).start();
		}
	}

	public static void testBluetooth() throws InterruptedException {
		BluetoothManager manager = BluetoothManager.getBluetoothManager();
		manager.startDiscovery();
		while (true) {
			List<BluetoothDevice> list = manager.getDevices();
			logger.info("!!!######################### Discovered devices: {}", list.size());
			for (BluetoothDevice device : list) {
				checkDevice(manager, device);
			}
			Thread.sleep(2000L);
		}

	}

	public static void checkDevice(BluetoothManager bluetoothManager, BluetoothDevice device) throws InterruptedException {
		if (device.getName().toUpperCase().contains("UART")) {
			logger.info("!!!######################### UART DEVICE FOUND ######################################!!!");
			boolean connect = device.connect();
			logger.info("!!!######################### UART DEVICE FOUND {}", connect);
			while (!device.getServicesResolved()) {
				logger.info("!!!######################### waiting for services to be resolved {}", device.getConnected());
				Thread.sleep(1000L);
			}
			logger.info("!!!######################### resolved services {}", device.getServices().size());
			for (BluetoothGattService resolvedService : device.getServices()) {
				logger.info("!!!######################### resolved service {}, {}",
						resolvedService.getCharacteristics().stream().flatMap(c -> Arrays.stream(c.getFlags())).collect(Collectors.joining()),
						resolvedService.getUUID());

				resolvedService.getCharacteristics().stream()
						.filter(c -> Arrays.stream(c.getFlags()).anyMatch(flag -> flag != null && flag.contains("write")))
						.findFirst()
						.ifPresent(c -> {
							logger.info("!!!######################### Sending fan on!");
							c.writeValue("fan on".getBytes());
						});



			}
		}
	}
}
