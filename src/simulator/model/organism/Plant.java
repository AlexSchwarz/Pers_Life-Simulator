package simulator.model.organism;

public class Plant extends Organism {

    public Plant() {
        super(OrganismType.PLANT);
    }

    @Override
    public String toString() {
        String orgString;
        if(Integer.parseInt(getId()) < 10) {
            orgString = "0" + getId() + "P";
        }else {
            orgString = getId() + "P";
        }
        return orgString;
    }

    @Override
    public String[] getDataArray() {
        String id = super.getId();
        String type = "Plant";
        String position = "NO_POSITION";
        String[] dataArray = {id, type, position};
        return dataArray;
    }
}
