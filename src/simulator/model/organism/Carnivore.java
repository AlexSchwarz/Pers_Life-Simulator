package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;

public class Carnivore extends Animal {

    public Carnivore() {
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
        return Config.CARNIVORE_SIGHT;
    }

    @Override
    public int getMovementRange() {
        return Config.CARNIVORE_MOVEMENT;
    }

    @Override
    public int getMaxEnergyLevel() {
        return Config.CARNIVORE_MAX_ENERGY;
    }

    @Override
    public int getStartEnergy() {
        return Config.CARNIVORE_START_ENERGY;
    }

    @Override
    public int getEnergyToMate() {
        return Config.CARNIVORE_ENERGY_TO_MATE;
    }

    @Override
    public int getEnergyMateCost() {
        return Config.CARNIVORE_ENERGY_MATE_COST;
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.CARNIVORE;
    }

}
