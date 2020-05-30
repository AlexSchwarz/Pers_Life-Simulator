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

    public void initIDRandomPlacement(Identification id) {
        Random random = new Random();
        boolean searching = true;
        while(searching) {
            int randomX = random.nextInt(size);
            int randomY = random.nextInt(size);
            PositionVector randomPosition = new PositionVector(randomX, randomY);
            try {
                setIDAtPosition(id, randomPosition);
                //System.out.println("DOMAIN: -> Init placement of ID " + id.toString() + " at " + randomPosition);
                searching = false;
            } catch (InvalidPositionException e) {
                //System.out.println(e.getMessage());
            }
        }
    }

    public void setIDAtPosition(Identification id, PositionVector position) throws InvalidPositionException {
        Objects.requireNonNull(position);
        Objects.requireNonNull(id);
        if(getIDAtPosition(position).isBlank()) {
            //System.out.println("DOMAIN: Set content " + id.toString() + " at position " + position);
            domainArray[position.getY()][position.getX()].setContent(id);
        } else {
            throw new InvalidPositionException("Error: Position: " + position + " is taken");
        }
    }

    public void removeID(Identification id) throws InvalidIdentifierException {
        Objects.requireNonNull(id);
        PositionVector pos = getPositionOfID(id);
        domainArray[pos.getY()][pos.getX()].setBlank();
    }

    public void moveID(Identification id, PositionVector pos) throws InvalidIdentifierException, InvalidPositionException {
        removeID(id);
        setIDAtPosition(id, pos);
        //System.out.println("DOMAIN: Moved content " + id.toString() + " to " + pos);
    }
}
