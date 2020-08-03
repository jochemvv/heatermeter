package nl.tabitsolutions.heatermeter.model;

import java.math.BigDecimal;

public class SteinhartHartEquationCalibrationProfile implements CalibrationProfile<Long> {

    private final double logCalibrationResistance1;
    private final double logCalibrationResistance2;
    private final double logCalibrationResistance3;

    private final double inverseTemp1;
    private final double inverseTemp2;
    private final double inverseTemp3;

    private final double gma2;
    private final double gma3;

    private final double A;
    private final double B;
    private final double C;

    private final double R;

    public SteinhartHartEquationCalibrationProfile(double calibrationResistance1,
                                                   double calibrationResistance2,
                                                   double calibrationResistance3,
                                                   double temp1,
                                                   double temp2,
                                                   double temp3,
                                                   double seriesResistance) {
        this.logCalibrationResistance1 = Math.log(calibrationResistance1);
        this.logCalibrationResistance2 = Math.log(calibrationResistance2);
        this.logCalibrationResistance3 = Math.log(calibrationResistance3);

        this.inverseTemp1 = 1d / temp1;
        this.inverseTemp2 = 1d / temp2;
        this.inverseTemp3 = 1d / temp3;

        this.gma2 = (this.inverseTemp2 - this.inverseTemp1) / (this.logCalibrationResistance2 - this.logCalibrationResistance1);
        this.gma3 = (this.inverseTemp3 - this.inverseTemp1) / (this.logCalibrationResistance3 - this.logCalibrationResistance1);

        this.C = ((gma3 - gma2) / (this.logCalibrationResistance3 - this.logCalibrationResistance2)) * Math.pow((this.logCalibrationResistance1 + this.logCalibrationResistance2 + this.logCalibrationResistance3), -1);
        this.B = gma2 - C * (Math.pow(this.logCalibrationResistance1, 2) + this.logCalibrationResistance1 * this.logCalibrationResistance2 + Math.pow(this.logCalibrationResistance2, 2));
        this.A = this.inverseTemp1 - (B + Math.pow(this.logCalibrationResistance1, 2) * C) * this.logCalibrationResistance1;

        this.R = seriesResistance;
    }

    @Override
    public Long getCalibratedValue(Long rawValue) {
        double r = this.adcValueToResistance(rawValue);
        double tempK = this.resistanceToKelvin(r);
        return BigDecimal.valueOf(kelvinToCelsius(tempK)).longValue();
    }

    public double resistanceToKelvin(double resistance) {
        return 1 / (A + B * Math.log(resistance) + C * Math.pow(Math.log(resistance), 3));
    }

    public double adcValueToResistance(long adcValue) {
        return (this.R / ((65535d / adcValue) - 1d));
    }

    public double kelvinToCelsius(double kelvin) {
        double tempC = kelvin - 273.15;
        return Math.round(tempC * 10d) / 10d;
    }
}
