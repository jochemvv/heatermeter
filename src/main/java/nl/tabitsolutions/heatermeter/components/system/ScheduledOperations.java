package nl.tabitsolutions.heatermeter.components.system;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledOperations {

    private final HeaterMeterSystemController heaterMeterSystemController;

    public ScheduledOperations(HeaterMeterSystemController heaterMeterSystemController) {
        this.heaterMeterSystemController = heaterMeterSystemController;
    }

    @Scheduled(fixedRate = 10000)
    public void handleActions() {
        heaterMeterSystemController.handleActions();
    }
}
