package simulator.model.organism;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal extends Organism {

    private int age = 0;
    private final int sightRange;
    private final int movementRange;
    private int energyLevel;

    public Animal(OrganismType type, int sightRange, int movementRange, int energyLevel) {
        super(type);
        this.sightRange = sightRange;
        this.movementRange = movementRange;
        this.energyLevel = energyLevel;
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
}
