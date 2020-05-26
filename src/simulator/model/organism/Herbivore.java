package simulator.model.organism;

import simulator.model.Config;

import java.util.List;

public class Herbivore extends Animal {

    public Herbivore() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder orgString = new StringBuilder();
        orgString.append(getId());
        orgString.append("H");
        orgString.append("\n");
        orgString.append("A"+ getAge());
        orgString.append("E" + getEnergyLevel());
        return orgString.toString();
    }

    @Override
    public String[] getDataArray() {
        String id = super.getId();
        String type = "Herbivore";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }

    @Override
    public Action interact(List<Organism> orgsInActionProx) {
        System.out.println("HERBIVORE: ID" + getId() + " Energy " + getEnergyLevel() + " Attempting interaction...");
        Action interaction;
        if(getEnergyLevel() >= Config.HERBIVORE_ENERGY_TO_MATE) {
            interaction = findFromTypeForAction(orgsInActionProx, Action.MATE_WITH, Config.OrganismType.HERBIVORE);
        }else {
            interaction = findFromTypeForAction(orgsInActionProx, Action.FEED_ON, Config.OrganismType.PLANT);
        }
        return  interaction;
    }

    private Action findFromTypeForAction(List<Organism> orgsInProx, Action action, Config.OrganismType orgType) {
        boolean searching = true;
        System.out.println("HERBIVORE: Checking given IDs " + orgsInProx + " for match of action " + action);
        for(Organism organism : orgsInProx) {
            if(searching && organism.getType().equals(orgType)) {
                action.setId(organism.getId());
                System.out.println("HERBIVORE: Found " + organism.getType() + " ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            action = Action.NO_ACTION;
            System.out.println("CARNIVORE: Found no point of interest");
        }
        return action;
    }

    @Override
    public Move move(List<Organism> orgsInSightProx) {
        System.out.println("HERBIVORE: ID" + getId() + " Energy " + getEnergyLevel() + " Attempting move...");
        Move move = Move.RANODM_MOVE;
        //Move move = findFromTypeForMove(orgsInSightProx, Move.RUN_FROM, Config.OrganismType.CARNIVORE);
        if(move.equals(Move.RANODM_MOVE)) {
            if (getEnergyLevel() >= Config.HERBIVORE_ENERGY_TO_MATE) {
                System.out.println("HERBIVORE: Wants to mate...");
                move = findFromTypeForMove(orgsInSightProx, Move.MOVE_TO, Config.OrganismType.HERBIVORE);
            } else {
                System.out.println("HERBIVORE: Wants to feed...");
                move = findFromTypeForMove(orgsInSightProx, Move.MOVE_TO, Config.OrganismType.PLANT);
            }
        }
        return move;
    }

    private Move findFromTypeForMove(List<Organism> orgsInProx, Move moveType, Config.OrganismType orgType) {
        Move newMove = moveType;
        newMove.resetList();
        boolean searching = true;
        System.out.println("HERBIVORE: Checking given IDs " + orgsInProx + " for match of move " + newMove);
        for(Organism organism : orgsInProx) {
            if(organism.getType().equals(orgType)) {
                newMove.addId(organism.getId());
                System.out.println("HERBIVORE: Found " + organism.getType() + " ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            newMove = Move.RANODM_MOVE;
            System.out.println("HERBIVORE: Found no point of interest");
        }
        return newMove;
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
    public int getMaxEnergyLevel() {
        return Config.HERBIVORE_MAX_ENERTGYLEVEL;
    }

    @Override
    public int getStartEnergy() {
        return Config.HERBIVORE_START_ENERGYLEVEL;
    }

    @Override
    public int getEnergyToMate() {
        return Config.HERBIVORE_ENERGY_TO_MATE;
    }

    @Override
    public int getEnergyMateCost() {
        return Config.HERVIBORE_ENERGY_MATE_COST;
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.HERBIVORE;
    }
}
