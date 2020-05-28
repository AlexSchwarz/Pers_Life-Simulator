package simulator.model;

public class Space {

    private final PositionVector position;
    private String content = Config.BLANK;

    public Space(PositionVector position) {
        this.position = position;
    }

    public String getContent() {
        return content;
    }

    public PositionVector getPosition() {
        return position;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setBlank() {
        content = Config.BLANK;
    }
}
