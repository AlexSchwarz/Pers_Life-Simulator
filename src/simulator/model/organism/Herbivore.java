package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;
import simulator.model.PositionVector;
import simulator.model.exceptions.InvalidIdentifierException;

public class Herbivore extends Animal {

    public Herbivore() {
        super();
    }

    @Override
    public Config.MoveType move(DomainReadable domain) throws InvalidIdentifierException {
        PositionVector position = domain.getPositionOfID(getID());
        PositionVector movePosition = null;
        PositionVector targetPosition = getFirstOccurrenceOfTypeFromSpaceList(domain.getSpacesInProximity(position, getSightRange()), Config.OrganismType.PLANT);
        if(targetPosition != null) {
            System.out.println("HERBIVORE: Found target at " + targetPosition);
            movePosition = getEmptyPosFromListClosestToTarget(domain.getSpacesInProximity(position, getMovementRange()), targetPosition);
        }else {
            System.out.println("HERBIVORE: No target");
            movePosition = getRandomOccurrenceOfTypeFromSpaceList(domain.getSpacesInProximity(position, getMovementRange()), Config.OrganismType.VOID);
        }

        Config.MoveType move;
        if(movePosition == null) {
            move = Config.MoveType.NO_MOVE;
        }else {
            System.out.println("HERBIVORE: Returning move to " + movePosition);
            move = Config.MoveType.MOVE_TO;
            move.setPosition(movePosition);
        }
        return move;
    }

    @Override
    public Config.ActionType interact(DomainReadable domain) {
       return Config.ActionType.NO_ACTION;
    }

    @Override
    public int getSightRange() {
        return Config.HERBIVORE_SIGHT;
    }

    @Override
    public int getMovementRange() {
        return Config.HERBIVORE_MOVEMENT;
    }

    @Override
    public int getMaxEnergy() {
        return Config.HERBIVORE_MAX_ENERGY;
    }

    @Override
    public int getStartEnergy() {
        return Config.HERBIVORE_START_ENERGY;
    }

    @Override
    public int getEnergyToMate() {
        return Config.HERBIVORE_ENERGY_TO_MATE;
    }

    @Override
    public int getEnergyMateCost() {
        return Config.HERBIVORE_ENERGY_MATE_COST;
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.HERBIVORE;
    }
}
