package simulator.model.organism;

import simulator.model.Config;
import simulator.model.exceptions.NoAnimalActionException;

import java.util.List;
import java.util.Objects;

public class Carnivore extends Animal {

    public Carnivore() {
        super(OrganismType.CARNIVORE, Config.CARNIVORE_SIGHT, Config.CARNIVORE_MOVEMENT, Config.CARNIVORE_ENERGYLEVEL);
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
    public Organism feed(List<Organism> orgsInActionProx) throws NoAnimalActionException {
        System.out.println("CARNIVORE: " + getId() + " Attempting feed...");
        System.out.println("CARNIVORE: Searching through IDs...");
        Organism foundOrg = null;
        boolean searching = true;
        for(Organism organism : orgsInActionProx) {
            if(searching && organism instanceof Herbivore) {
                foundOrg = organism;
                System.out.println("CARNIVORE: Found Herbivore ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            System.out.println("CARNIVORE: No point of Interest found");
            throw new NoAnimalActionException("No Action");
        }
        Objects.requireNonNull(foundOrg);
        return foundOrg;
    }

    @Override
    public Animal mate(List<Organism> orgsInActionProx) throws NoAnimalActionException{
        System.out.println("CARNIVORE: " + getId() + " Attempting mate...");
        System.out.println("CARNIVORE: Searching through IDs...");
        Animal foundOrg = null;
        boolean searching = true;
        for(Organism organism : orgsInActionProx) {
            if(searching && organism instanceof Carnivore) {
                foundOrg = (Animal) organism;
                System.out.println("CARNIVORE: Found Herbivore ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            System.out.println("CARNIVORE: No point of Interest found");
            throw new NoAnimalActionException("No Action");
        }
        Objects.requireNonNull(foundOrg);
        return foundOrg;
    }

    @Override
    public String move(List<Organism> orgsInSightProx) throws NoAnimalActionException{
        System.out.println("CARNIVORE: " + getId() + " Attempting move...");
        System.out.println("CARNIVORE: Searching through IDs...");
        String foundOrg = null;
        boolean searching = true;
        for(Organism organism : orgsInSightProx) {
            if(searching && organism instanceof Herbivore) {
                foundOrg = organism.getId();
                System.out.println("CARNIVORE: Found Herbivore ID " + organism.getId());
                searching = false;
            }
        }
        if(searching) {
            System.out.println("CARNIVORE: No point of Interest found");
            throw new NoAnimalActionException("No Action");
        }
        Objects.requireNonNull(foundOrg);
        return foundOrg;
    }
}
