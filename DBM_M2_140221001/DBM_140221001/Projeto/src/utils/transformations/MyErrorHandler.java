package utils.transformations;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class MyErrorHandler implements ErrorHandler {
    public void warning(SAXParseException exception) throws SAXException {
        // Bring things to a crashing halt
        System.out.println("**Parsing Warning**" +
                "  Line:    " +
                exception.getLineNumber() +
                "  URI:     " +
                exception.getSystemId() +
                "  Message: " +
                exception.getMessage());
        throw new SAXException("Warning encountered");
    }

    public void error(SAXParseException exception) throws SAXException {
        // Bring things to a crashing halt
        System.out.println("**Parsing Error**" +
                "  Line:    " +
                exception.getLineNumber() +
                "  URI:     " +
                exception.getSystemId() +
                "  Message: " +
                exception.getMessage());
        throw new SAXException("Error encountered");
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        // Bring things to a crashing halt
        System.out.println("**Parsing Fatal Error**" +
                "  Line:    " +
                exception.getLineNumber() +
                "  URI:     " +
                exception.getSystemId() +
                "  Message: " +
                exception.getMessage());
        throw new SAXException("Fatal Error encountered");
    }
}
