package metamodels;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private String name;
    private List<Class> classes;
    private List<Relation> relations;


    public Model(String name) {
        this.name = name;
        this.classes = new ArrayList<>();
        this.relations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }

    public void addClass(Class clazz) {
        this.classes.add(clazz);
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public void addRelation(Relation relation) {
        this.relations.add(relation);
    }

    public Class getClassbyName(String name) {

        for (Class c : classes) {
            if (c.getName().equals(name)) {
                return c;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Model{" +
                "name='" + name + '\'' +
                ", classes=" + classes +
                ", relations=" + relations +
                '}';
    }
}