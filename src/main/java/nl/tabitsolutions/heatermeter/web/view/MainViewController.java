package nl.tabitsolutions.heatermeter.web.view;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.views.View;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import nl.tabitsolutions.heatermeter.components.sensors.TemperatureSensorsService;
import nl.tabitsolutions.heatermeter.model.SensorInfo;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.stream.Collectors.toList;

@Controller("/probes")
@OpenAPIDefinition(
        info = @Info(
                title = "Proves API",
                version = "0.01-DRAFT",
                description = "Probes API"
        )
)
@Hidden
public class MainViewController {

    private final TemperatureSensorsService sensorsService;

    public MainViewController(TemperatureSensorsService sensorsService) {
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

    @Consumes(value = MediaType.APPLICATION_FORM_URLENCODED)
    @Post("/")
    public HttpResponse sensorsUpdate(@Body SensorsView update) throws URISyntaxException {
        handleUpdate(update);
        URI location = new URI("/probes");
        return HttpResponse.redirect(location);
    }

    private void handleUpdate(SensorsView update) {
        update.getSensors()
                .forEach(sensor -> {
                    sensorsService.updateSensor(sensor.getIdentifier(), !Objects.equals("Disabled", sensor.getMode()), sensor.getMode());
                });


    }

    private List<List<Object>> getReadings() {
        ArrayList<List<Object>> objects = new ArrayList<>();
        objects.addAll(new TreeMap<>(sensorsService.getReadingsByTimeStamps()).entrySet().stream()
                            .map(entry -> {
                                ArrayList<Object> v = new ArrayList<>();
                                v.add(entry.getKey().toEpochSecond());
                                v.addAll(entry.getValue().stream().map(reading ->
                                        reading.getValue() != null && sensorsService.isEnabled(reading.getIdentifier()) ?  reading.getValue().getValue() : null).collect(toList()));
                                return v;
                            })
                            .collect(toList()));
        return objects;
    }

}
