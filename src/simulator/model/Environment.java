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
        System.out.println("ENVIRONMENT: Attempting init " + orgCount + " " + type);
        int counter = 0;
        while(counter < orgCount) {
            Organism organism = newOrganismFromType(type);
            organisms.add(organism);
            domain.initOrganismPlacement(organism.getId());
            counter ++;
        }
        System.out.println("ENVIRONMENT: -> Init " + orgCount + " " + type + " successful");
    }

    public void progressEnvironmentByOrganism() throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException, EnvironmentCycleCompleteException {
        Organism organism = currentOrganism;
        Objects.requireNonNull(organism);
        System.out.println("ENVIRONMENT: Progress ID " + organism.getId());
        if(organism instanceof Plant) {
            progressPlant(organism);
        } else if (organism instanceof Animal) {
            animalMove((Animal) organism);
            animalAction((Animal) organism);
        } else {
            throw new IllegalEnvironmentException("Object not instance of any concrete organism");
        }
        switchNextOrganism();
    }

    private void switchNextOrganism() throws EnvironmentCycleCompleteException, IllegalEnvironmentException {
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
        System.out.println("ENVIRONMENT: Attempting progression " + organism.getType() + " ID " + organism.getId());
        System.out.println("ENVIRONMENT: Progression organism ended");
    }

    private void animalMove(Animal animal) throws InvalidIdentifierException, InvalidPositionException {
        //todo: for point of interest also use closest like with empty space magnitude calc
        //-> enviroment filter list of ids close to organism based on energy. Give domain smaller list and say move to closest one.
        List<Organism> orgMoveList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), animal.getSightRange()));
        try {
            System.out.println("ENVIRONMENT: Attempting move " + animal.getType() + " ID " + animal.getId());
            domain.moveInProxToTarget(animal.getId(), animal.move(orgMoveList), animal.getMovementRange());
        } catch (NoAnimalActionException e) {
            System.out.println("ENVIRONMENT: Found not point of interest. Random Move");
            domain.moveOrgRandomInRange(animal.getId(), animal.getMovementRange());
        }
    }

    private void animalAction(Animal animal) throws InvalidIdentifierException {
        System.out.println("ENVIRONMENT: Attempting action " + animal.getType() + " ID " + animal.getId());
        List<Organism> orgActionList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), INTERACTION_RANGE));
        try {
            Organism orgToFeedOn = animal.feed(orgActionList);
            feedAnimal(animal, orgToFeedOn);
        } catch (NoAnimalActionException e) {
            System.out.println("ENVIRONMENT: No action");
        }
        try {
            Animal orgToMate = animal.mate(orgActionList);
            mateAnimal(animal, orgToMate);
        } catch (NoAnimalActionException e) {
            System.out.println("ENVIRONMENT: No action");
        }
    }

    private List<Organism> convertToOrgList(List<String> idList) throws InvalidIdentifierException {
        List<Organism> orgList = new ArrayList<>();
        for(String id : idList) {
            orgList.add(getOrganismFromId(id));
        }
        return orgList;
    }

    private void feedAnimal(Animal animal, Organism orgToFeedOn) {
        System.out.println("FEED");
    }

    private void mateAnimal(Animal animal, Animal animalToMate) {
        System.out.println("MATE");
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
