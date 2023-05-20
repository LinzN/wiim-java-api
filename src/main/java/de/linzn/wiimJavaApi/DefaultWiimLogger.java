package de.linzn.wiimJavaApi;

class DefaultWiimLogger implements IWiimLogger{
    @Override
    public void error(Object input) {
        System.err.println(input);
    }

    @Override
    public void warning(Object input) {
        System.out.println(input);
    }

    @Override
    public void info(Object input) {
        System.out.println(input);
    }

    @Override
    public void debug(Object input) {
        System.out.println(input);
    }
}
