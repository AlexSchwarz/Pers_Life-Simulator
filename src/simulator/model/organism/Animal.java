package simulator.model.organism;

public abstract class Animal extends Organism {

    private int age = 0;
    private final int sightRange;
    private final int movementRange;

    public Animal(OrganismType type, int sightRange, int movementRange) {
        super(type);
        this.sightRange = sightRange;
        this.movementRange = movementRange;
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
