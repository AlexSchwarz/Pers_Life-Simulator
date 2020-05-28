package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;
import simulator.model.PositionVector;
import simulator.model.Space;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;
import simulator.model.exceptions.SimulatorErrorException;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static simulator.model.Config.SIZE;

public abstract class Animal extends Organism {

    public Animal() {
        super();
    }

    public abstract Config.ActionType interact(DomainReadable domain) throws InvalidIdentifierException;

    public abstract Config.MoveType move(DomainReadable domain) throws InvalidIdentifierException;

    public abstract int getSightRange();

    public abstract int getMovementRange();

    public abstract int getEnergyToMate();

    public abstract int getEnergyMateCost();

    public PositionVector getFirstOccurrenceOfTypeFromSpaceList(List<Space> spaceList, Config.OrganismType target) {
        Space foundSpace = spaceList
                .stream()
                .filter(space -> space.getContent().getType().equals(target))
                .findFirst()
                .orElse(null);
        PositionVector foundPos = null;
        if(foundSpace != null) {
            foundPos = foundSpace.getPosition();
        }
        System.out.println("ANIMAL: First occurrence of " + target.toString() + " at " + foundPos);
        return foundPos;
    }

    public PositionVector getEmptyPosFromListClosestToTarget(List<Space> moveList, PositionVector targetPosition) {
        List<Space> emptyInMoveRange = moveList
                .stream()
                .filter(space -> space.getContent().getType().equals(Config.OrganismType.VOID))
                .collect(Collectors.toList());
        PositionVector foundPosition = null;
        double shortestDistance = SIZE;
        for(Space space : emptyInMoveRange) {
            double distance = PositionVector.magnitude(PositionVector.subtract(targetPosition, space.getPosition()));
            if(distance < shortestDistance) {
                foundPosition = space.getPosition();
                shortestDistance = distance;
            }
        }
        System.out.println("ANIMAL: Empty Pos closest to " + targetPosition + " at " + foundPosition);
        return foundPosition;
    }

    public PositionVector getRandomOccurrenceOfTypeFromSpaceList(List<Space> spaceList, Config.OrganismType type) {
        Collections.shuffle(spaceList);
        Space foundSpace = spaceList
                .stream()
                .filter(space -> space.getContent().getType().equals(type))
                .findFirst()
                .orElse(null);
        PositionVector foundPos = null;
        if(foundSpace != null) {
            foundPos = foundSpace.getPosition();
        }
        System.out.println("ANIMAL: Random " + type.toString() + " at " + foundPos);
        return foundPos;
    }
}
