package utils.transformations;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class ValidateXML {
    public static void main(String[] args) {
        try {
            // Create a new XML parser
            XMLReader reader = XMLReaderFactory.createXMLReader();
            // Request validation
            reader.setFeature("http://xml.org/sax/features/validation", true);
            // Register the error handler
            reader.setErrorHandler(new MyErrorHandler());
            // Parse the file as the first argument on the command-line
            reader.parse(args[0]);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

