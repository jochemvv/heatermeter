package nl.tabitsolutions.heatermeter.config;

import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CalibrationConfiguration {


    @Bean(name = "ikeaCalibration")
    @Primary
    public SteinhartHartEquationCalibrationProfile ikeaCalibration() {
        return new SteinhartHartEquationCalibrationProfile("Ikea", 142000d, 52000d, 4300d, 275d, 298d, 364d, 100000d);
    }

    @Bean(name = "mastradCalibration")
    public SteinhartHartEquationCalibrationProfile mastradCalibration() {
        return new SteinhartHartEquationCalibrationProfile("Mastrad", 785000d, 248000d, 22000d, 273d, 296d, 355d, 100000d);
    }

    @Bean(name = "aldiCalibration")
    public SteinhartHartEquationCalibrationProfile aldiCalibration() {
        return new SteinhartHartEquationCalibrationProfile("Aldi", 325000d, 102000d, 10000d, 276d, 297d, 325d, 100000d);
    }

    @Bean(name = "thermoWorks")
    public SteinhartHartEquationCalibrationProfile thermoWorksCalibration() {
        return new SteinhartHartEquationCalibrationProfile("ThermoWorks",
                7.3431401e-4,
                2.1574370e-4,
                9.5156860e-8,
                100000d
                );
    }
}
