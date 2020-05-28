package simulator.model.organism;

import simulator.model.Config;
import simulator.model.Identification;

public abstract class Organism {

    private static int numberCounter = 1;
    private final Identification id;
    private int age = 0;
    private int energy;

    public Organism() {
        id = new Identification(String.valueOf(numberCounter), getType());
        numberCounter++;
        this.energy = getStartEnergy();
    }

    public Identification getID() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public int getEnergy() {
        return energy;
    }

    public void decreaseEnergy(int energyAmount) {
        if(energyAmount >= 0) {
            int newEnergy = energy - energyAmount;
            energy = Math.max(newEnergy, 0);
        } else {
            throw new IllegalArgumentException("Energy amount may not be negative");
        }
    }

    public void increaseEnergy(int energyAmount) {
        if(energyAmount >= 0) {
            int newEnergy = energy + energyAmount;
            energy = Math.min(newEnergy, getMaxEnergy());
        } else {
            throw new IllegalArgumentException("Energy amount may not be negative");
        }
    }

    public void increaseAge() {
        age++;
    }

    protected abstract Config.OrganismType getType();

    public abstract int getMaxEnergy();

    public abstract int getStartEnergy();

    public String toInfoString() {
        StringBuilder orgString = new StringBuilder();
        orgString.append(id.toString());
        //orgString.append("\n");
        orgString.append(" Age "+ getAge());
        orgString.append(" Energy " + getEnergy());
        return orgString.toString();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
