package nl.tabitsolutions.heatermeter.components.system;

import nl.tabitsolutions.heatermeter.components.actions.Action;
import nl.tabitsolutions.heatermeter.model.SensorInfo;
import nl.tabitsolutions.heatermeter.model.SensorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;

public class HeaterMeterSystemController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final List<Action> actions = new ArrayList<>();

    public HeaterMeterSystemController() {
        logger.info("creating controller");
    }

    public synchronized void registerAction(Action action) {
        if (this.actions.stream().anyMatch(ac -> Objects.equals(ac.getIdentifier(), action.getIdentifier()))) {
            throw new RuntimeException("Unable to register action, it is already registered");
        }
        this.actions.add(action);
    }

    public synchronized void registerOrReplaceAction(Action action) {
        this.actions.removeIf(ac -> Objects.equals(ac.getIdentifier(), action.getIdentifier()));
        this.actions.add(action);
    }

    public synchronized  void deregisterAction(Action action) {
        this.actions.remove(action);
    }

    public synchronized void handleActions() {
        logger.info("handling actions");
        actions.forEach(action -> {
            try {
                if (action.getTarget().getSensor().isEnabled()) {
                    action.doActionBasedOnTarget();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public synchronized Map<String, SensorInfo> getSensorInfo() {
        return this.actions.stream()
                .collect(toMap(Action::getIdentifier, this::actionToSensorInfo));
    }

    private synchronized SensorInfo actionToSensorInfo(Action a) {
        boolean enabled = a.getTarget().getSensor().isEnabled();
        SensorValue<Long> currentValue = a.getTarget().getCurrentValue();

        return new SensorInfo(a.getIdentifier(),
                enabled ? currentValue.getValue() : null,
                enabled ? a.getTarget().getTargetValue().getValue() : null,
                enabled ? currentValue.getUnit().toString() : null,
                enabled ? a.getTarget().getSensor().getSteinhartHartEquationCalibrationProfile().getIdentifier() : "Disabled"
        );

    }

    public synchronized void updateTarget(String identifier, Long target) {
        this.actions.stream()
                .filter(action -> Objects.equals(action.getTarget().getSensor().getIdentifier(), identifier))
                .findFirst()
                .ifPresent(action -> action.getTarget().setTargetValue(new SensorValue<>(target != null ? target : 0, action.getTarget().getTargetValue().getUnit())));
    }
}