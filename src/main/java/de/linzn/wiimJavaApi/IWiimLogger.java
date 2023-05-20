package de.linzn.wiimJavaApi;

public interface IWiimLogger {
    void error(Object input);

    void warning(Object input);

    void info(Object input);

    void debug(Object input);
}
