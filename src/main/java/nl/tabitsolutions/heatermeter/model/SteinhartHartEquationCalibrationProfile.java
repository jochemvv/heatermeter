package nl.tabitsolutions.heatermeter.model;

import java.math.BigDecimal;

public class SteinhartHartEquationCalibrationProfile implements CalibrationProfile<Long> {

//    private final double logCalibrationResistance1;
//    private final double logCalibrationResistance2;
//    private final double logCalibrationResistance3;
//
//    private final double inverseTemp1;
//    private final double inverseTemp2;
//    private final double inverseTemp3;
//
//    private final double gma2;
//    private final double gma3;

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
//        this.logCalibrationResistance1 = Math.log(calibrationResistance1);
//        this.logCalibrationResistance2 = Math.log(calibrationResistance2);
//        this.logCalibrationResistance3 = Math.log(calibrationResistance3);
//
//        this.inverseTemp1 = 1d / temp1;
//        this.inverseTemp2 = 1d / temp2;
//        this.inverseTemp3 = 1d / temp3;
//
//        this.gma2 = (this.inverseTemp2 - this.inverseTemp1) / (this.logCalibrationResistance2 - this.logCalibrationResistance1);
//        this.gma3 = (this.inverseTemp3 - this.inverseTemp1) / (this.logCalibrationResistance3 - this.logCalibrationResistance1);
//
//        this.C = ((gma3 - gma2) / (this.logCalibrationResistance3 - this.logCalibrationResistance2)) * Math.pow((this.logCalibrationResistance1 + this.logCalibrationResistance2 + this.logCalibrationResistance3), -1);
//        this.B = gma2 - C * (Math.pow(this.logCalibrationResistance1, 2) + this.logCalibrationResistance1 * this.logCalibrationResistance2 + Math.pow(this.logCalibrationResistance2, 2));
//        this.A = this.inverseTemp1 - (B + Math.pow(this.logCalibrationResistance1, 2) * C) * this.logCalibrationResistance1;

        double L1 = Math.log(calibrationResistance1);
        double L2 = Math.log(calibrationResistance2);
        double L3 = Math.log(calibrationResistance3);
        double Y1 = 1 / temp1;
        double Y2 = 1 / temp2;
        double Y3 = 1 / temp3;
        double gma2 = (Y2 - Y1) / (L2 - L1);
        double gma3 = (Y3 - Y1) / (L3 - L1);

        // A, B, and C are doubleiables used in the Steinhart-Hart equation
        // to determine temperature from resistance in a thermistor. These
        // values will be set during the init() function.
        this.C = ((gma3 - gma2) / (L3 - L2)) * Math.pow((L1 + L2 + L3), -1);
        this.B = gma2 - C * (Math.pow(L1, 2) + L1 * L2 + Math.pow(L2, 2));
        this.A = Y1 - (B + Math.pow(L1, 2) * C) * L1;

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
        // adcVal = resistance / (resistance + serialResistor) * Vcc * 1023 / Varef
        // adcVal = resistance / (resistance + this.R) * 3.3 * (Math.pow(2, 15) - 1) / 4.09
        // adcVal = resistance / (resistance + this.R) * 26438
        // accVAl = R / R + this.R

//        return (this.R / ((26438d / adcValue) - 1d));
        return (this.R / (((Math.pow(2, 15) - 1) / adcValue) - 1d));
    }

    public double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
