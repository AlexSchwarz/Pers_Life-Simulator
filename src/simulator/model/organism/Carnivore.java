package simulator.model.organism;

import simulator.model.Config;

import java.util.List;

public class Carnivore extends Animal {

    public Carnivore() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder orgString = new StringBuilder();
        orgString.append(getId());
        orgString.append("C");
        orgString.append("\n");
        orgString.append("A"+ getAge());
        orgString.append("E" + getEnergyLevel());
        return orgString.toString();
    }

    @Override
    public String[] getDataArray() {
        String id = super.getId();
        String type = "Carnivore";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }

    @Override
    public Action interact(List<Organism> orgsInActionProx) {
        System.out.println("CARNIVORE: ID" + getId() + " Energy " + getEnergyLevel() + " Attempting interaction...");
        Action interaction;
        if(getEnergyLevel() >= Config.CARNIVORE_ENERGY_TO_MATE) {
            interaction = findFromTypeForAction(orgsInActionProx, Action.MATE_WITH, Config.OrganismType.CARNIVORE);
        }else {
            interaction = findFromTypeForAction(orgsInActionProx, Action.FEED_ON, Config.OrganismType.HERBIVORE);
        }
        return  interaction;
    }

    @Override
    public Move move(List<Organism> orgsInSightProx) {
        System.out.println("CARNIVORE: ID" + getId() + " Energy " + getEnergyLevel() + " Attempting move...");
        Move move;
        if(getEnergyLevel() >= Config.CARNIVORE_ENERGY_TO_MATE) {
            System.out.println("CARNIVORE: Wants to mate...");
            move = findFromTypeForMove(orgsInSightProx, Move.MOVE_TO, Config.OrganismType.CARNIVORE);
        }else {
            System.out.println("CARNIVORE: Wants to feed...");
            move = findFromTypeForMove(orgsInSightProx, Move.MOVE_TO, Config.OrganismType.HERBIVORE);
        }
        return move;
    }

    @Override
    public int getSightRange() {
        return Config.CARNIVORE_SIGHT;
    }

    @Override
    public int getMovementRange() {
        return Config.CARNIVORE_MOVEMENT;
    }

    @Override
    public int getMaxEnergyLevel() {
        return Config.CARNIVORE_MAX_ENERGYLEVEL;
    }

    @Override
    public int getStartEnergy() {
        return Config.CARNIVORE_START_ENERGYLEVEL;
    }

    @Override
    public int getEnergyToMate() {
        return Config.CARNIVORE_ENERGY_TO_MATE;
    }

    @Override
    public int getEnergyMateCost() {
        return Config.CARNIVORE_ENERGY_MATE_COST;
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.CARNIVORE;
    }

    private Action findFromTypeForAction(List<Organism> orgsInProx, Action action, Config.OrganismType orgType) {
        boolean searching = true;
        System.out.println("CARNIVORE: Checking given IDs " + orgsInProx + " for match of action " + action);
        for(Organism organism : orgsInProx) {
            if(searching && organism.getType().equals(orgType)) {
                action.setId(organism.getId());
                System.out.println("CARNIVORE: Found " + organism.getType() + " ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            action = Action.NO_ACTION;
            System.out.println("CARNIVORE: Found no point of interest");
        }
        return action;
    }

    private Move findFromTypeForMove(List<Organism> orgsInProx, Move moveType, Config.OrganismType orgType) {
        Move newMove = moveType;
        newMove.resetList();
        boolean searching = true;
        System.out.println("CARNIVORE: Checking given IDs " + orgsInProx + " for match of move " + newMove);
        for(Organism organism : orgsInProx) {
            if(organism.getType().equals(orgType)) {
                newMove.addId(organism.getId());
                System.out.println("CARNIVORE: Found " + organism.getType() + " ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            newMove = Move.RANODM_MOVE;
            System.out.println("CARNIVORE: Found no point of interest");
        }
        return newMove;
    }
}
