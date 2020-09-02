package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.components.actions.Action;
import nl.tabitsolutions.heatermeter.components.actuators.AirIntake;
import nl.tabitsolutions.heatermeter.components.actuators.Fan;
import nl.tabitsolutions.heatermeter.components.sensors.AbstractTemperatureSensor;
import nl.tabitsolutions.heatermeter.components.sensors.PiTemperatureSensor;
import nl.tabitsolutions.heatermeter.components.system.HeaterMeterSystemController;
import nl.tabitsolutions.heatermeter.components.targets.TemperatureTarget;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import nl.tabitsolutions.heatermeter.model.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;
import javax.inject.Qualifier;
import java.util.List;

@Configuration
@Import({I2CConfiguration.class, TestConfiguration.class, BluetoothConfiguration.class, TestBluetoothConfiguration.class})
public class HeaterMeterSystemConfiguration {


    @Bean
    public HeaterMeterSystemController heaterMeterSystemController(List<Action> actions) {
        HeaterMeterSystemController heaterMeterSystemController = new HeaterMeterSystemController();

        actions.forEach(heaterMeterSystemController::registerAction);

        return heaterMeterSystemController;
    }

    @Bean("action1")
    public Action action1(@Named("channel0") AbstractTemperatureSensor channel0,
                          Fan fan,
                          AirIntake intake) {

        Logger logger = LoggerFactory.getLogger(Action.class);

        return new Action("action1",
                new TemperatureTarget(channel0, new SensorValue<>(100L, Unit.CELSIUS)),
                        (target) -> {
                            if (target.getCurrentValue().getValue() < target.getTargetValue().getValue()) {
                                logger.info("temp too low, turning on fan {}", target.getCurrentValue().getValue());
                                fan.turnOn();
                            } else {
                                logger.info("temp not below target turning off fan {}", target.getCurrentValue().getValue());
                                fan.turnOff();
                            }

                            if (target.getCurrentValue().getValue() > target.getTargetValue().getValue() + 10) {
                                logger.info("temp way too high closing intake {}", target.getCurrentValue().getValue());
                                intake.setToPosition(0);
                            } else {
                                double position = Math.max((target.getTargetValue().getValue() / 200d) * 60d, 0);
                                logger.info("temp not too high adjusting intake accordingly {}, {} ", (int) position, target.getCurrentValue().getValue());
                                intake.setToPosition((int) position);
                            }

                        });
    }

}
