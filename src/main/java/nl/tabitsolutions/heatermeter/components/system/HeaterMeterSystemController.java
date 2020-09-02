package nl.tabitsolutions.heatermeter.components.system;

import nl.tabitsolutions.heatermeter.components.actions.Action;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HeaterMeterSystemController {

    private final List<Action> actions = new ArrayList<>();

    public HeaterMeterSystemController() {

    }

    public synchronized void registerAction(Action action) {
        if (this.actions.stream().anyMatch(ac -> Objects.equals(ac.getIdentifier(), action))) {
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

    @Scheduled(fixedRate = 1000 * 10)
    public synchronized void handleActions() {
        actions.forEach(Action::doActionBasedOnTarget);
    }
}