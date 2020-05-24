package simulator.model.organism;

import simulator.model.Config;
import simulator.model.exceptions.NoAnimalActionException;

import java.util.List;

public class Herbivore extends Animal {

    public Herbivore() {
        super(OrganismType.HERBIVORE, Config.HERBIVORE_SIGHT, Config.HERBIVORE_MOVEMENT, Config.HERBIVORE_ENERGYLEVEL);
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
    public Organism feed(List<Organism> orgsInActionProx) throws NoAnimalActionException{
        throw new NoAnimalActionException("No action");
    }

    @Override
    public Animal mate(List<Organism> orgsInActionProx) throws NoAnimalActionException{
        throw new NoAnimalActionException("No action");
    }

    @Override
    public String move(List<Organism> orgsInSightProx) throws NoAnimalActionException {
        System.out.println("HERBIVORE: Searching through IDs...");
        String foundOrg = null;
        boolean searching = true;
        for(Organism organism : orgsInSightProx) {
            if(searching && organism instanceof Plant) {
                foundOrg = organism.getId();
                searching = false;
            }
        }
        if(searching) {
            throw new NoAnimalActionException("No Action");
        }
        return foundOrg;
    }
}
