package model.organism;

public abstract class Organism {

    private static int identificationCounter = 1;
    private final String organismID;

    public Organism() {
        organismID = String.valueOf(identificationCounter);
        identificationCounter++;
    }

    public String getOrganismID() {
        return organismID;
    }

    public abstract String toString();

    public abstract String getType();

    public abstract String[] getDataArray();
}
