package nl.tabitsolutions.heatermeter.components.sensors;

import nl.tabitsolutions.heatermeter.model.Sensor;
import nl.tabitsolutions.heatermeter.model.SteinhartHartEquationCalibrationProfile;

public abstract class AbstractTemperatureSensor extends Sensor<Long> {

    private volatile SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile;

    public AbstractTemperatureSensor(String identifier,
                                     SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        super(identifier);
        this.steinhartHartEquationCalibrationProfile = steinhartHartEquationCalibrationProfile;
    }

    public SteinhartHartEquationCalibrationProfile getSteinhartHartEquationCalibrationProfile() {
        return steinhartHartEquationCalibrationProfile;
    }

    public void setSteinhartHartEquationCalibrationProfile(SteinhartHartEquationCalibrationProfile steinhartHartEquationCalibrationProfile) {
        this.steinhartHartEquationCalibrationProfile = steinhartHartEquationCalibrationProfile;
    }

}
