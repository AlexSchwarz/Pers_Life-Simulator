package simulator.model.organism;

public abstract class Organism {

    private static int identificationCounter = 1;
    private final String organismID;
    private final OrganismType type;

    public Organism(OrganismType type) {
        organismID = String.valueOf(identificationCounter);
        identificationCounter++;
        this.type = type;
    }

    public String getId() {
        return organismID;
    }

    public abstract String toString();

    public OrganismType getType() {
        return type;
    }
    public abstract String[] getDataArray();

    public enum OrganismType {
        PLANT, HERBIVORE, CARNIVORE;
    }
}
