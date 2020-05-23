package simulator.model;

/**
 * Holds a position (vector to x,y-position of the car on the track grid)
 * or a velocity vector (x,y-components of the velocity vector of a car).
 *
 * Created by mach 21.01.2020
 */
public final class PositionVector {
    private int x; // horizontal component (position / velocity)
    private int y; // vertical component (position / velocity)


    /**
     * Adds two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return A new PositionVector holding the result of the addition. If both
     * arguments are positions (not velocity), the result is mathematically
     * correct but meaningless.
     */
    public static PositionVector add(final PositionVector vectorA, final PositionVector vectorB) {
        return new PositionVector(vectorA.getX() + vectorB.getX(), vectorA.getY() + vectorB.getY());
    }

    /**
     * Subtracts two PositionVectors (e.g. car position and velocity vector or two velocity vectors).
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return A new PositionVector holding the result of the addition. If both
     * arguments are positions (not velocity), the result is mathematically
     * correct but meaningless.
     */
    public static PositionVector subtract(final PositionVector vectorA, final PositionVector vectorB) {
        return new PositionVector(vectorA.getX() - vectorB.getX(), vectorA.getY() - vectorB.getY());
    }

    /**
     * Calculates the scalar product (Skalarprodukt) of two 2D vectors. The scalar product
     * multiplies the lengths of the parallel components of the vectors.
     *
     * @param vectorA A position or velocity vector
     * @param vectorB A position or velocity vector
     * @return The scalar product (vectorA * vectorB). Since vectorA and
     * vectorB are PositionVectors, which hold only integer coordinates,
     * the resulting scalar product is an integer.
     */
    public static int scalarProduct(final PositionVector vectorA, final PositionVector vectorB) {
        return (vectorA.getY() * vectorB.getY()) + (vectorA.getX() * vectorB.getX());
    }

    public static double magnitude(final PositionVector vector) {
        double i = Math.pow(vector.getX(), 2);
        double j = Math.pow(vector.getY(), 2);
        double mag = Math.sqrt(i+j);
        return mag;
    }

    public PositionVector(final int x, final int y) {
        this.y = y;
        this.x = x;
    }

    public PositionVector(final PositionVector other) {
        x = other.getX();
        y = other.getY();
    }

    public PositionVector() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }


    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof PositionVector)) throw new ClassCastException();
        final PositionVector otherPositionVector = (PositionVector) other;
        return y == otherPositionVector.getY() && x == otherPositionVector.getX();
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "]";
    }
}

