package simulator.model.organism;

import simulator.model.Config;

public abstract class Organism {

    private static int identificationCounter = 1;
    private final String organismID;

    public Organism() {
        organismID = String.valueOf(identificationCounter);
        identificationCounter++;
    }

    public String getId() {
        return organismID;
    }

    public abstract String toString();

    public abstract Config.OrganismType getType();

    public abstract String[] getDataArray();

    public abstract int getEnergyLevel();

}
