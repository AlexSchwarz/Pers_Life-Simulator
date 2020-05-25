package simulator.model.organism;

import simulator.model.Config;

import java.util.List;

public class Carnivore extends Animal {

    public Carnivore() {
        super(OrganismType.CARNIVORE, Config.CARNIVORE_SIGHT, Config.CARNIVORE_MOVEMENT, Config.CARNIVORE_MAX_ENERGYLEVEL, Config.CARNIVORE_START_ENERGYLEVEL);
    }

    @Override
    public String toString() {
        String orgString;
        if(Integer.parseInt(getId()) < 10) {
            orgString = "0" + getId() + "C";
        }else {
            orgString = getId() + "C";
        }
        return orgString;
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
        if(getEnergyLevel() > 7) {
            interaction = findFromTypeForAction(orgsInActionProx, Action.MATE_WITH, OrganismType.CARNIVORE);
        }else {
            interaction = findFromTypeForAction(orgsInActionProx, Action.FEED_ON, OrganismType.HERBIVORE);
        }
        return  interaction;
    }

    @Override
    public Move move(List<Organism> orgsInSightProx) {
        System.out.println("CARNIVORE: ID" + getId() + " Energy " + getEnergyLevel() + " Attempting move...");
        Move move;
        if(getEnergyLevel() > 7) {
            System.out.println("CARNIVORE: Wants to mate...");
            move = findFromTypeForMove(orgsInSightProx, Move.MOVE_TO, OrganismType.CARNIVORE);
        }else {
            System.out.println("CARNIVORE: Wants to feed...");
            move = findFromTypeForMove(orgsInSightProx, Move.MOVE_TO, OrganismType.HERBIVORE);
        }
        return move;
    }

    private Action findFromTypeForAction(List<Organism> orgsInProx, Action action, OrganismType orgType) {
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

    private Move findFromTypeForMove(List<Organism> orgsInProx, Move move, OrganismType orgType) {
        move.resetList();
        boolean searching = true;
        System.out.println("CARNIVORE: Checking given IDs " + orgsInProx + " for match of move " + move);
        for(Organism organism : orgsInProx) {
            if(organism.getType().equals(orgType)) {
                move.addId(organism.getId());
                System.out.println("CARNIVORE: Found " + organism.getType() + " ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            move = Move.RANODM_MOVE;
            System.out.println("CARNIVORE: Found no point of interest");
        }
        return move;
    }
}
