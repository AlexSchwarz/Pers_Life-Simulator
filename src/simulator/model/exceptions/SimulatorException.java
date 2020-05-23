package simulator.model.exceptions;

public abstract class SimulatorException extends Exception{
    /**
     * Default constructor
     * @param message Error message
     */
    public SimulatorException(String message) {
        super(message);
    }

    /**
     * Constructor with extra parameter
     * @param message Error message
     * @param thrown {@link Throwable}
     */
    public SimulatorException(String message, Throwable thrown) {
        super(message, thrown);
    }

}
