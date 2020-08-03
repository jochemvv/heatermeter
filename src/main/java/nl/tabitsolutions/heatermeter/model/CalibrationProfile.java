package nl.tabitsolutions.heatermeter.model;

public interface CalibrationProfile<T> {

    T getCalibratedValue(T rawValue);

}
