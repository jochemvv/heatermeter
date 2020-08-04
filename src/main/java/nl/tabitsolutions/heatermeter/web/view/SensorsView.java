package nl.tabitsolutions.heatermeter.web.view;

import nl.tabitsolutions.heatermeter.model.SensorInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SensorsView {

    private String simpleVal = "testing value";
    private List<SensorInfo> sensors = new ArrayList<>();

    public SensorsView() {

    }

    public SensorsView(List<SensorInfo> sensorInfo) {
        this.sensors = sensorInfo;
    }

    public String getSimpleVal() {
        return simpleVal;
    }

    public void setSimpleVal(String simpleVal) {
        this.simpleVal = simpleVal;
    }

    public void setSensors(List<SensorInfo> sensors) {
        this.sensors = sensors;
    }

    public List<SensorInfo> getSensors() {
        return sensors;
    }
}
