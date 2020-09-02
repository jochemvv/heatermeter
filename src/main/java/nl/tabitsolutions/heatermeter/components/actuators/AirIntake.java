package nl.tabitsolutions.heatermeter.components.actuators;

import org.springframework.stereotype.Component;

@Component
public class AirIntake {

    private final BluetoothDevice bluetoothDevice;

    public AirIntake(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public void setToPosition(int position) {
        this.bluetoothDevice.sendMessage("intake " + (Math.max(position, 0)));
    }
}
