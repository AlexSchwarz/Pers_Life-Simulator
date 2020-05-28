package simulator.model.organism;

import simulator.model.Config;
import simulator.model.DomainReadable;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;

public abstract class Animal extends Organism {

    public Animal() {
        super();
    }

    public abstract Config.ActionType interact(DomainReadable domain);

    public abstract Config.MoveType move(DomainReadable domain);

    public abstract int getSightRange();

    public abstract int getMovementRange();

    public abstract int getEnergyToMate();

    public abstract int getEnergyMateCost();

    public abstract Config.OrganismType getType();
}
