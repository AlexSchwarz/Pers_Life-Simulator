package model;

import model.exceptions.InvalidPositionException;
import model.organism.Carnivore;
import model.organism.Herbivore;
import model.organism.Organism;
import model.organism.Plant;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Domain {

    private Organism[][] domain;
    Random random = new Random();
    private final int HEIGHT;
    private final int WIDTH;
    private File dataFile;

    private enum OrganismType {
        PLANT, HERBIVORE, CARNIVORE;
    }

    private static Organism newOrganismFromType(OrganismType organismType) {
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
                throw new IllegalArgumentException("Not a valid type");
        }
        return organism;
    }

    public Domain(int width, int height, int plantCount, int herbivoreCount, int carnivoreCount) {
        initDataFile();
        this.WIDTH = width;
        this.HEIGHT = height;
        domain = new Organism[HEIGHT][WIDTH];
        initOrganisms(plantCount, herbivoreCount, carnivoreCount);
    }

    private void initDataFile() {
        dataFile = new File("C:/Users/AlexanderPatrick/IdeaProjects/Pers_Life-Simulator/files/data.csv");
        dataFile.delete();
        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //dataFile.deleteOnExit();
    }

    public Organism[][] getDomain() {
        return domain;
    }

    private void initOrganisms(int plantCount, int herbivoreCount, int carnivoreCount) {
        initOrganismFromTypeAndCount(OrganismType.PLANT, plantCount);
        initOrganismFromTypeAndCount(OrganismType.HERBIVORE, herbivoreCount);
        initOrganismFromTypeAndCount(OrganismType.CARNIVORE, carnivoreCount);
    }

    private void initOrganismFromTypeAndCount(OrganismType organismType, int orgCount) {
        System.out.println("DOMAIN: Init " + orgCount + " " + organismType);
        int counter = 0;
        while(counter < orgCount ) {
            Organism organism = newOrganismFromType(organismType);
            boolean searching = true;
            while(searching) {
                int randomX = random.nextInt(HEIGHT);
                int randomY = random.nextInt(WIDTH);
                PositionVector randomPosition = new PositionVector(randomX, randomY);
                try {
                    setOrganism(randomPosition, organism);
                    searching = false;
                } catch (InvalidPositionException e) {
                    System.out.println(e.getMessage());
                }
            }
            counter++;
        }
        System.out.println("DOMAIN: -> Init organism successful");
    }

    private void setOrganism(PositionVector position, Organism organism) throws InvalidPositionException {
        Objects.requireNonNull(position);
        Objects.requireNonNull(organism);
        System.out.println("DOMAIN: Attempting to set " + organism.toString() + " at " + position);
        int x = position.getX();
        int y = position.getY();
        if(domain[y][x] == null) {
            if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                domain[y][x] = organism;
            } else {
                throw new InvalidPositionException("DOMAIN: Position: " + position + " exceeds boarder limit");
            }
            System.out.println("DOMAIN: -> Set successful");
            registerOrganism(organism, position);
            //printDomain();
        }else {
            throw new InvalidPositionException("DOMAIN: Position: " + position + " is taken by " + domain[y][x].toString());
        }
    }

    private void registerOrganism(Organism organism, PositionVector position) {
        System.out.println("FILE: Attempting register...");
        String csvDataString = convertToCSV(organism, position);
        System.out.println("FILE: -> Register successful");
    }

    public String convertToCSV(Organism organism, PositionVector positionVector) {
        return String.format("%s;%s;%s", organism.getOrganismID(), organism.getType(), positionVector.toString());
    }

    public void printDomain() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < HEIGHT; i++) {
            for(int j = 0; j < WIDTH; j++) {
                sb.append("[");
                if(domain[i][j] == null) {
                    sb.append("---");
                } else {
                    Organism organism = domain[i][j];
                    sb.append(organism.toString());
                }
                sb.append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
