package pt.ulisboa.tecnico.cmov.cmu_project.Quiz;

/**
 * Created by tiago on 17-May-18.
 */

public class QuizEvent {
    private int munId;
    private String type;
    private String value;


    public QuizEvent(int munId, String type, String value) {
        this.munId = munId;
        this.type = type;
        this.value = value;
    }

    public int getMunId() {
        return munId;
    }

    public void setMunId(int munId) {
        this.munId = munId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
