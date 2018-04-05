package pt.ulisboa.tecnico.cmov.cmu_project.Fragments.MonumentList;

/**
 * Created by tiago on 28-Mar-18.
 */

public class Monument {

    public static final String VISITED="Visited";
    public static final String NOT_VISITED="Not Visited";
    public static final String QUIZ="Quiz";

    private String status;
    private String image;


    public Monument(String status) {
        this.status = status;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
