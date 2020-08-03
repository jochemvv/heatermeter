package nl.tabitsolutions.heatermeter.web.view;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;
import nl.tabitsolutions.heatermeter.components.sensors.SensorsService;
import nl.tabitsolutions.heatermeter.model.Reading;
import nl.tabitsolutions.heatermeter.model.SensorValue;

import java.util.ArrayList;
import java.util.Arrays;
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
                "sensors", new TreeMap<>(sensorsService.getLastReadings()),
                        "readings", getReadings()
                    )
        );
    }

    private List<List<Object>> getReadings() {
        ArrayList<List<Object>> objects = new ArrayList<>();

        objects.add(Arrays.asList("time" ,"s1", "s2", "s3", "s4"));
        objects.addAll(new TreeMap<>(sensorsService.getReadingsByTimeStamps()).entrySet().stream()
                            .map(entry -> {
                                ArrayList<Object> v = new ArrayList<>();
                                v.add(entry.getKey().toEpochSecond());
                                v.addAll(entry.getValue().stream().map(Reading::getValue).map(SensorValue::getValue).collect(toList()));
                                return v;
                            })
                            .collect(toList()));
        return objects;
    }

}
