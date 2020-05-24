package simulator.model.exceptions;

public class NoAnimalActionException extends SimulatorException{
    /**
     * Default constructor
     * @param message Error message
     */
    public NoAnimalActionException(String message) {
        super(message);
    }
}
