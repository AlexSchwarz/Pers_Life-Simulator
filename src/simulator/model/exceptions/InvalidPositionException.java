package simulator.model.exceptions;

public class InvalidPositionException extends SimulatorException{
    /**
     * Default constructor
     * @param message Error message
     */
    public InvalidPositionException(String message) {
        super(message);
    }
}
