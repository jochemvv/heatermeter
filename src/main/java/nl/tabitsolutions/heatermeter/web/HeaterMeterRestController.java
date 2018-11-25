package nl.tabitsolutions.heatermeter.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/heater-meter")
public class HeaterMeterRestController {

    @GetMapping("/health")
    public String health() {
        return "up";
    }
}
