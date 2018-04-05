package utils.transformations;

import metamodels.*;
import metamodels.Class;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by pctm on 20/04/2017.
 */
public class Model2Model {

    public static Model getModel(String filename) {
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

            System.out.println(model);
            return model;

            // Do M2M transformation
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
