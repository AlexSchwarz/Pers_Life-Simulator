package simulator.model.exceptions;

public class EnvironmentCycleCompleteException extends SimulatorException{
    /**
     * Default constructor
     * @param message Error message
     */
    public EnvironmentCycleCompleteException(String message) {
        super(message);
    }
}
