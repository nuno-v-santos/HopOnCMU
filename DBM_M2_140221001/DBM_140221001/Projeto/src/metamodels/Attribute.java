package metamodels;

import java.util.Date;

public class Attribute {
    private String name;
    private String type;
    private boolean required;
    private Integer max;
    private Integer min;
    private String contains;
    private boolean notEmpty;
    private Integer maxLength;
    private Integer minLength;


    public Attribute(String name, String type) {
        this.name = name;
        this.type = type;
        this.required = false;
        this.notEmpty = false;
    }

    public Attribute(String name, String type, boolean required) {
        this.name = name;
        this.type = type;
        this.required = required;
        this.notEmpty = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public boolean getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    @Override
    public String toString() {
        return name +" : "+type;
    }
}
