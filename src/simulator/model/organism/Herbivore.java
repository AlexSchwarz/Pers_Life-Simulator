package simulator.model.organism;

import simulator.model.Config;

import java.util.List;

public class Herbivore extends Animal {

    public Herbivore() {
        super(OrganismType.HERBIVORE, Config.HERBIVORE_SIGHT, Config.HERBIVORE_MOVEMENT, Config.HERBIVORE_ENERGYLEVEL, Config.HERBIVORE_ENERGYLEVEL);
    }

    @Override
    public String toString() {
        String orgString;
        if(Integer.parseInt(getId()) < 10) {
            orgString = "0" + getId() + "H";
        }else {
            orgString = getId() + "H";
        }
        return orgString;
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
        return Action.NO_ACTION;
    }

    @Override
    public Move move(List<Organism> orgsInSightProx) {
        return Move.RANODM_MOVE;
    }

    private Action findFromType(List<Organism> orgsInProx, Action action, OrganismType orgType) {
        boolean searching = true;
        System.out.println("HERBIVORE: Checking given IDs " + orgsInProx);
        for(Organism organism : orgsInProx) {
            if(searching && organism.getType().equals(orgType)) {
                action.setId(organism.getId());
                System.out.println("HERBIVORE: Found " + organism.getType() + " ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            action = Action.NO_ACTION;
            System.out.println("HERBIVORE: Found no point of interest");
        }
        return action;
    }
}
