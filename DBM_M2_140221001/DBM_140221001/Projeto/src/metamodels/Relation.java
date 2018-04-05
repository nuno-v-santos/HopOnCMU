package metamodels;



/**
 * Created by pctm on 06/05/2017.
 */
public class Relation {
    private Class base;
    private Class target;
    private RelationType type;

    public Relation(Class base, Class target, RelationType type) {
        this.base = base;
        this.target = target;
        this.type = type;
    }

    public Class getBase() {
        return base;
    }

    public void setBase(Class base) {
        this.base = base;
    }

    public Class getTarget() {
        return target;
    }

    public void setTarget(Class target) {
        this.target = target;
    }

    public RelationType getType() {
        return type;
    }

    public void setType(RelationType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return base+"->"+target +"["+type+"]";
    }
}
