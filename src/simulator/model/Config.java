package simulator.model;

public final class Config {
    public static final int SIZE = 30;
    public static final int MAX_RUN_PROGRESSIONS = 100;
    public static final int PLANT_COUNT = 15;
    public static final int CARNIVORE_COUNT = 3;
    public static final int HERBIVORE_COUNT = 5;
    public static final int PLANT_MAX_ENERGYLEVEL = 10;
    public static final int CARNIVORE_SIGHT = 15;
    public static final int CARNIVORE_MOVEMENT = 3;
    public static final int CARNIVORE_MAX_ENERGYLEVEL = 20;
    public static final int CARNIVORE_START_ENERGYLEVEL = 10;
    public static final int CARNIVORE_ENERGY_TO_MATE = 15;
    public static final int CARNIVORE_ENERGY_MATE_COST = 10;
    public static final int HERBIVORE_SIGHT = 15;
    public static final int HERBIVORE_MOVEMENT = 5;
    public static final int HERBIVORE_MAX_ENERTGYLEVEL = 20;
    public static final int HERBIVORE_START_ENERGYLEVEL = 10;
    public static final int HERBIVORE_ENERGY_TO_MATE = 10;
    public static final int HERVIBORE_ENERGY_MATE_COST = 5;

    public enum OrganismType {
        PLANT, HERBIVORE, CARNIVORE;
    }
}
