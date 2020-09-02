package nl.tabitsolutions.heatermeter.components.actuators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Component
public class Fan  {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final nl.tabitsolutions.heatermeter.components.actuators.BluetoothDevice bluetoothDevice;

    public Fan(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public void turnOn() {
        this.bluetoothDevice.sendMessage("fan on");
    }

    public void turnOff() {
        this.bluetoothDevice.sendMessage("fan off");
    }

}
