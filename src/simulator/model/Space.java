package simulator.model;

public class Space {

    private final PositionVector position;
    private Identification content = new Identification(String.valueOf(Config.BLANK), Config.OrganismType.VOID);

    public Space(PositionVector position) {
        this.position = position;
    }

    public Identification getContent() {
        return content;
    }

    public PositionVector getPosition() {
        return position;
    }

    public void setContent(Identification content) {
        this.content = content;
    }

    public void setBlank() {
        content = new Identification(String.valueOf(Config.BLANK), Config.OrganismType.VOID);
    }
}
