package simulator.model;

import simulator.model.exceptions.*;
import simulator.model.organism.*;

import java.util.*;

public class Environment {

    private List<Organism> organismList = new ArrayList<>();
    private String currentOrganismId;
    private DomainWritable domain;

    public Environment(int size, int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException, SimulatorErrorException {
        validateParameters(plantCount, herbivoreCount, carnivoreCount, size);
        domain = new DomainWritable(size);
        initOrganismsStart(plantCount, herbivoreCount, carnivoreCount);
        currentOrganismId = organismList.get(0).getId();
    }

    private void validateParameters(int orgCount1, int orgCount2, int orgCount3, int size) throws IllegalEnvironmentException {
        int totalOrganisms = orgCount1+orgCount2+orgCount3;
        if(orgCount1 < 0 || orgCount2 < 0 || orgCount3 < 0 || totalOrganisms > size*size || totalOrganisms < 1) {
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
            organismList.add(organism);
            //domain.initOrganismRandomPlacement(organism.getId());
            counter ++;
        }
        System.out.println("ENVIRONMENT: -> Init " + orgCount + " " + type + "s successful");
    }

    /*
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
        organism.increaseAge();
        System.out.println("ENVIRONMENT: Progress ID " + organism.getId() + " ended");
        switchNextOrganism();
    }

     */

    private Organism getOrganismFromId(String identifier) throws InvalidIdentifierException {
        Organism foundOrganism = null;
        Iterator<Organism> it = organismList.iterator();
        boolean searching = true;
        while(searching && it.hasNext()) {
            Organism organism = it.next();
            if(organism.getId().equals(identifier)) {
                foundOrganism = organism;
            }
        }
        if(searching) {
            throw new InvalidIdentifierException("No Environment organism ID matches " + identifier);
        }
        Objects.requireNonNull(foundOrganism);
        return foundOrganism;
    }

    /*
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
     */

    private void progressPlant(Organism organism) {

    }

    private void progressAnimal(Animal animal) throws InvalidIdentifierException, InvalidPositionException, IllegalEnvironmentException {

    }

    /*
    private void animalMove(Animal animal) throws InvalidIdentifierException, InvalidPositionException {
        //todo: for point of interest also use closest like with empty space magnitude calc
        //-> enviroment filter list of ids close to organism based on energy. Give domain smaller list and say move to closest one.
        System.out.println("ENVIRONMENT: Handling move " + animal.getType() + " " + animal.getId() + "...");
        List<Organism> orgMoveList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), animal.getSightRange()));
        Config.Move move = animal.move(orgMoveList);
        System.out.println("ENVIRONMENT: Received " + move + " " + move.getIdList());
        switch(move) {
            case MOVE_TO:
                domain.moveToClosestEmptyPositionToClosestIdInMovementRange(animal.getId(), move.getIdList(), animal.getMovementRange());
                break;
            case RUN_FROM:
                System.out.println("IMPLEMENT RUN FROM");
                break;
            case RANDOM_MOVE:
                domain.moveOrgRandomInRange(animal.getId(), animal.getMovementRange());
                break;
            default:
                break;
        }
    }

     */

    /*
    private void animalAction(Animal animal) throws InvalidIdentifierException, IllegalEnvironmentException, InvalidPositionException {
        System.out.println("ENVIRONMENT: Handling interaction " + animal.getType() + " " + animal.getId() + "...");
        List<Organism> orgMoveList = convertToOrgList(domain.getAllIDsInProximity(animal.getId(), INTERACTION_RANGE));
        Config.Action intaction = animal.interact(orgMoveList);
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

     */

    private void feedAnimal(Animal animal, Organism orgToFeedOn) throws InvalidIdentifierException {
        System.out.println("ENVIRONMENT: " + animal.getType() + " " + animal.getId() + " feeding on " + orgToFeedOn.getType() + " " + orgToFeedOn.getId());
        animal.increaseEnergyLevel(orgToFeedOn.getEnergyLevel());
        domain.removeID(orgToFeedOn.getId());
        organismList.remove(orgToFeedOn);
    }

    /*
    private void mateAnimal(Animal animal, Animal animalToMate) throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException {
        System.out.println("ENVIRONMENT: " + animal.getType() + " " + animal.getId() + " mate with " + animalToMate.getType() + " " + animalToMate.getId());
        if(animal.getEnergyLevel() >= animal.getEnergyToMate() && animalToMate.getEnergyLevel() >= animalToMate.getEnergyToMate()) {
            animal.decreaseEnergyLevel(animal.getEnergyMateCost());
            animalToMate.decreaseEnergyLevel(animalToMate.getEnergyMateCost());
            Organism child = newOrganismFromType(animal.getType());
            organisms.add(child);
            domain.initIDPlacementAtId(child.getId(), animal.getId());
        }
    }

     */

    /*
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

     */

    /*
    public String getGridDataString(PositionVector pos) throws InvalidPositionException {
        String dataString;
        try{
            dataString = getOrganismFromId(domain.getContentAtPosition(pos)).toString();
        } catch (InvalidIdentifierException e) {
            dataString = "--";
        }
        return  dataString;
    }
    public void printDomain() {
        domain.printDomain();
    }

     */
}
