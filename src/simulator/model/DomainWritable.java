package simulator.model;

import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;
import simulator.model.exceptions.SimulatorErrorException;

import java.util.Objects;
import java.util.Random;

public class DomainWritable extends DomainReadable{

    public DomainWritable(int size) throws SimulatorErrorException {
        super(size);
    }

    public void initIDRandomPlacement(String identifier) {
        Random random = new Random();
        boolean searching = true;
        while(searching) {
            int randomX = random.nextInt(size);
            int randomY = random.nextInt(size);
            PositionVector randomPosition = new PositionVector(randomX, randomY);
            try {
                setIDAtPosition(identifier, randomPosition);
                System.out.println("DOMAIN: -> Init placement of ID " + identifier + " at " + randomPosition);
                searching = false;
            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setIDAtPosition(String id, PositionVector position) throws InvalidPositionException {
        Objects.requireNonNull(position);
        Objects.requireNonNull(id);
        if(getContentAtPosition(position).equals(BLANK)) {
            System.out.println("DOMAIN: Set content " + id + " at position " + position);
            domainArray[position.getY()][position.getX()].setContent(id);
        } else {
            throw new InvalidPositionException("Error: Position: " + position + " is taken");
        }
    }

    public void removeID(String id) throws InvalidIdentifierException {
        Objects.requireNonNull(id);
        PositionVector pos = getPositionOfId(id);
        domainArray[pos.getY()][pos.getX()].setBlank();
    }

    public void moveID(String identifier, PositionVector pos) throws InvalidIdentifierException, InvalidPositionException {
        removeID(identifier);
        setIDAtPosition(identifier, pos);
        System.out.println("DOMAIN: Moved content " + identifier + " to " + pos);
    }
}
