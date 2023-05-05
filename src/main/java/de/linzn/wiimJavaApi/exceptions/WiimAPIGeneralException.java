package de.linzn.wiimJavaApi.exceptions;

public class WiimAPIGeneralException extends IllegalArgumentException {
    public WiimAPIGeneralException(String input) {
        super(input);
    }
}
