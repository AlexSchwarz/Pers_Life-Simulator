package model.organism;

public abstract class Animal extends Organism {

    private int age = 0;

    public Animal(OrganismType type) {
        super(type);
    }

    public int getAge() {
        return age;
    }

    public void increaseAge() {
        age++;
    }
}
