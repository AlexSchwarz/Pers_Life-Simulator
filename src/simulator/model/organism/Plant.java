package simulator.model.organism;

import simulator.model.Config;

public class Plant extends Organism {

    public Plant() {
        super();
    }

    @Override
    public Config.OrganismType getType() {
        return Config.OrganismType.PLANT;
    }

    @Override
    public int getMaxEnergy() {
        return Config.PLANT_MAX_ENERGY;
    }

    @Override
    public int getStartEnergy() {
        return Config.PLANT_START_ENERGY;
    }

    @Override
    public int getEnergy() {
        return Config.PLANT_MAX_ENERGY;
    }
}
