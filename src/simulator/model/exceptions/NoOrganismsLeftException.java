package simulator.model.exceptions;

public class NoOrganismsLeftException extends SimulatorException{

    /**
     * Default constructor
     * @param message Error message
     */
    public NoOrganismsLeftException(String message) {
        super(message);
    }
}
