package simulator.model;

import simulator.model.exceptions.IllegalEnvironmentException;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;
import simulator.model.exceptions.SimulatorErrorException;
import simulator.model.organism.Carnivore;
import simulator.model.organism.Herbivore;
import simulator.model.organism.Organism;
import simulator.model.organism.Plant;

import java.util.*;

public class Environment {

    private static final int RANGE = 1;
    private final int maxOrganisms;
    private List<Organism> organisms = new ArrayList<>();
    private Organism currentOrganism;
    private Domain domain;

    public Environment(int size, int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException {
        maxOrganisms = size*size;
        domain = new Domain(size);
        validateOrganismCount(plantCount, herbivoreCount, carnivoreCount);
        initOrganismsStart(plantCount, herbivoreCount, carnivoreCount);
        currentOrganism = organisms.get(0);
    }

    private void validateOrganismCount(int orgCount1, int orgCount2, int orgCount3) throws IllegalEnvironmentException {
        if(orgCount1 < 0 || orgCount2 < 0 || orgCount3 < 0 || orgCount1+orgCount2+orgCount3 > maxOrganisms) {
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

    public void progressEnvironment() throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException, SimulatorErrorException {
        Organism organism = currentOrganism;
        Objects.requireNonNull(organism);
        if(organism instanceof Plant) {
            progressPlant(organism);
        } else if (organism instanceof  Carnivore) {
            Carnivore carnivore = (Carnivore) organism;
            progressCarnivore(carnivore);
        } else if (organism instanceof Herbivore) {
            Herbivore herbivore = (Herbivore) organism;
            progressHerbivore(herbivore);
        } else {
            throw new IllegalEnvironmentException("Object not instance of any Organism");
        }
        switchNextOrganism();
    }

    private void switchNextOrganism() throws SimulatorErrorException {
        if(!organisms.isEmpty()) {
            try {
                currentOrganism = organisms.get(organisms.indexOf(currentOrganism) + 1);
            } catch (IndexOutOfBoundsException e) {
                currentOrganism = organisms.get(0);
                throw new SimulatorErrorException("next day");
            }
        }
    }

    private void progressPlant(Organism organism) {
        System.out.println("ENVIRONMENT: Attempting progression " + organism.getType() + " ID " + organism.getId());
        System.out.println("ENVIRONMENT: Progression organism ended");
    }

    private void progressCarnivore(Carnivore carnivore) throws InvalidIdentifierException, InvalidPositionException {
        //todo: for point of interest also use closest like with empty space magnitude calc
        //-> enviroment filter list of ids close to organism based on energy. Give domain smaller list and say move to closest one.
        System.out.println("ENVIRONMENT: Attempting progression " + carnivore.getType() + " ID " + carnivore.getId());
        List<String> idsInRangeList = domain.getAllIDsInProximity(carnivore.getId(), carnivore.getSightRange());
        boolean searchingForTarget = true;
        System.out.println("ENVIRONMENT: Searching through IDs...");
        for(String targetId : idsInRangeList) {
            if(searchingForTarget && getOrganismFromId(targetId) instanceof Herbivore) {
                System.out.println("ENVIRONMENT: Found Herbivore ID " + targetId);
                domain.moveInProxToTarget(carnivore.getId(), targetId, carnivore.getMovementRange());
                searchingForTarget = false;
            }
        }
        if(searchingForTarget) {
            System.out.println("ENVIRONMENT: Found not point of interest. Random Move");
            domain.moveOrgRandomInRange(carnivore.getId(), carnivore.getMovementRange());
        }
    }

    private void progressHerbivore(Herbivore herbivore) throws InvalidIdentifierException, InvalidPositionException {
        System.out.println("ENVIRONMENT: Attempting progression " + herbivore.getType() + " ID " + herbivore.getId());
        domain.moveOrgRandomInRange(herbivore.getId(), herbivore.getMovementRange());
        System.out.println("ENVIRONMENT: Progression organism ended");
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
