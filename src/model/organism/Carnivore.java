package model.organism;

public class Carnivore extends Animal {

    public Carnivore() {
        super();
    }

    @Override
    public String toString() {
        String orgString;
        if(Integer.parseInt(getOrganismID()) < 10) {
            orgString = "0" + getOrganismID() + "C";
        }else {
            orgString = getOrganismID() + "C";
        }
        return orgString;
    }

    @Override
    public String getType() {
        return "Carnivore";
    }

    @Override
    public String[] getDataArray() {
        String id = super.getOrganismID();
        String type = "Carnivore";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }
}
