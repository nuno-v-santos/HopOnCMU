package metamodels;

/**
 * Created by pctm on 06/05/2017.
 */
public enum RelationType {
    OneToOne, OneToMany, ManyToOne, ManyToMany;


    @Override
    public String toString() {
        switch (this) {
            case OneToOne:
                return "OneToOne";
            case OneToMany:
                return "OneToMany";
            case ManyToOne:
                return "ManyToOne";
            case ManyToMany:
                return "ManyToMany";
            default:
                return "";
        }
    }

    public static RelationType getRelation(String type) {
        switch (type) {
            case "OneToOne":
                return OneToOne;
            case "OneToMany":
                return OneToMany;
            case "ManyToOne":
                return ManyToOne;
            case "ManyToMany":
                return ManyToMany;
            default:
                return null;
        }
    }

}
