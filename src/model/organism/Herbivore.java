package model.organism;

import model.Config;

public class Herbivore extends Animal {

    public Herbivore() {
        super(OrganismType.HERBIVORE, Config.HERBIVORE_SIGHT, Config.HERBIVORE_MOVEMENT);
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
}
