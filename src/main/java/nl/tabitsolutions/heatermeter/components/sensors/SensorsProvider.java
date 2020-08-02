package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Sensor;

import java.util.List;

public interface SensorsProvider {

    List<Sensor<?>> getSensors();

}
