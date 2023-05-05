package de.linzn.wiimJavaApi.exceptions;

public class WiimAPINoDataException extends IllegalArgumentException {
    public WiimAPINoDataException() {
        super("No data in this field yet!");
    }
}
