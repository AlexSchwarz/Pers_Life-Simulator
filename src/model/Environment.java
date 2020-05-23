package model;

import model.exceptions.IllegalEnvironmentException;
import model.exceptions.InvalidIdentifierException;
import model.exceptions.InvalidPositionException;
import model.organism.Carnivore;
import model.organism.Herbivore;
import model.organism.Organism;
import model.organism.Plant;

import java.util.*;

public class Environment {

    private static final int RANGE = 1;
    private final int maxOrganisms;
    private Set<Organism> organisms = new HashSet<>();
    private Domain domain;

    public Environment(int size, int plantCount, int herbivoreCount, int carnivoreCount) throws IllegalEnvironmentException {
        maxOrganisms = size*size;
        domain = new Domain(size);
        validateOrganismCount(plantCount, herbivoreCount, carnivoreCount);
        initOrganismsStart(plantCount, herbivoreCount, carnivoreCount);
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
            domain.initOrganismPlacement(organism.getOrganismID());
            counter ++;
        }
        System.out.println("ENVIRONMENT: -> Init " + orgCount + " " + type + " successful");
    }

    public void progressEnvironment() throws IllegalEnvironmentException, InvalidIdentifierException, InvalidPositionException {
        System.out.println("ENVIRONMENT: Attempting progression all organisms...");
        for(Organism organism : organisms) {
            Objects.requireNonNull(organism);
            if(organism instanceof Plant) {
                progressPlant(organism);
            } else if (organism instanceof  Carnivore) {
                progressCarnivore(organism);
            } else if (organism instanceof Herbivore) {
                progressHerbivore(organism);
            } else {
                throw new IllegalEnvironmentException("Object not instance of any Organism");
            }
        }
        printDomain();
        System.out.println("ENVIRONMENT: Progression all organisms ended");
    }

    private void progressPlant(Organism organism) {
        System.out.println("ENVIRONMENT: Attempting progression " + organism.getType() + " ID " + organism.getOrganismID());
        System.out.println("ENVIRONMENT: Progression organism ended");
    }

    private void progressCarnivore(Organism organism) throws InvalidIdentifierException, InvalidPositionException {
        System.out.println("ENVIRONMENT: Attempting progression " + organism.getType() + " ID " + organism.getOrganismID());
        //getAllIDs in range. iterate. if org owner of id of interes, move that way, else random
        boolean searchingTarget = true;
        List<String> iDsInRange = domain.getAllIDsInRange(organism.getOrganismID(), 4);
        for(String id : iDsInRange) {
            if(getOrganismFromId(id) instanceof Herbivore) {
                domain.moveOrganism(organism.getOrganismID(), domain.getEmptySpacesInRange(id, 1).get(0));
                searchingTarget = false;
            }
        }
        if(searchingTarget) {
            List<PositionVector> emptySpaces = domain.getEmptySpacesInRange(organism.getOrganismID(), 4);
            Collections.shuffle(emptySpaces);
            domain.moveOrganism(organism.getOrganismID(), emptySpaces.get(0));
        }
        System.out.println("ENVIRONMENT: Progression organism ended");
    }

    private void progressHerbivore(Organism organism) throws InvalidIdentifierException, InvalidPositionException {
        System.out.println("ENVIRONMENT: Attempting progression " + organism.getType() + " ID " + organism.getOrganismID());
        List<PositionVector> emptySpaces = domain.getEmptySpacesInRange(organism.getOrganismID(), RANGE);
        Collections.shuffle(emptySpaces);
        //domain.moveOrganism(organism.getOrganismID(), emptySpaces.get(0));
        System.out.println("ENVIRONMENT: Progression organism ended");
    }

    private Organism getOrganismFromId(String identifier) throws InvalidIdentifierException {
        Organism foundOrganism = null;
        boolean searching = true;
        for(Organism organism : organisms) {
            if(organism.getOrganismID().equals(identifier)) {
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

    private void registerOrganism(Organism organism, PositionVector position) {
        System.out.println("FILE: Attempting register...");
        String csvDataString = convertOrgToCSV(organism, position);
        System.out.println("FILE: -> Register successful");
    }

    public String convertOrgToCSV(Organism organism, PositionVector positionVector) {
        return String.format("%s;%s;%s", organism.getOrganismID(), organism.getType(), positionVector.toString());
    }

    public void printDomain() {
        domain.printDomain();
    }
}
