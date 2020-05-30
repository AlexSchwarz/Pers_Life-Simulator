package simulator.model;

import simulator.model.exceptions.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DomainReadable {

    private static final int MIN_SIZE = 10;
    private static final int MAX_SIZE = 30;
    protected final int size;
    protected final Space[][] domainArray;

    public DomainReadable(int size) throws SimulatorErrorException {
        validateSize(size);
        this.size = size;
        domainArray = new Space[size][size];
        initDomain();
    }

    private void validateSize(int size) throws SimulatorErrorException {
        if(size < MIN_SIZE || size > MAX_SIZE) {
            throw new SimulatorErrorException("Invalid Size: " + size + "... Valid between " + MIN_SIZE + "-" + MAX_SIZE);
        }
    }

    public void initDomain() {
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                domainArray[i][j] = new Space(new PositionVector(j,i));
            }
        }
    }

    private Space getSpaceAtPosition(int x, int y) {
        Space space = null;
        if(x>=0 && x<size && y>=0 && y<size) {
            space = domainArray[y][x];
        }
        return space;
    }

    protected Identification getIDAtPosition(PositionVector position) throws InvalidPositionException {
        Objects.requireNonNull(position);
        Space space = getSpaceAtPosition(position.getX(), position.getY());
        if(space == null) throw new InvalidPositionException("Error: Position " + position + " exceeds boarder limit");
        Identification id = space.getContent();
        //System.out.println("DOMAIN: Return content at " + position + " " + id);
        Objects.requireNonNull(id);
        return id;
    }

    public List<Space> getSpacesInProximity(PositionVector position, int radius) {
        List<Space> list = new ArrayList<>();
        int radiusCount = 1;
        while(radiusCount <= radius) {
            int d = (5 - radius * 4) / 4;
            int centerX = position.getX();
            int centerY = position.getY();
            int x = 0;
            int y = radiusCount;
            do {
                list.add(getSpaceAtPosition(centerX + x,centerY + y));
                list.add(getSpaceAtPosition(centerX + x,centerY - y));
                list.add(getSpaceAtPosition(centerX - x,centerY + y));
                list.add(getSpaceAtPosition(centerX - x,centerY - y));

                list.add(getSpaceAtPosition(centerX + y,centerY + x));
                list.add(getSpaceAtPosition(centerX + y,centerY - x));
                list.add(getSpaceAtPosition(centerX - y,centerY + x));
                list.add(getSpaceAtPosition(centerX - y,centerY - x));

                //list.add(domainArray[centerY + y][centerX + x]);
                //list.add(domainArray[centerY - y][centerX + x]);
                //list.add(domainArray[centerY + y][centerX - x]);
                //list.add(domainArray[centerY - y][centerX - x]);
                //list.add(domainArray[centerY + x][centerX + y]);
                //list.add(domainArray[centerY - x][centerX + y]);
                //list.add(domainArray[centerY + x][centerX - y]);
                //list.add(domainArray[centerY - x][centerX - y]);
                if (d < 0) {
                    d += 2 * x + 1;
                } else {
                    d += 2 * (x - y) + 1;
                    y--;
                }
                x++;
            } while (x <= y);
            radiusCount++;
        }
        list.removeAll(Collections.singleton(null));
        return list.stream()
                .filter(distinctByKey(space -> space.getPosition().toString()))
                .collect(Collectors.toList());
    }

    private static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /*
    public List<Space> getSpacesInActionProximity(PositionVector position) {
        List<Space> list = new ArrayList<>();
        int x = position.getX();
        int y = position.getY();
        int range = Config.INTERACTION_RANGE;
        for(int i = y - range; i <= y + range; i++) {
            for(int j = x - range; j <= x + range; j++) {
                if(i >= 0 && j >= 0 && i < size && j < size && !(i == y && j == x)) {
                    list.add(domainArray[i][j]);
                }
            }
        }
        return list;
    }
     */

    public PositionVector getPositionOfID(Identification id) throws InvalidIdentifierException {
        Objects.requireNonNull(id);
        PositionVector position = null;
        boolean searching = true;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                Space space = domainArray[i][j];
                if(searching && space.getContent().equals(id)) {
                    position = space.getPosition();
                    searching = false;
                }
            }
        }
        if(searching) {
            throw new InvalidIdentifierException("No number in Domain matches " + id.toString());
        }
        //System.out.println("DOMAIN: Returning pos " + position + " for ID " + id.toString());
        Objects.requireNonNull(position);
        return position;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                sb.append("[");
                String stringRep = domainArray[i][j].getContent().toString();
                sb.append(stringRep);
                sb.append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
