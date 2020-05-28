package simulator.model.organism;

import simulator.model.DomainReadable;
import simulator.model.PositionVector;
import simulator.model.Space;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.SimulatorErrorException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import simulator.model.Config.OrganismType;

import static simulator.model.Config.*;

public class Carnivore extends Animal {

    public Carnivore() {
        super();
    }

    @Override
    public MoveType move(DomainReadable domain) throws InvalidIdentifierException {
        PositionVector position = domain.getPositionOfID(getID());
        PositionVector movePosition = null;
        PositionVector targetPosition = getFirstOccurrenceOfTypeFromSpaceList(domain.getSpacesInProximity(position, getSightRange()), OrganismType.HERBIVORE);
        if(targetPosition != null) {
            System.out.println("CARNIVORE: Found target at " + targetPosition);
            movePosition = getEmptyPosFromListClosestToTarget(domain.getSpacesInProximity(position, getMovementRange()), targetPosition);
        }else {
            System.out.println("CARNIVORE: No target");
            movePosition = getRandomOccurrenceOfTypeFromSpaceList(domain.getSpacesInProximity(position, getMovementRange()), OrganismType.VOID);
        }

        MoveType move;
        if(movePosition == null) {
            move = MoveType.NO_MOVE;
        }else {
            System.out.println("CARNIVORE: Returning move to " + movePosition);
            move = MoveType.MOVE_TO;
            move.setPosition(movePosition);
        }
        return move;
    }

    @Override
    public ActionType interact(DomainReadable domain) throws InvalidIdentifierException {
        return ActionType.NO_ACTION;
    }

    @Override
    public int getSightRange() {
        return CARNIVORE_SIGHT;
    }

    @Override
    public int getMovementRange() {
        return CARNIVORE_MOVEMENT;
    }

    @Override
    public int getMaxEnergy() {
        return CARNIVORE_MAX_ENERGY;
    }

    @Override
    public int getStartEnergy() {
        return CARNIVORE_START_ENERGY;
    }

    @Override
    public int getEnergyToMate() {
        return CARNIVORE_ENERGY_TO_MATE;
    }

    @Override
    public int getEnergyMateCost() {
        return CARNIVORE_ENERGY_MATE_COST;
    }

    @Override
    protected OrganismType getType() {
        return OrganismType.CARNIVORE;
    }

}
