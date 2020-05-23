package model.exceptions;

public class IllegalEnvironmentException extends SimulatorException{
    /**
     * Default constructor
     * @param message Error message
     */
    public IllegalEnvironmentException(String message) {
        super(message);
    }
}
