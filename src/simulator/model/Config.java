package simulator.model;

import java.util.ArrayList;
import java.util.List;

public final class Config {
    public static final int SIZE = 30;
    public static final String BLANK = "---";
    public static final String DELIMINATOR = ";";
    public static final int MAX_RUN_PROGRESSIONS = 100;
    public static final int INTERACTION_RANGE = 1;

    public static final int PLANT_COUNT = 15;
    public static final int CARNIVORE_COUNT = 3;
    public static final int HERBIVORE_COUNT = 5;

    public static final int PLANT_START_ENERGY = 10;
    public static final int PLANT_MAX_ENERGY = 10;

    public static final int CARNIVORE_SIGHT = 15;
    public static final int CARNIVORE_MOVEMENT = 3;
    public static final int CARNIVORE_MAX_ENERGY = 20;
    public static final int CARNIVORE_START_ENERGY = 10;
    public static final int CARNIVORE_ENERGY_TO_MATE = 15;
    public static final int CARNIVORE_ENERGY_MATE_COST = 10;

    public static final int HERBIVORE_SIGHT = 15;
    public static final int HERBIVORE_MOVEMENT = 5;
    public static final int HERBIVORE_MAX_ENERGY = 20;
    public static final int HERBIVORE_START_ENERGY = 10;
    public static final int HERBIVORE_ENERGY_TO_MATE = 10;
    public static final int HERBIVORE_ENERGY_MATE_COST = 5;

    public enum OrganismType {
        PLANT('P'), HERBIVORE('H'), CARNIVORE('C');

        private char symbol;

        OrganismType(char symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }
    }

    public enum ActionType {
        FEED_ON, MATE_WITH, NO_ACTION;;

        PositionVector position;

        public void setPosition(PositionVector position) {
            this.position = position;
        }

        public PositionVector getPosition() {
            return position;
        }
    }

    public enum MoveType {
        MOVE_TO, RUN_FROM, NO_MOVE;

        PositionVector position;

        public void setPosition(PositionVector position) {
            this.position = position;
        }

        public PositionVector getPosition() {
            return position;
        }
    }
}
