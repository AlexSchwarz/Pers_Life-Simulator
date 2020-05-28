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
    public int getMaxEnergyLevel() {
        return Config.PLANT_MAX_ENERGY;
    }

    @Override
    public int getStartEnergy() {
        return Config.PLANT_START_ENERGY;
    }

    @Override
    public int getEnergyLevel() {
        return Config.PLANT_MAX_ENERGY;
    }
}
