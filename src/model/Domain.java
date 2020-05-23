package model;

import model.exceptions.IllegalEnvironmentException;
import model.exceptions.InvalidIdentifierException;
import model.exceptions.InvalidPositionException;

import java.util.*;

public class Domain {

    private static final int MIN_SIZE = 10;
    private static final int MAX_SIZE = 30;
    private static final String BLANK = "Blank";
    private final int size;
    private String[][] stringRepDomain;
    Random random = new Random();

    public Domain(int size) throws IllegalEnvironmentException {
        validateSize(size);
        this.size = size;
        stringRepDomain = new String[size][size];
        initDomain();
    }

    private void validateSize(int size) throws IllegalEnvironmentException {
        if(size < MIN_SIZE || size > MAX_SIZE) {
            throw new IllegalEnvironmentException("Invalid Size: " + size + "... Valid between " + MIN_SIZE + "-" + MAX_SIZE);
        }
    }

    private void initDomain() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                stringRepDomain[i][j] = BLANK;
            }
        }
    }

    public String[][] getStringRepDomain() {
        return stringRepDomain;
    }

    public void initOrganismPlacement(String identifier) {
        System.out.println("DOMAIN: Attempting init placement of ID " + identifier + "...");
        boolean searching = true;
        while(searching) {
            int randomX = random.nextInt(size);
            int randomY = random.nextInt(size);
            PositionVector randomPosition = new PositionVector(randomX, randomY);
            try {
                setOrganism(identifier, randomPosition);
                searching = false;
            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("DOMAIN: -> Init placement successful");
    }

    private void setOrganism(String identifier, PositionVector position) throws InvalidPositionException {
        Objects.requireNonNull(position);
        Objects.requireNonNull(identifier);
        System.out.println("DOMAIN: Attempting set ID " + identifier + " at " + position + "...");
        int x = position.getX();
        int y = position.getY();
        if(stringRepDomain[y][x].equals(BLANK)) {
            if (x < size && y < size) {
                stringRepDomain[y][x] = identifier;
                System.out.println("DOMAIN: -> Set successful");
            } else {
                throw new InvalidPositionException("DOMAIN: Position: " + position + " exceeds boarder limit");
            }
        }else {
            throw new InvalidPositionException("DOMAIN: Position: " + position + " is taken by " + stringRepDomain[y][x]);
        }
    }

    private void removeOrganism(String identifier) throws InvalidIdentifierException {
        Objects.requireNonNull(identifier);
        System.out.println("DOMAIN: Attempting remove ID " + identifier + "...");
        PositionVector pos = getPosFromId(identifier);
        stringRepDomain[pos.getY()][pos.getX()] = BLANK;
    }

    public PositionVector getPosFromId(String identifier) throws InvalidIdentifierException {
        Objects.requireNonNull(identifier);
        System.out.println("DOMAIN: Attempting get Pos from ID " + identifier + "...");
        PositionVector positionVector = null;
        boolean searching = true;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(stringRepDomain[i][j].equals(identifier)) {
                    positionVector = new PositionVector(j, i);
                    System.out.println("DOMAIN: -> Get Pos successful");
                    searching = false;
                }
            }
        }
        if(searching) {
            throw new InvalidIdentifierException("No Domain ID matches " + identifier);
        }
        Objects.requireNonNull(positionVector);
        return positionVector;
    }

    public List<String> getAllIDsInRange(String identifier, int range) throws InvalidIdentifierException {
        List<String> idList = new ArrayList<>();
        PositionVector pos = getPosFromId(identifier);
        int x = pos.getX();
        int y = pos.getY();
        for(int i = y-range; i <= y+range; i++) {
            for (int j = x - range; j <= x + range; j++) {
                //todo: replace with getContentFromPos tryCatch
                if(i >= 0 && i < size && j >= 0 && j < size && !stringRepDomain[i][j].equals(BLANK) && !stringRepDomain[i][j].equals(identifier)) {
                    idList.add(stringRepDomain[i][j]);
                }
            }
        }
        return idList;
    }

    public List<PositionVector> getEmptySpacesInRange(String identifier, int range) throws InvalidIdentifierException {
        List<PositionVector> posList = new ArrayList<>();
        PositionVector pos = getPosFromId(identifier);
        int x = pos.getX();
        int y = pos.getY();
        for(int i = y-range; i <= y+range; i++) {
            for (int j = x - range; j <= x + range; j++) {
                if(i >= 0 && i < size && j >= 0 && j < size && stringRepDomain[i][j].equals(BLANK)) {
                    posList.add(new PositionVector(j, i));
                }
            }
        }
        return posList;
    }

    public void moveOrganism(String identifier, PositionVector pos) throws InvalidIdentifierException, InvalidPositionException {
        removeOrganism(identifier);
        setOrganism(identifier, pos);
    }

    public void printDomain() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                sb.append("[");
                String stringRep = stringRepDomain[i][j];
                if(stringRepDomain[i][j].equals(BLANK)) {
                    sb.append("--");
                } else {
                    if(Integer.parseInt(stringRep) < 10) {
                        sb.append("0" + stringRep);
                    }else {
                        sb.append(stringRep);
                    }
                }
                sb.append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
