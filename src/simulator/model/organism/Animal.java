package simulator.model.organism;

import simulator.model.exceptions.NoAnimalActionException;

import java.util.List;

public abstract class Animal extends Organism {

    private int age = 0;
    private final int sightRange;
    private final int movementRange;
    private int energyLevel;
    private int maxEnergyLevel;

    public Animal(OrganismType type, int sightRange, int movementRange, int energyLevel) {
        super(type);
        this.sightRange = sightRange;
        this.movementRange = movementRange;
        this.maxEnergyLevel = energyLevel;
        this.energyLevel = maxEnergyLevel;
    }

    public int getAge() {
        return age;
    }

    public void increaseAge() {
        age++;
    }

    public int getSightRange() {
        return sightRange;
    }

    public int getMovementRange() {
        return movementRange;
    }

    public int getEnergyLevel() {
        return energyLevel;
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
            energyLevel = Math.min(newEnergy, maxEnergyLevel);
        } else {
            throw new IllegalArgumentException("Energy amount may not be negative");
        }
    }

    public abstract Organism feed(List<Organism> orgsInActionProx) throws NoAnimalActionException;

    public abstract Animal mate(List<Organism> orgsInActionProx) throws NoAnimalActionException;

    public abstract String move(List<Organism> orgsInSightProx) throws NoAnimalActionException;
}
