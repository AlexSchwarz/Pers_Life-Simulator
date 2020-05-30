package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;
import simulator.model.PositionVector;
import simulator.model.Space;
import simulator.model.exceptions.InvalidIdentifierException;

import java.util.List;

import static simulator.model.Config.INTERACTION_RANGE;

public class Herbivore extends Animal {

    public Herbivore() {
        super();
    }

    @Override
    public Config.MoveType move(PositionVector position, DomainReadable domain) {
        List<Space> fearList = domain.getSpacesInProximity(position, getFearRange());
        List<Space> sightList = domain.getSpacesInProximity(position, getSightRange());
        List<Space> moveList = domain.getSpacesInProximity(position, getMovementRange());
        PositionVector movePosition = null;
        PositionVector targetPosition;
        targetPosition = getFirstOccurrenceOfTypeFromSpaceList(fearList, Config.OrganismType.CARNIVORE);
        if(targetPosition != null) {
            //System.out.println("HERBIVORE: Running away...");
            movePosition = getEmptyPosFromListFurthestFromTarget(moveList, targetPosition);
        } else {
            if(getEnergy() >= getEnergyToMate()) {
                //System.out.println("HERBIVORE: Wants to mate...");
                targetPosition = getFirstOccurrenceOfTypeFromSpaceList(sightList, Config.OrganismType.HERBIVORE);
            }else {
                //System.out.println("HERBIVORE: Wants to feed...");
                targetPosition = getFirstOccurrenceOfTypeFromSpaceList(sightList, Config.OrganismType.PLANT);
            }

            if (targetPosition != null) {
                //System.out.println("HERBIVORE: Found target at " + targetPosition);
                movePosition = getEmptyPosFromListClosestToTarget(moveList, targetPosition);
            } else {
                //System.out.println("HERBIVORE: No target");
                movePosition = getRandomOccurrenceOfTypeFromSpaceList(moveList, Config.OrganismType.VOID);
            }
        }

        Config.MoveType move;
        if(movePosition == null) {
            //System.out.println("HERBIVORE: Returning no move");
            move = Config.MoveType.NO_MOVE;
        }else {
            //System.out.println("HERBIVORE: Returning move to " + movePosition);
            move = Config.MoveType.MOVE_TO;
            move.setPosition(movePosition);
        }
        return move;
    }

    @Override
    public Config.ActionType interact(PositionVector position, DomainReadable domain) {
        List<Space> actionList = domain.getSpacesInProximity(position, INTERACTION_RANGE);
        Config.ActionType action;
        PositionVector targetPosition;
        if(getEnergy() >= getEnergyToMate()) {
            //System.out.println("CARNIVORE: Action to mate...");
            action = Config.ActionType.MATE_WITH;
            targetPosition = getFirstOccurrenceOfTypeFromSpaceList(actionList, Config.OrganismType.HERBIVORE);
        } else {
            //System.out.println("CARNIVORE: Action to feed...");
            action = Config.ActionType.FEED_ON;
            targetPosition = getFirstOccurrenceOfTypeFromSpaceList(actionList, Config.OrganismType.PLANT);
        }

        if(targetPosition == null) {
            action = Config.ActionType.NO_ACTION;
        }else {
            action.setPosition(targetPosition);
        }
        return action;
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

    public int getFearRange() {
        return Config.HERBIVORE_FEAR;
    }
}
