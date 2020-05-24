package simulator.model.organism;

import simulator.model.Config;

import java.util.ArrayList;
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
}
