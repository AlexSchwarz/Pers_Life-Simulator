package simulator.model;

import simulator.model.exceptions.*;
import simulator.model.organism.*;

import java.util.*;

public class Environment {

    private static final int INTERACTION_RANGE = 1;
    private List<Organism> organisms = new ArrayList<>();
    private Organism currentOrganism;
    private Domain domain;

    public Environment(int size, int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException {
        validateOrganismCount(plantCount, herbivoreCount, carnivoreCount, size);
        domain = new Domain(size);
        initOrganismsStart(plantCount, herbivoreCount, carnivoreCount);
        currentOrganism = organisms.get(0);
    }

    private void validateOrganismCount(int orgCount1, int orgCount2, int orgCount3, int size) throws IllegalEnvironmentException {
        if(orgCount1 < 0 || orgCount2 < 0 || orgCount3 < 0 || orgCount1+orgCount2+orgCount3 > size*size) {
            throw new IllegalEnvironmentException("Invalid organism count: " + orgCount1 + " " + orgCount2 + " " + orgCount3);
        }
    }

    private static Organism newOrganismFromType(Organism.OrganismType organismType) throws IllegalEnvironmentException {
        Objects.requireNonNull(organismType);
        Organism organism;
        switch(organismType) {
            case PLANT:
                organism = new Plant();
                break;
            case HERBIVORE:
                organism = new Herbivore();
                break;
            case CARNIVORE:
                organism = new Carnivore();
                break;
            default:
                throw new IllegalEnvironmentException("Not a valid type");
        }
        Objects.requireNonNull(organism);
        return organism;
    }

    private void initOrganismsStart(int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException {
        System.out.println("ENVIRONMENT: Attempting init all organisms...");
        initOrganismFromTypeAndCount(Organism.OrganismType.PLANT, plantCount);
        initOrganismFromTypeAndCount(Organism.OrganismType.HERBIVORE, herbivoreCount);
        initOrganismFromTypeAndCount(Organism.OrganismType.CARNIVORE, carnivoreCount);
        System.out.println("ENVIRONMENT: -> Init all organisms successful");
    }

    private void initOrganismFromTypeAndCount(Organism.OrganismType type, int orgCount) throws IllegalEnvironmentException {
        int counter = 0;
        while(counter < orgCount) {
            Organism organism = newOrganismFromType(type);
            organisms.add(organism);
            domain.initOrganismPlacement(organism.getId());
            counter ++;
        }
        System.out.println("ENVIRONMENT: -> Init " + orgCount + " " + type + "s successful");
    }

    public void progressEnvironmentByOrganism() throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException, EnvironmentCycleCompleteException {
        Organism organism = currentOrganism;
        Objects.requireNonNull(organism);
        System.out.println("ENVIRONMENT: Progress " + organism.getType() + " ID " + organism.getId() + " **********");
        if(organism instanceof Plant) {
            progressPlant(organism);
        } else if (organism instanceof Animal) {
            animalMove((Animal) organism);
            animalAction((Animal) organism);
        } else {
            throw new IllegalEnvironmentException("Object not instance of any concrete organism");
        }
        //todo: REMOVE 1 ENERGY
        System.out.println("ENVIRONMENT: Progress ID " + organism.getId() + " ended");
        switchNextOrganism();
    }

    private void switchNextOrganism() throws EnvironmentCycleCompleteException, IllegalEnvironmentException {
        System.out.println("ENVIRONMENT: Switching to next organism");
        if(organisms.isEmpty()) {
            throw new IllegalEnvironmentException("No organisms left in environment");
        } else {
            try {
                currentOrganism = organisms.get(organisms.indexOf(currentOrganism) + 1);
            } catch (IndexOutOfBoundsException e) {
                currentOrganism = organisms.get(0);
                throw new EnvironmentCycleCompleteException("Full organism cycle completed");
            }
        }
    }

    private void progressPlant(Organism organism) {

    }

    private void animalMove(Animal animal) throws InvalidIdentifierException, InvalidPositionException {
        //todo: for point of interest also use closest like with empty space magnitude calc
        //-> enviroment filter list of ids close to organism based on energy. Give domain smaller list and say move to closest one.
        System.out.println("ENVIRONMENT: Handling move " + animal.getType() + " " + animal.getId() + "...");
        List<Organism> orgMoveList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), animal.getSightRange()));
        Animal.Action move = animal.move(orgMoveList);
        System.out.println("ENVIRONMENT: Received " + move + " " + move.getId());
        switch(move) {
            case MOVE_TO:
                domain.moveInProxToTarget(animal.getId(), move.getId(), animal.getMovementRange());
                break;
            case RUN_FROM:
                break;
            case NO_ACTION:
                domain.moveOrgRandomInRange(animal.getId(), animal.getMovementRange());
                break;
            default:
                break;
        }
    }

    private void animalAction(Animal animal) throws InvalidIdentifierException {
        System.out.println("ENVIRONMENT: Handling interaction " + animal.getType() + " " + animal.getId() + "...");
        List<Organism> orgMoveList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), INTERACTION_RANGE));
        Animal.Action intaction = animal.interact(orgMoveList);
        System.out.println("ENVIRONMENT: Received " + intaction + " " + intaction.getId());
        switch(intaction) {
            case FEED_ON:
                feedAnimal(animal, getOrganismFromId(intaction.getId()));
                break;
            case MATE_WITH:
                mateAnimal(animal, (Animal) getOrganismFromId(intaction.getId()));
                break;
            case NO_ACTION:
                System.out.println("ENVIRONMENT: No interaction");
                break;
            default:
                break;
        }
    }

    private List<Organism> convertToOrgList(List<String> idList) throws InvalidIdentifierException {
        List<Organism> orgList = new ArrayList<>();
        for(String id : idList) {
            orgList.add(getOrganismFromId(id));
        }
        return orgList;
    }

    private void feedAnimal(Animal animal, Organism orgToFeedOn) throws InvalidIdentifierException {
        System.out.println("ENVIRONMENT: " + animal.getType() + " " + animal.getId() + " feeding on " + orgToFeedOn.getType() + " " + orgToFeedOn.getId());
        animal.increaseEnergyLevel(5);
        domain.removeOrganism(orgToFeedOn.getId());
        organisms.remove(orgToFeedOn);
    }

    private void mateAnimal(Animal animal, Animal animalToMate) {
        //todo: CHECK BOTH ENOUGH ENERGY
        System.out.println("ENVIRONMENT: " + animal.getType() + " " + animal.getId() + " mate with " + animalToMate.getType() + " " + animalToMate.getId());
        animal.decreaseEnergyLevel(5);
        animalToMate.decreaseEnergyLevel(5);
    }

    private Organism getOrganismFromId(String identifier) throws InvalidIdentifierException {
        Organism foundOrganism = null;
        boolean searching = true;
        for(Organism organism : organisms) {
            if(organism.getId().equals(identifier)) {
                foundOrganism = organism;
                searching = false;
            }
        }
        if(searching) {
            throw new InvalidIdentifierException("No Domain ID matches " + identifier);
        }
        Objects.requireNonNull(foundOrganism);
        return foundOrganism;
    }

    public String getEnvironmentDataString() {
        Map<Organism.OrganismType, Integer> orgCountMap = new HashMap<>();
        for(Organism organism : organisms) {
            int counter = orgCountMap.getOrDefault(organism.getType(), 0);
            orgCountMap.put(organism.getType(), counter + 1);
        }
        StringBuilder sb = new StringBuilder();
        for(Organism.OrganismType type : orgCountMap.keySet()) {
            sb.append(type).append(";").append(orgCountMap.get(type)).append(";");
        }
        return sb.toString();
    }

    public String getGridDataString(PositionVector pos) throws InvalidPositionException {
        String dataString;
        try{
            dataString = getOrganismFromId(domain.getContentFromPosition(pos)).toString();
        } catch (InvalidIdentifierException e) {
            dataString = "--";
        }
        return  dataString;
    }

    private void registerOrganism(Organism organism, PositionVector position) {
        System.out.println("FILE: Attempting register...");
        String csvDataString = convertOrgToCSV(organism, position);
        System.out.println("FILE: -> Register successful");
    }

    public String convertOrgToCSV(Organism organism, PositionVector positionVector) {
        return String.format("%s;%s;%s", organism.getId(), organism.getType(), positionVector.toString());
    }

    public void printDomain() {
        domain.printDomain();
    }
}
