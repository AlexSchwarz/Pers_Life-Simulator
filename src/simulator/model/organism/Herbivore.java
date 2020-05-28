package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;

public class Herbivore extends Animal {

    public Herbivore() {
        super();
    }

    @Override
    public Config.ActionType interact(DomainReadable domain) {
        return null;
    }

    @Override
    public Config.MoveType move(DomainReadable domain) {
        return null;
    }

    @Override
    public int getSightRange() {
        return Config.HERBIVORE_SIGHT;
    }

    @Override
    public int getMovementRange() {
        return Config.HERBIVORE_MOVEMENT;
    }

    @Override
    public int getMaxEnergyLevel() {
        return Config.HERBIVORE_MAX_ENERGY;
    }

    @Override
    public int getStartEnergy() {
        return Config.HERBIVORE_START_ENERGY;
    }

    @Override
    public int getEnergyToMate() {
        return Config.HERBIVORE_ENERGY_TO_MATE;
    }

    @Override
    public int getEnergyMateCost() {
        return Config.HERBIVORE_ENERGY_MATE_COST;
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.HERBIVORE;
    }
}
