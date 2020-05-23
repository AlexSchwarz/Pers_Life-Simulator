package simulator.model.organism;

import simulator.model.Config;

public class Carnivore extends Animal {

    public Carnivore() {
        super(OrganismType.CARNIVORE, Config.CARNIVORE_SIGHT, Config.CARNIVORE_MOVEMENT);
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
}