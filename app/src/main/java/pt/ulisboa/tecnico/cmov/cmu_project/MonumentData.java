package pt.ulisboa.tecnico.cmov.cmu_project;

/**
 * Created by espada on 23-03-2018.
 */

public class MonumentData {

    private int imID;
    private String monumentName;
    private String monumentDescription;


    public MonumentData(int imID, String monumentName, String monumentDescription) {

        this.imID = imID;
        this.monumentName = monumentName;
        this.monumentDescription = monumentDescription;
    }

    public int getImID() {
        return this.imID;
    }

    public String getMonumentName() {
        return this.monumentName;
    }

    public String getMonumentDescription() {
        return this.monumentDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MonumentData that = (MonumentData) o;

        if (imID != that.imID) return false;
        if (monumentName != null ? !monumentName.equals(that.monumentName) : that.monumentName != null)
            return false;
        return monumentDescription != null ? monumentDescription.equals(that.monumentDescription) : that.monumentDescription == null;
    }

    @Override
    public int hashCode() {
        int result = imID;
        result = 31 * result + (monumentName != null ? monumentName.hashCode() : 0);
        result = 31 * result + (monumentDescription != null ? monumentDescription.hashCode() : 0);
        return result;
    }
}
