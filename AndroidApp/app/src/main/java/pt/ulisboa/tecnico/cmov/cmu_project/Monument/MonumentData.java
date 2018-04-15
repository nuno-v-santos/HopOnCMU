package pt.ulisboa.tecnico.cmov.cmu_project.Monument;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by espada on 23-03-2018.
 */

public class MonumentData implements Serializable {

    public static final String VISITED = "Visited";
    public static final String NOT_VISITED = "Not Visited";
    public static final String QUIZ = "Quiz";


    private String status = NOT_VISITED;
    private static final long serialVersionUID = 834457624276534179L;
    private String imURL;
    private String monumentName;
    private String monumentDescription;
    private String wifiId;

    private int monumentID;

    public MonumentData(String imURL, String monumentName, String monumentDescription, String wifiId, int monumentID) {
        this.imURL = imURL;
        this.monumentName = monumentName;
        this.monumentDescription = monumentDescription;
        this.wifiId = wifiId;
        this.monumentID = monumentID;
    }

    public String getImURL() {
        return this.imURL;
    }

    public String getMonumentName() {
        return this.monumentName;
    }

    public String getMonumentDescription() {
        return this.monumentDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public int getMonumentID() {
        return monumentID;
    }


    @Override
    public String toString() {

        return this.imURL + "\n" + this.monumentName + "\n" + this.getMonumentDescription();
    }


    public String getWifiId() {
        return wifiId;
    }

    public void setWifiId(String wifiId) {
        this.wifiId = wifiId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonumentData that = (MonumentData) o;
        return imURL == that.imURL &&
                Objects.equals(monumentName, that.monumentName) &&
                Objects.equals(monumentDescription, that.monumentDescription) &&
                Objects.equals(wifiId, that.wifiId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(imURL, monumentName, monumentDescription, wifiId);
    }
}
