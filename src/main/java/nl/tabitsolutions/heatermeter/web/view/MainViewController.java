package nl.tabitsolutions.heatermeter.web.view;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import nl.tabitsolutions.heatermeter.components.sensors.SensorsService;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

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
                "sensorsView", new SensorsView(new ArrayList<>(new TreeMap<>(sensorsService.getSensorInfo()).values())),
                        "readings", getReadings()
                    )
        );
    }

    private List<List<Object>> getReadings() {
        ArrayList<List<Object>> objects = new ArrayList<>();

        objects.addAll(new TreeMap<>(sensorsService.getReadingsByTimeStamps()).entrySet().stream()
                            .map(entry -> {
                                ArrayList<Object> v = new ArrayList<>();
                                v.add(entry.getKey().toEpochSecond());
                                v.addAll(entry.getValue().stream().map(reading -> reading.getValue() != null ?  reading.getValue().getValue() : null).collect(toList()));
                                return v;
                            })
                            .collect(toList()));
        return objects;
    }

}
