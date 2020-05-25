package simulator.model.organism;

import java.util.ArrayList;
import java.util.List;

public abstract class Animal extends Organism {

    private int age = 0;
    private final int sightRange;
    private final int movementRange;
    private int energyLevel;
    private int maxEnergyLevel;

    public Animal(OrganismType type, int sightRange, int movementRange, int maxEnergyLevel, int energyLevel) {
        super(type);
        this.sightRange = sightRange;
        this.movementRange = movementRange;
        this.maxEnergyLevel = maxEnergyLevel;
        this.energyLevel = energyLevel;
    }

    public enum Action {
        FEED_ON, MATE_WITH, NO_ACTION;;

        String id = "-1";

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public enum Move {
        MOVE_TO, RUN_FROM, RANODM_MOVE;

        List<String> idList = new ArrayList<>();

        public void resetList() {
            idList = new ArrayList<>();
        }

        public void addId(String id) {
            idList.add(id);
        }

        public List<String> getIdList() {
            return idList;
        }
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

    public abstract Action interact(List<Organism> orgsInActionProx);

    public abstract Move move(List<Organism> orgsInSightProx);
}
