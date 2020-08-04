package nl.tabitsolutions.heatermeter.web.view;

import nl.tabitsolutions.heatermeter.model.SensorInfo;

import java.util.List;
import java.util.Map;

public class SensorsView {

    private final List<SensorInfo> sensors;

    public SensorsView(List<SensorInfo> sensorInfo) {
        this.sensors = sensorInfo;
    }

    public List<SensorInfo> getSensors() {
        return sensors;
    }
}
