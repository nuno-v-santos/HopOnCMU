package M2M;

import metamodels.Attribute;
import metamodels.Class;
import metamodels.Model;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by pctm on 12/05/2017.
 */
public class XMITOModel implements M2M {

    private String filename;

    public XMITOModel(String filename) {
        this.filename = filename;
    }

    @Override
    public Model getModel() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
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

                    if (classes.item(i).getNodeName().equals("packagedElement")) {
                        Class c = new Class(classes.item(i).getAttributes().getNamedItem("name").getNodeValue(), modelName);

                        NodeList attributes = classes.item(i).getChildNodes();

                        for (int j = 0; j < attributes.getLength(); j++) {
                            if (attributes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                String name = attributes.item(j).getAttributes().getNamedItem("name").getNodeValue();
                                String type = attributes.item(j).getChildNodes().item(1).getAttributes().getNamedItem("href").getNodeValue().split("#")[1];
                                if (type.equals("Integer")) {
                                    type = "int";
                                }
                                Attribute at = new Attribute(name, type);

                                c.addAttribute(at);

                            }
                        }

                        model.addClass(c);
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
