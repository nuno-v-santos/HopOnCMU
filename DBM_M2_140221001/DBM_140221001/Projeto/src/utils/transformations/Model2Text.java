/*
 * Model-to-text transformation using Freemarker templates.
 *
 */

package utils.transformations;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class Model2Text {

    private Configuration cfg;

    public Model2Text(String pathname) {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_25);
            cfg.setDirectoryForTemplateLoading(new File(pathname));
            cfg.setDefaultEncoding("UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShared(String key, String value){
        try {
            cfg.setSharedVariable(key, value);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders a model using a template.
     *
     * @param model the model
     * @param name  the template name
     * @return      String
     */
    public String render(Object model, String name) {
        try {
            Template template = cfg.getTemplate(name);
            Writer out = new StringWriter();
            template.process(model, out);
            return out.toString();
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
