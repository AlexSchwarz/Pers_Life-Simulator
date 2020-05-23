package model.organism;

public class Herbivore extends Animal {

    public Herbivore() {
        super(OrganismType.HERBIVORE);
    }

    @Override
    public String toString() {
        String orgString;
        if(Integer.parseInt(getOrganismID()) < 10) {
            orgString = "0" + getOrganismID() + "H";
        }else {
            orgString = getOrganismID() + "H";
        }
        return orgString;
    }

    @Override
    public String[] getDataArray() {
        String id = super.getOrganismID();
        String type = "Herbivore";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }
}
