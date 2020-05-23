package simulator.model;

import simulator.model.exceptions.IllegalEnvironmentException;
import simulator.model.exceptions.InvalidIdentifierException;
import simulator.model.exceptions.InvalidPositionException;

import java.util.*;

public class Domain {

    private static final int MIN_SIZE = 10;
    private static final int MAX_SIZE = 30;
    private static final String BLANK = "--";
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
        String content = getContentFromPosition(position);
        if(content.equals(BLANK)) {
            stringRepDomain[position.getY()][position.getX()] = identifier;
            System.out.println("DOMAIN: -> Set successful");
        } else {
            throw new InvalidPositionException("Error: Position: " + position + " is taken by " + content);
        }
    }

    public String getContentFromPosition(PositionVector pos) throws InvalidPositionException {
        Objects.requireNonNull(pos);
        String foundContent;
        int x = pos.getX();
        int y = pos.getY();
        if(x>=0 && x<size && y>=0 && y<size) {
            foundContent = stringRepDomain[y][x];
        }else {
            throw new InvalidPositionException("Error: Position: Position " + pos + " exceeds boarder limit");
        }
        return foundContent;
    }

    public void removeOrganism(String identifier) throws InvalidIdentifierException {
        Objects.requireNonNull(identifier);
        System.out.println("DOMAIN: Attempting remove ID " + identifier + "...");
        PositionVector pos = getPosFromId(identifier);
        stringRepDomain[pos.getY()][pos.getX()] = BLANK;
        System.out.println("DOMAIN: -> Removal ID " + identifier + " successful");
    }

    private PositionVector getPosFromId(String identifier) throws InvalidIdentifierException {
        Objects.requireNonNull(identifier);
        PositionVector positionVector = null;
        boolean searching = true;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if(stringRepDomain[i][j].equals(identifier)) {
                    positionVector = new PositionVector(j, i);
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

    public List<String> getAllIDsInProximity(String identifier, int proximity) throws InvalidIdentifierException {
        System.out.println("DOMAIN: Get all IDs in " + proximity + " prox to ID " + identifier);
        List<String> idList = new ArrayList<>();
        PositionVector pos = getPosFromId(identifier);
        int x = pos.getX();
        int y = pos.getY();
        for(int i = y-proximity; i <= y+proximity; i++) {
            for (int j = x-proximity; j <= x+proximity; j++) {
                if(i >= 0 && i < size && j >= 0 && j < size && !stringRepDomain[i][j].equals(BLANK)) {
                    idList.add(stringRepDomain[i][j]);
                    idList.remove(identifier);
                }
            }
        }
        System.out.println("DOMAIN: Found these IDs " + idList);
        Collections.shuffle(idList);
        return idList;
    }

    public void moveInProxToTarget(String identifier, String targetId, int proximity) throws InvalidIdentifierException, InvalidPositionException {
        System.out.println("DOMAIN: Attempting move ID " + identifier + " to target " + targetId + " with prox " + proximity + "...");
        PositionVector orgPos = getPosFromId(identifier);
        PositionVector targetPos = getPosFromId(targetId);
        PositionVector foundPos = orgPos;
        double shortestDistance = PositionVector.magnitude(PositionVector.subtract(targetPos, orgPos));
        List<PositionVector> emptySpacesInProx = getEmptySpacesInProximity(identifier, proximity);
        for(PositionVector emptyPos : emptySpacesInProx) {
            double distance = PositionVector.magnitude(PositionVector.subtract(targetPos, emptyPos));
            if( distance < shortestDistance) {
                foundPos = emptyPos;
                shortestDistance = distance;
            }
        }
        moveOrganism(identifier, foundPos);
    }

    private List<PositionVector> getEmptySpacesInProximity(String identifier, int proximity) throws InvalidIdentifierException {
        List<PositionVector> posList = new ArrayList<>();
        PositionVector pos = getPosFromId(identifier);
        int x = pos.getX();
        int y = pos.getY();
        for(int i = y-proximity; i <= y+proximity; i++) {
            for (int j = x - proximity; j <= x + proximity; j++) {
                if(i >= 0 && i < size && j >= 0 && j < size && i != y && j != x && stringRepDomain[i][j].equals(BLANK)) {
                    posList.add(new PositionVector(j, i));
                }
            }
        }
        return posList;
    }

    private void moveOrganism(String identifier, PositionVector pos) throws InvalidIdentifierException, InvalidPositionException {
        System.out.println("DOMAIN: Move ID " + identifier + " to " + pos);
        removeOrganism(identifier);
        setOrganism(identifier, pos);
    }

    public void moveOrgRandomInRange(String identifier, int proximity) throws InvalidIdentifierException, InvalidPositionException {
        System.out.println("DOMAIN: Random move ID " + identifier + " in prox " + proximity);
        List<PositionVector> emptySpacesInProx = getEmptySpacesInProximity(identifier, proximity);
        if(!emptySpacesInProx.isEmpty()) {
            Collections.shuffle(emptySpacesInProx);
            moveOrganism(identifier, emptySpacesInProx.get(0));
        }else {
            System.out.println("DOMAIN: No empty space in prox, no move");
        }
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
