package simulator.model;

import simulator.model.exceptions.*;
import simulator.model.organism.*;

import java.util.*;

public class Environment {

    private List<Organism> organismList = new ArrayList<>();
    private DomainWritable domain;
    private Identification currentOrganismID;
    private int dayCount = 0;

    public Environment(int size, int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException, SimulatorErrorException {
        validateParameters(plantCount, herbivoreCount, carnivoreCount, size);
        domain = new DomainWritable(size);
        initOrganismsStart(plantCount, herbivoreCount, carnivoreCount);
        currentOrganismID = organismList.get(0).getID();
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

    public int getDayCount() {
        return dayCount;
    }

    //TESTING ONLY
    public DomainWritable getDomain() {
        return domain;
    }

    public void printDomain() {
        System.out.println(domain.toString());
    }

    private void initOrganismsStart(int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException {
        //System.out.println("ENVIRONMENT: Attempting init all organisms...");
        initOrganismFromTypeAndCount(Config.OrganismType.PLANT, plantCount);
        initOrganismFromTypeAndCount(Config.OrganismType.HERBIVORE, herbivoreCount);
        initOrganismFromTypeAndCount(Config.OrganismType.CARNIVORE, carnivoreCount);
        //System.out.println("ENVIRONMENT: -> Init all organisms successful");
    }

    private void initOrganismFromTypeAndCount(Config.OrganismType type, int orgCount) throws IllegalEnvironmentException {
        int counter = 0;
        while(counter < orgCount) {
            Organism organism = newOrganismFromType(type);
            organismList.add(organism);
            domain.initIDRandomPlacement(organism.getID());
            counter ++;
        }
        //System.out.println("ENVIRONMENT: -> Init " + orgCount + " " + type + "s successful");
    }


    public void progressCurrentOrganism() throws InvalidIdentifierException, SimulatorErrorException, InvalidPositionException, IllegalEnvironmentException {
        Organism organism = getOrganismFromID(currentOrganismID);
        //System.out.println("ENVIRONMENT: Progressing organism " + organism.toInfoString() + ", day " + dayCount);
        if(organism instanceof Plant) {
            progressPlant((Plant) organism);
        }else if(organism instanceof Animal) {
            progressAnimal((Animal) organism);
        }
        organism.increaseAge();
        switchNextOrganism();
    }

    private Organism getOrganismFromID(Identification id) throws InvalidIdentifierException {
        Organism foundOrganism = null;
        Iterator<Organism> it = organismList.iterator();
        boolean searching = true;
        while(searching && it.hasNext()) {
            Organism organism = it.next();
            if(organism.getID().equals(id)) {
                foundOrganism = organism;
                searching = false;
            }
        }
        if(searching) {
            throw new InvalidIdentifierException("No Environment organism number matches " + id);
        }
        Objects.requireNonNull(foundOrganism);
        return foundOrganism;
    }


    private void switchNextOrganism() throws SimulatorErrorException, IllegalEnvironmentException {
        //System.out.println("ENVIRONMENT: Switching to next ID from " + currentOrganismID);
        if(organismList.isEmpty()) {
            currentOrganismID = null;
            throw new SimulatorErrorException("No orgs left");
        }
        //System.out.println("ENVIRONMENT: Searching through " + organismList);
        Iterator<Organism> it = organismList.iterator();
        boolean searching = true;
        while(searching && it.hasNext()) {
            Organism organism = it.next();
            if(Integer.parseInt(currentOrganismID.getNumber()) < Integer.parseInt(organism.getID().getNumber())) {
                currentOrganismID = organism.getID();
                //System.out.println("ENVIRONMENT: Found higher number " + currentOrganismID);
                searching = false;
            }
        }
        if(searching) {
            currentOrganismID = organismList.get(0).getID();
            //System.out.println("ENVIRONMENT: No higher ID found, cycle to first organism " + currentOrganismID);
            cycleDay();
        }
    }

    private void cycleDay() throws IllegalEnvironmentException {
        //todo: Plant count should be fixed amount -> make sure every day that there are a certain number of plants (dont just add 3 every time)
        dayCount++;
        //System.out.println("ENVIRONMENT: Increase day count to " + dayCount);
        //System.out.println("ENVIRONMENT: Init daily plants");
        initOrganismFromTypeAndCount(Config.OrganismType.PLANT, Config.DAILY_PLANT_RESTOCK);
    }

    private void progressPlant(Plant plant) {
        //System.out.println("PLACE HOLDER PLANT PROGRESS");
    }

    private void progressAnimal(Animal animal) throws InvalidIdentifierException, InvalidPositionException, IllegalEnvironmentException {
        PositionVector position = domain.getPositionOfID(animal.getID());
        Config.MoveType move = animal.move(position, domain);
        //System.out.println("ENVIRONMENT: Received " + move);
        if (move == Config.MoveType.MOVE_TO) {
            domain.moveID(animal.getID(), move.getPosition());
            position = move.getPosition();
        } else {
            //System.out.println("ENVIRONMENT: No move");
        }
        Config.ActionType action = animal.interact(position, domain);
        //System.out.println("ENVIRONMENT: Received " + action);
        if (action == Config.ActionType.MATE_WITH) {
            mateAction(animal,position, (Animal) getOrganismFromID(domain.getIDAtPosition(action.getPosition())));
        } else if (action == Config.ActionType.FEED_ON) {
            feedAction(animal, getOrganismFromID(domain.getIDAtPosition(action.getPosition())));
        } else {
            //System.out.println("ENVIRONMENT: No action");
        }
        animal.decreaseEnergy(Config.DAILY_ENERGY_LOSS);
        if(animal.getEnergy() == 0) {
            deleteOrganism(animal);
        }
    }

    private void mateAction(Animal actorAnimal, PositionVector positionActor, Animal targetAnimal) throws IllegalEnvironmentException, InvalidPositionException {
        if(actorAnimal.canMate() && targetAnimal.canMate()) {
            actorAnimal.decreaseEnergy(actorAnimal.getEnergyMateCost());
            targetAnimal.decreaseEnergy(targetAnimal.getEnergyMateCost());
            Organism child = newOrganismFromType(actorAnimal.getID().getType());
            organismList.add(child);
            domain.setIDAtPosition(child.getID(), actorAnimal.getFirstOccurrenceOfTypeFromSpaceList(domain.getSpacesInProximity(positionActor, 2), Config.OrganismType.VOID));
        }
    }

    private void feedAction(Animal actorAnimal, Organism targetOrganism) throws InvalidIdentifierException {
        actorAnimal.increaseEnergy(targetOrganism.getEnergy());
        deleteOrganism(targetOrganism);
    }

    private void deleteOrganism(Organism organism) throws InvalidIdentifierException {
        domain.removeID(organism.getID());
        organismList.remove(organism);
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

    /*
    private void feedAnimal(Animal animal, Organism orgToFeedOn) throws InvalidIdentifierException {
        System.out.println("ENVIRONMENT: " + animal.getInfo().getType() + " " + animal.getInfo().getID() + " feeding on " + orgToFeedOn.getInfo().getType() + " " + orgToFeedOn.getInfo().getID());
        animal.increaseEnergyLevel(orgToFeedOn.getEnergyLevel());
        domain.removeInfo(orgToFeedOn.getInfo());
        organismList.remove(orgToFeedOn);
    }

     */

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

    public String getEnvironmentDataString() {
        Map<Config.OrganismType, Integer> orgCountMap = new HashMap<>();
        for(Organism organism : organismList) {
            int counter = orgCountMap.getOrDefault(organism.getID().getType(), 0);
            orgCountMap.put(organism.getID().getType(), counter + 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Day;" + getDayCount() + ";");
        for(Config.OrganismType type : orgCountMap.keySet()) {
            sb.append(type).append(";").append(orgCountMap.get(type)).append(";");
        }
        return sb.toString();
    }

    public String getGridDataString(PositionVector pos) throws InvalidPositionException {
        return domain.getIDAtPosition(pos).toString();
    }

    public String getCurrentOrganismDataString() {
        return currentOrganismID.toString();
    }
}
