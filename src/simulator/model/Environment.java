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

    private static Organism newOrganismFromType(Config.OrganismType organismType) throws IllegalEnvironmentException {
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
        initOrganismFromTypeAndCount(Config.OrganismType.PLANT, plantCount);
        initOrganismFromTypeAndCount(Config.OrganismType.HERBIVORE, herbivoreCount);
        initOrganismFromTypeAndCount(Config.OrganismType.CARNIVORE, carnivoreCount);
        System.out.println("ENVIRONMENT: -> Init all organisms successful");
    }

    private void initOrganismFromTypeAndCount(Config.OrganismType type, int orgCount) throws IllegalEnvironmentException {
        int counter = 0;
        while(counter < orgCount) {
            Organism organism = newOrganismFromType(type);
            organisms.add(organism);
            domain.initOrganismRandomPlacement(organism.getId());
            counter ++;
        }
        System.out.println("ENVIRONMENT: -> Init " + orgCount + " " + type + "s successful");
    }

    public void progressEnvironmentByOrganism() throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException, EnvironmentCycleCompleteException, NoOrganismsLeftException {
        Organism organism = currentOrganism;
        if(organism == null) throw new NoOrganismsLeftException("No animal left");
        System.out.println("ENVIRONMENT: Progress " + organism.getType() + " ID " + organism.getId() + " **********");
        if(organism instanceof Plant) {
            progressPlant(organism);
        } else if (organism instanceof Animal) {
            progressAnimal((Animal) organism);
        } else {
            throw new IllegalEnvironmentException("Object not instance of any concrete organism");
        }
        System.out.println("ENVIRONMENT: Progress ID " + organism.getId() + " ended");
        switchNextOrganism();
    }

    private void switchNextOrganism() throws EnvironmentCycleCompleteException, NoOrganismsLeftException {
        System.out.println("ENVIRONMENT: Switching to next organism");
        if(organisms.isEmpty()) {
            currentOrganism = null;
            throw new NoOrganismsLeftException("No organisms left in environment");
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

    private void progressAnimal(Animal animal) throws InvalidIdentifierException, InvalidPositionException, IllegalEnvironmentException {
        animalMove(animal);
        animalAction(animal);
        if(animal.getEnergyLevel() < 1) {
            domain.removeOrganism(animal.getId());
            organisms.remove(animal);
        }
        animal.increaseAge();
        animal.decreaseEnergyLevel(1);
    }

    private void animalMove(Animal animal) throws InvalidIdentifierException, InvalidPositionException {
        //todo: for point of interest also use closest like with empty space magnitude calc
        //-> enviroment filter list of ids close to organism based on energy. Give domain smaller list and say move to closest one.
        System.out.println("ENVIRONMENT: Handling move " + animal.getType() + " " + animal.getId() + "...");
        List<Organism> orgMoveList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), animal.getSightRange()));
        Animal.Move move = animal.move(orgMoveList);
        System.out.println("ENVIRONMENT: Received " + move + " " + move.getIdList());
        switch(move) {
            case MOVE_TO:
                domain.moveInProxToClosestTarget(animal.getId(), move.getIdList(), animal.getMovementRange());
                break;
            case RUN_FROM:
                System.out.println("IMPLEMENT RUN FROM");
                break;
            case RANODM_MOVE:
                domain.moveOrgRandomInRange(animal.getId(), animal.getMovementRange());
                break;
            default:
                break;
        }
    }

    private void animalAction(Animal animal) throws InvalidIdentifierException, IllegalEnvironmentException, InvalidPositionException {
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
        animal.increaseEnergyLevel(orgToFeedOn.getEnergyLevel());
        domain.removeOrganism(orgToFeedOn.getId());
        organisms.remove(orgToFeedOn);
    }

    private void mateAnimal(Animal animal, Animal animalToMate) throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException {
        System.out.println("ENVIRONMENT: " + animal.getType() + " " + animal.getId() + " mate with " + animalToMate.getType() + " " + animalToMate.getId());
        if(animal.getEnergyLevel() >= animal.getEnergyToMate() && animalToMate.getEnergyLevel() >= animalToMate.getEnergyToMate()) {
            animal.decreaseEnergyLevel(animal.getEnergyMateCost());
            animalToMate.decreaseEnergyLevel(animalToMate.getEnergyMateCost());
            Organism child = newOrganismFromType(animal.getType());
            organisms.add(child);
            domain.initOrganismPlacementAtId(child.getId(), animal.getId());
        }
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
        Map<Config.OrganismType, Integer> orgCountMap = new HashMap<>();
        for(Organism organism : organisms) {
            int counter = orgCountMap.getOrDefault(organism.getType(), 0);
            orgCountMap.put(organism.getType(), counter + 1);
        }
        StringBuilder sb = new StringBuilder();
        for(Config.OrganismType type : orgCountMap.keySet()) {
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

    public String getCurrentOrgDataString() {
        return currentOrganism.toString();
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
