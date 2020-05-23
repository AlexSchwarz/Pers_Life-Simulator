package model.organism;

public class Plant extends Organism {

    public Plant() {
        super(OrganismType.PLANT);
    }

    @Override
    public String toString() {
        String orgString;
        if(Integer.parseInt(getOrganismID()) < 10) {
            orgString = "0" + getOrganismID() + "P";
        }else {
            orgString = getOrganismID() + "P";
        }
        return orgString;
    }

    @Override
    public String[] getDataArray() {
        String id = super.getOrganismID();
        String type = "Plant";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }
}
