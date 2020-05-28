package simulator.model;

public class Identification {

    private final String number;
    private final Config.OrganismType type;

    public Identification(String number, Config.OrganismType type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public Config.OrganismType getType() {
        return type;
    }

    public boolean isBlank() {
        return this.equals(new Identification(String.valueOf(Config.BLANK), Config.OrganismType.VOID));
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Identification)) throw new ClassCastException();
        final Identification otherIdentification = (Identification) other;
        return number.equals(otherIdentification.getNumber()) && type.equals(otherIdentification.getType());
    }

    @Override
    public String toString() {
        return "{" + number + "," + type + "}";
    }
}
