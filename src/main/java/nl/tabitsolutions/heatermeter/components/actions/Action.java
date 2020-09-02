package nl.tabitsolutions.heatermeter.components.actions;

import nl.tabitsolutions.heatermeter.components.targets.TemperatureTarget;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class Action {



    private final String identifier;
    private final TemperatureTarget target;
    private final Consumer<TemperatureTarget> action;

    public Action(String identifier,
                  TemperatureTarget target,
                  Consumer<TemperatureTarget> action) {
        this.identifier = identifier;
        this.target = target;
        this.action = action;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void doActionBasedOnTarget() {
        action.accept(target);
    }

}
