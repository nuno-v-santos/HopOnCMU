package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.Ranking;

/**
 * Created by tiago on 28-Mar-18.
 */

public class User {

    private String name;
    private int points;

    public User(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
