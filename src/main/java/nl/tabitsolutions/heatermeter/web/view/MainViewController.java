package nl.tabitsolutions.heatermeter.web.view;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import nl.tabitsolutions.heatermeter.components.sensors.SensorsService;

import java.util.TreeMap;

@Controller("/views")
public class MainViewController {

    private final SensorsService sensorsService;

    public MainViewController(SensorsService sensorsService) {
        this.sensorsService = sensorsService;
    }

    @View("home")
    @Get("/")
    public HttpResponse index() {
        return HttpResponse.ok(CollectionUtils.mapOf(
                "sensors", new TreeMap<>(sensorsService.getCurrentReadings()),
                        "readings", new TreeMap<>(sensorsService.getReadings())
                    )
        );
    }

}
