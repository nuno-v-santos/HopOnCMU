package M2M;

import metamodels.*;
import metamodels.Class;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import utils.transformations.MyErrorHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by pctm on 08/05/2017.
 */
public class XMLTOModel implements M2M {

    private String filename;

    public XMLTOModel(String filename) {
        this.filename = filename;
    }


    @Override
    public Model getModel() {

        HashMap<Class, String> extensions = new HashMap<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new MyErrorHandler());
            Document document = db.parse(new File(filename));
            // Get model node
            Node modelNode = document.getDocumentElement();
            String modelName = modelNode.getAttributes()
                    .getNamedItem("name").getNodeValue();
            NodeList classes = modelNode.getChildNodes();
            //instance model
            Model model = new Model(modelName);

            for (int i = 0; i < classes.getLength(); i++) {
                if (classes.item(i).getNodeType() == Node.ELEMENT_NODE) {

                    String typeNode = classes.item(i).getNodeName();

                    if (typeNode == "class") {
                        String className = classes.item(i).getAttributes().getNamedItem("name").getNodeValue();
                        Class clazz = new Class(className, modelName);
                        model.addClass(clazz);
                        NodeList atributtos = classes.item(i).getChildNodes();

                        String extend = classes.item(i).getAttributes().getNamedItem("extends") == null ? null : classes.item(i).getAttributes().getNamedItem("extends").getNodeValue();
                        extensions.put(clazz, extend);

                        for (int k = 0; k < atributtos.getLength(); k++) {

                            if (atributtos.item(k).getNodeType() == Node.ELEMENT_NODE) {

                                Attribute attribute;
                                String name = atributtos.item(k).getAttributes().getNamedItem("name").getNodeValue();
                                String type = atributtos.item(k).getAttributes().getNamedItem("type").getNodeValue();
                                String required = atributtos.item(k).getAttributes().getNamedItem("required") == null ? null : atributtos.item(k).getAttributes().getNamedItem("required").getNodeValue();

                                if (required != null && required.equals("true")) {
                                    attribute = new Attribute(name, type, true);
                                } else {
                                    attribute = new Attribute(name, type);
                                }

                                if (attribute.getType().equals("int") || attribute.getType().equals("double") || attribute.getType().equals("float")
                                        || attribute.getType().equals("long") || attribute.getType().equals("short") || attribute.getType().equals("byte")) {
                                    checkValidationsNumeric(attribute, atributtos.item(k));
                                }

                                if (attribute.getType().equals("String")) {
                                    checkValidationsString(attribute, atributtos.item(k));
                                }

                                clazz.addAttribute(attribute);
                            }
                        }
                    }

                    if (typeNode == "relation") {

                        String base = classes.item(i).getAttributes().getNamedItem("base").getNodeValue();
                        String target = classes.item(i).getAttributes().getNamedItem("target").getNodeValue();
                        String type = classes.item(i).getAttributes().getNamedItem("type").getNodeValue();

                        Relation relation = new Relation(model.getClassbyName(base), model.getClassbyName(target), RelationType.getRelation(type));
                        model.addRelation(relation);
                    }
                }
            }

            Iterator<Class> it = extensions.keySet().iterator();

            while (it.hasNext()) {
                Class c = it.next();
                c.setExtend(model.getClassbyName(extensions.get(c)));

                if (extensions.get(c) != null)
                    model.getClassbyName(extensions.get(c)).setSuperClass(true);
            }


            System.out.println(model);
            return model;

            // Do M2M transformation
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private void checkValidationsString(Attribute attribute, Node item) {
        String contains = item.getAttributes().getNamedItem("contains") == null ? null : item.getAttributes().getNamedItem("contains").getNodeValue();
        String notEmpty = item.getAttributes().getNamedItem("notEmpty") == null ? null : item.getAttributes().getNamedItem("notEmpty").getNodeValue();
        String minLength = item.getAttributes().getNamedItem("minLength") == null ? null : item.getAttributes().getNamedItem("minLength").getNodeValue();
        String maxLength = item.getAttributes().getNamedItem("maxLength") == null ? null : item.getAttributes().getNamedItem("maxLength").getNodeValue();


        if(contains != null){
            attribute.setContains(contains);
        }

        if(notEmpty != null && notEmpty.equals("true")){
            attribute.setNotEmpty(true);
        }

        if(minLength != null){
            attribute.setMinLength(new Integer(minLength));
        }

        if(maxLength != null){
            attribute.setMaxLength(new Integer(maxLength));
        }
/*
        System.out.println("««««««««««««««««««««««««««««««««««««««««««««««««««««");
        System.out.println(contains);
        System.out.println(notEmpty);
        System.out.println(minLength);
        System.out.println(maxLength);
        System.out.println("««««««««««««««««««««««««««««««««««««««««««««««««««««");
*/
    }

    private void checkValidationsNumeric(Attribute attribute, Node item) {

        String min = item.getAttributes().getNamedItem("min") == null ? null : item.getAttributes().getNamedItem("min").getNodeValue();
        String max = item.getAttributes().getNamedItem("max") == null ? null : item.getAttributes().getNamedItem("max").getNodeValue();

        if(min != null){
            attribute.setMin(new Integer(min));
        }

        if(max != null){
            attribute.setMax(new Integer(max));
        }

        /*
        System.out.println("*****************************************************");
        System.out.println(min);
        System.out.println(max);
        System.out.println("*****************************************************");
*/
    }

}
