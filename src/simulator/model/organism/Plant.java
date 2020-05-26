package simulator.model.organism;

import simulator.model.Config;

public class Plant extends Organism {

    public Plant() {
        super();
    }

    @Override
    public String toString() {
        StringBuilder orgString = new StringBuilder();
        orgString.append(getId());
        orgString.append("P");
        orgString.append("\n");
        orgString.append("E" + getEnergyLevel());
        return orgString.toString();
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.PLANT;
    }

    @Override
    public String[] getDataArray() {
        String id = super.getId();
        String type = "Plant";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }

    @Override
    public int getEnergyLevel() {
        return Config.PLANT_MAX_ENERGYLEVEL;
    }
}
