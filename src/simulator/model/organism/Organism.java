package simulator.model.organism;

import simulator.model.Config;

public abstract class Organism {

    private static int identificationCounter = 1;
    private final String organismID;
    private int age = 0;
    private int energyLevel;

    public Organism() {
        organismID = String.valueOf(identificationCounter) + Config.DELIMINATOR + getType().getSymbol();
        identificationCounter++;
        this.energyLevel = getStartEnergy();
    }

    public void decreaseEnergyLevel(int energyAmount) {
        if(energyAmount >= 0) {
            int newEnergy = energyLevel - energyAmount;
            energyLevel = Math.max(newEnergy, 0);
        } else {
            throw new IllegalArgumentException("Energy amount may not be negative");
        }
    }

    public void increaseEnergyLevel(int energyAmount) {
        if(energyAmount >= 0) {
            int newEnergy = energyLevel + energyAmount;
            energyLevel = Math.min(newEnergy, getMaxEnergyLevel());
        } else {
            throw new IllegalArgumentException("Energy amount may not be negative");
        }
    }

    public int getAge() {
        return age;
    }

    public void increaseAge() {
        age++;
    }

    public String getId() {
        return organismID;
    }

    public int getEnergyLevel() {
        return energyLevel;
    }

    public abstract Config.OrganismType getType();

    public abstract int getMaxEnergyLevel();

    public abstract int getStartEnergy();

    @Override
    public String toString() {
        StringBuilder orgString = new StringBuilder();
        orgString.append(getId());
        orgString.append("\n");
        orgString.append("A"+ getAge());
        orgString.append("E" + getEnergyLevel());
        return orgString.toString();
    }
}
