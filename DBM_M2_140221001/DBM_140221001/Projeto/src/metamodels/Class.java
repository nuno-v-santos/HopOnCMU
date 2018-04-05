package metamodels;

import java.util.ArrayList;
import java.util.List;

public class Class {
    private String name;
    private List<Attribute> attributes;
    private List<Relation> relations;
    private String pkg;
    private Class extend;
    private boolean superClass;
    private String protectedMethods;

    public Class(String name) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.pkg = "";
        this.extend = null;
        this.superClass = false;
    }

    public Class(String name, String pkg) {
        this.name = name;
        this.attributes = new ArrayList<>();
        this.relations = new ArrayList<>();
        this.pkg = pkg;
        this.extend = null;
        this.superClass = false;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public Class getExtend() {
        return extend;
    }

    public void setExtend(Class extend) {
        this.extend = extend;
    }

    public boolean getSuperClass() {
        return superClass;
    }

    public void setSuperClass(boolean superClass) {
        this.superClass = superClass;
    }

    public boolean isSuperClass() {
        return superClass;
    }

    public String getProtectedMethods() {
        return protectedMethods;
    }

    public void setProtectedMethods(String protectedMethods) {
        this.protectedMethods = protectedMethods;
    }

    @Override
    public String toString() {

        if (extend == null)
            return "Class " + this.name;
        else
            return "Class " + this.name + " (" + extend.getName() + ")";
    }
}