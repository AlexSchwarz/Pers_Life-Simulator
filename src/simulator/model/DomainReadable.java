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
    protected static final String BLANK = Config.BLANK;
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

    public String getContentAtPosition(PositionVector position) throws InvalidPositionException {
        Objects.requireNonNull(position);
        String content;
        int x = position.getX();
        int y = position.getY();
        if(x>=0 && x<size && y>=0 && y<size) {
            content = domainArray[y][x].getContent();
        }else {
            throw new InvalidPositionException("Error: Position " + position + " exceeds boarder limit");
        }
        System.out.println("DOMAIN: Return content at " + position + " " + content);
        Objects.requireNonNull(content);
        return content;
    }

    private List<Space> getSpacesInProximity(PositionVector position, int radius) {
        List<Space> list = new ArrayList<>();
        int radiusCount = 1;
        while(radiusCount <= radius) {
            int d = (5 - radius * 4) / 4;
            int centerX = position.getX();
            int centerY = position.getY();
            int x = 0;
            int y = radiusCount;
            do {
                list.add(domainArray[centerY + y][centerX + x]);
                list.add(domainArray[centerY - y][centerX + x]);
                list.add(domainArray[centerY + y][centerX - x]);
                list.add(domainArray[centerY - y][centerX - x]);
                list.add(domainArray[centerY + x][centerX + y]);
                list.add(domainArray[centerY - x][centerX + y]);
                list.add(domainArray[centerY + x][centerX - y]);
                list.add(domainArray[centerY - x][centerX - y]);
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
        return list.stream()
                .filter(distinctByKey(space -> space.getPosition().toString()))
                .collect(Collectors.toList());
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public PositionVector getClosestOccurrenceOfStringInProximity(PositionVector position, String target, int proximity) throws SimulatorErrorException {
        List<Space> list = getSpacesInProximity(position, proximity);
        Iterator<Space> it = list.iterator();
        PositionVector foundPos = null;
        boolean searching = true;
        while(searching && it.hasNext()) {
            Space space = it.next();
            if (space.getContent().contains(target)) {
                foundPos = space.getPosition();
                searching = false;
            }
        }
        if(foundPos == null) {
            throw new SimulatorErrorException("No target found in proximity");
        }
        Objects.requireNonNull(foundPos);
        System.out.println("DOMAIN: Returning pos " + foundPos + " as closest occurrence of " + target + " in prox " + proximity + " to " + position);
        return foundPos;
    }

    public PositionVector getOccurrenceOfStringInActionRange(PositionVector position, String target) throws SimulatorErrorException {
        PositionVector foundPos = null;
        int x = position.getX();
        int y = position.getY();
        int range = Config.INTERACTION_RANGE;
        for(int i = y - range; i <= y + range; i++) {
            for(int j = x - range; j <= x + range; j++) {
                if(i >= 0 && j >= 0 && i < size && j < size && !(i == y && j == x) && domainArray[i][j].getContent().contains(target)) {
                    foundPos = new PositionVector(j,i);
                }
            }
        }
        if(foundPos == null) {
            throw new SimulatorErrorException("No target found in proximity");
        }
        Objects.requireNonNull(foundPos);
        System.out.println("DOMAIN: Returning pos " + foundPos + " occurrence of " + target + " in action range of " + position);
        return foundPos;
    }

    protected PositionVector getPositionOfId(String id) throws InvalidIdentifierException {
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
            throw new InvalidIdentifierException("No ID in Domain matches " + id);
        }
        System.out.println("DOMAIN: Returning pos " + position + " for ID " + id);
        Objects.requireNonNull(position);
        return position;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                sb.append("[");
                String stringRep = domainArray[i][j].getContent();
                if(!stringRep.equals(BLANK)) {
                    if (Integer.parseInt(stringRep) < 10) {
                        sb.append("00");
                    } else if (Integer.parseInt(stringRep) < 100) {
                        sb.append("0");
                    }
                }
                sb.append(stringRep);
                sb.append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
