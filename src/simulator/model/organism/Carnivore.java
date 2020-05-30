package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;
import simulator.model.PositionVector;
import simulator.model.Space;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.SimulatorErrorException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import simulator.model.Config.OrganismType;

import javax.swing.*;

import static simulator.model.Config.*;

public class Carnivore extends Animal {

    public Carnivore() {
        super();
    }

    @Override
    public MoveType move(PositionVector position, DomainReadable domain) {
        List<Space> sightList = domain.getSpacesInProximity(position, getSightRange());
        List<Space> moveList = domain.getSpacesInProximity(position, getMovementRange());
        PositionVector movePosition = null;
        PositionVector targetPosition;
        if(getEnergy() >= getEnergyToMate()) {
            //System.out.println("CARNIVORE: Move to mate...");
            targetPosition = getFirstOccurrenceOfTypeFromSpaceList(sightList, OrganismType.CARNIVORE);
        }else {
            //System.out.println("CARNIVORE: Move to feed...");
            targetPosition = getFirstOccurrenceOfTypeFromSpaceList(sightList, OrganismType.HERBIVORE);
        }
        if(targetPosition != null) {
            //System.out.println("CARNIVORE: Found target at " + targetPosition);
            movePosition = getEmptyPosFromListClosestToTarget(moveList, targetPosition);
        }else {
            //System.out.println("CARNIVORE: No target");
            movePosition = getRandomOccurrenceOfTypeFromSpaceList(moveList, OrganismType.VOID);
        }

        MoveType move;
        if(movePosition == null) {
            //System.out.println("CARNIVORE: Returning no move");
            move = MoveType.NO_MOVE;
        }else {
            //System.out.println("CARNIVORE: Returning move to " + movePosition);
            move = MoveType.MOVE_TO;
            move.setPosition(movePosition);
        }
        return move;
    }

    @Override
    public ActionType interact(PositionVector position, DomainReadable domain) {
        List<Space> actionList = domain.getSpacesInProximity(position, INTERACTION_RANGE);
        ActionType action;
        PositionVector targetPosition;
        if(getEnergy() >= getEnergyToMate()) {
            //System.out.println("CARNIVORE: Action to mate...");
            action = ActionType.MATE_WITH;
            targetPosition = getFirstOccurrenceOfTypeFromSpaceList(actionList, OrganismType.CARNIVORE);
        } else {
            //System.out.println("CARNIVORE: Action to feed...");
            action = ActionType.FEED_ON;
            targetPosition = getFirstOccurrenceOfTypeFromSpaceList(actionList, OrganismType.HERBIVORE);
        }

        if(targetPosition == null) {
            action = ActionType.NO_ACTION;
        }else {
            action.setPosition(targetPosition);
        }
        return action;
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
